package capstone.demo.global.security.dto;

import lombok.AllArgsConstructor;
import lombok.Getter;

public class TokenResponseDTO {

    @Getter
    @AllArgsConstructor
    public static class RefreshTokenResponseDto {
        private Long userId;
        private String accessToken;
        private String refreshToken;

        public static RefreshTokenResponseDto of(Long userId, String accessToken,String refreshToken) {
            return new RefreshTokenResponseDto(userId, accessToken,refreshToken);
        }
    }
}
