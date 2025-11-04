package capstone.demo.global.security.jwt;

import io.jsonwebtoken.*;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.InitializingBean;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.stereotype.Component;
import org.springframework.util.StringUtils;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;
import capstone.demo.global.redis.RedisService;
import capstone.demo.global.security.dto.TokenResponse;
import capstone.demo.global.security.dto.TokenResponseDTO;

import java.security.Key;
import java.util.Date;


@Component
@RequiredArgsConstructor
public class TokenProvider implements InitializingBean {

    @Value("${jwt.secret}")
    private String secretKey;
    private Key key;

    @Value("${jwt.token.access-expiration-time}")
    private long accessExpirationTime;

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    private final UserDetailsService userDetailsService;
    private final RedisService redisService;

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";


    @Override
    public void afterPropertiesSet(){
        byte[] keyBytes = Decoders.BASE64.decode(secretKey);
        this.key = Keys.hmacShaKeyFor(keyBytes);
    }

    public String createAccessToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + accessExpirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key, SignatureAlgorithm.HS512)
                .compact();
    }

    public String createRefreshToken(Long userId) {
        Date now = new Date();
        Date expiration = new Date(now.getTime() + refreshExpirationTime);

        return Jwts.builder()
                .setSubject(String.valueOf(userId))
                .setIssuedAt(now)
                .setExpiration(expiration)
                .signWith(key,SignatureAlgorithm.HS256)
                .compact();
    }

    public TokenResponse createToken(Long userId) {
        return TokenResponse.builder()
                .accessToken(createAccessToken(userId))
                .refreshToken(createRefreshToken(userId))
                .build();
    }

    public TokenResponseDTO.RefreshTokenResponseDto recreate(Long userId) {
        String accessToken = createAccessToken(userId);
        String newRefreshToken = createRefreshToken(userId);

        return TokenResponseDTO.RefreshTokenResponseDto.of(userId,accessToken,newRefreshToken);
    }

    public Long getExpirationTime(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody()
                .getExpiration()
                .getTime();
    }

    public String resolveToken(HttpServletRequest request){
        String token = request.getHeader("Authorization");
        if (StringUtils.hasText(token) && token.startsWith("Bearer ")) {
            return token.substring(7);
        }
        return null;
    }

    //userId 추출
    public String getUserIdFromToken(String token){
        Claims claims = Jwts.parserBuilder()
                .setSigningKey(key)
                .build()
                .parseClaimsJws(token)
                .getBody();
        return claims.getSubject();
    }

    public Authentication getAuthentication(String token){
        UserDetails userDetails =
                (UserDetails)
                        userDetailsService.loadUserByUsername(getUserIdFromToken(token));
        return new UsernamePasswordAuthenticationToken(
                userDetails, token, userDetails.getAuthorities());
    }

    @Transactional
    public void invalidateToken(String token) {
        if (!validateToken(token)) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }
        redisService.deleteValue(REFRESH_TOKEN_PREFIX+getUserIdFromToken(token));
    }

    public boolean validateToken(String token){
        try {
            Jwts.parserBuilder()
                    .setSigningKey(key)
                    .build()
                    .parseClaimsJws(token);
            return true;
        } catch (SecurityException | MalformedJwtException ex) {
            throw new IllegalArgumentException("Invalid JWT signature or token malformed: "+ ex.getMessage());
        } catch (ExpiredJwtException ex) {
            throw new IllegalArgumentException("Expired JWT token: "+ ex.getMessage());
        } catch (UnsupportedJwtException ex) {
            throw new IllegalArgumentException("Unsupported JWT token: "+ ex.getMessage());
        } catch (IllegalArgumentException ex) {
            throw new IllegalArgumentException("JWT token compact of null or empty: "+ ex.getMessage());
        }
    }

}
