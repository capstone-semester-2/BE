package capstone.demo.domain.auth.dto;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Getter;

public class LoginResponseDTO {
    @Getter
    @AllArgsConstructor
    public static class LoginTokenResponseDto {
        @Schema(description = "사용자 id", example="1")
        private Long userId;
        @Schema(description = "사용자 accessToken", example="exksoijsdjon...")
        private String accessToken;
        @Schema(description = "사용자 refreshToken", example="exjnasoicjkdd...")
        private String refreshToken;
        @Schema(description = "사용자 최초 로그인 여부(처음이면 true)", example="true")
        private boolean isFirst;

        public static LoginTokenResponseDto of(Long userId,String accessToken,String refreshToken,boolean isFirst) {
            return new LoginTokenResponseDto(
                    userId,
                    accessToken,
                    refreshToken,
                    isFirst);
        }
    }

    @Getter
    @AllArgsConstructor
    public static class RecreateTokenResponseDto {
        @Schema(description = "사용자 id", example="1")
        private Long userId;
        @Schema(description = "사용자 accessToken", example="exksoijsdjon...")
        private String accessToken;
        @Schema(description = "사용자 refreshToken", example="exjnasoicjkdd...")
        private String refreshToken;

        public static RecreateTokenResponseDto of(Long userId,String accessToken,String refreshToken) {
            return new RecreateTokenResponseDto(
                    userId,
                    accessToken,
                    refreshToken);
        }
    }
}
