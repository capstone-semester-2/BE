package capstone.demo.domain.auth.service;

import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;
import capstone.demo.domain.auth.dto.LoginResponseDTO;
import capstone.demo.domain.user.LoginType;
import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.user.UserRole;
import capstone.demo.domain.user.repository.UserRepository;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;
import capstone.demo.global.redis.RedisService;
import capstone.demo.global.security.dto.TokenResponse;
import capstone.demo.global.security.dto.TokenResponseDTO;
import capstone.demo.global.security.jwt.TokenProvider;
import capstone.demo.global.security.kakao.KakaoDTO;
import capstone.demo.global.security.kakao.KakaoUtil;

import java.util.UUID;
import java.util.concurrent.atomic.AtomicBoolean;

@Service
@RequiredArgsConstructor
public class AuthService {
    private final KakaoUtil kakaoUtil;
    private final UserRepository userRepository;
    private final TokenProvider tokenProvider;
    private final PasswordEncoder passwordEncoder;
    private final RedisService redisService;

    private static final String REFRESH_TOKEN_PREFIX = "refresh:";

    @Value("${jwt.token.refresh-expiration-time}")
    private long refreshExpirationTime;

    @Transactional
    public LoginResponseDTO.LoginTokenResponseDto kakaoLogin(String accessCode, String redirectUri) {
        KakaoDTO.OAuthToken oAuthToken = kakaoUtil.requestToken(accessCode,redirectUri);
        KakaoDTO.KakaoProfile kakaoProfile = kakaoUtil.requestProfile(oAuthToken);

        String email = kakaoProfile.getKakao_account().getEmail();
        String name = kakaoProfile.getProperties().getNickname();

        return loginUser(email,name,LoginType.KAKAO);
    }

    @Transactional
    public LoginResponseDTO.LoginTokenResponseDto loginUser(String email, String name, LoginType loginType) {
        AtomicBoolean isFirst = new AtomicBoolean(false);

        User user = userRepository.findByEmailAndDeletedAtIsNull(email)
                .map(existingUser -> existingUser)
                .orElseGet(() -> {
                    isFirst.set(true);
                    return createUser(email, name, loginType);
                });

        TokenResponse tokenResponse = tokenProvider.createToken(user.getId());
        String key = REFRESH_TOKEN_PREFIX + user.getId();
        redisService.setValue(key, tokenResponse.getRefreshToken(), refreshExpirationTime);

        return LoginResponseDTO.LoginTokenResponseDto.of(
                user.getId(),
                tokenResponse.getAccessToken(),
                tokenResponse.getRefreshToken(),
                isFirst.get()
        );
    }

    @Transactional
    public LoginResponseDTO.RecreateTokenResponseDto recreateToken(String refreshToken) {
        tokenProvider.validateToken(refreshToken);

        Long userId = Long.valueOf(tokenProvider.getUserIdFromToken(refreshToken));

        String key = REFRESH_TOKEN_PREFIX + userId;
        String storedToken = redisService.getValue(key);
        if (storedToken == null || !storedToken.equals(refreshToken)) {
            throw new GeneralException(ErrorStatus.TOKEN_INVALID);
        }

        TokenResponseDTO.RefreshTokenResponseDto newTokenDTO = tokenProvider.recreate(userId);
        redisService.setValue(key, newTokenDTO.getRefreshToken(),refreshExpirationTime);

        return LoginResponseDTO.RecreateTokenResponseDto.of(
                userId,
                newTokenDTO.getAccessToken(),
                newTokenDTO.getRefreshToken()
        );
    }

    @Transactional
    public void logout(User user) {
        redisService.deleteValue(REFRESH_TOKEN_PREFIX + user.getId());
    }

    private User createUser(String email, String name, LoginType loginType) {
        String rawPassword = UUID.randomUUID().toString();

        User user =User.builder()
                .email(email)
                .name(name)
                .password(passwordEncoder.encode(rawPassword))
                .userRole(UserRole.USER)
                .loginType(loginType)
                .build();

        return userRepository.save(user);
    }
}
