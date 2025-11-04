package capstone.demo.domain.auth.controller;

import capstone.demo.domain.auth.dto.LoginResponseDTO;
import capstone.demo.domain.auth.service.AuthService;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.jwt.CookieUtil;
import capstone.demo.global.security.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

@RestController
@RequiredArgsConstructor
public class AuthController {

    private final AuthService authService;
    private final TokenProvider tokenProvider;
    private final CookieUtil cookieUtil;

    @PostMapping("/auth/kakao")
    @Operation(summary = "카카오 로그인", description = "인가 코드를 통해 토큰을 발급받는 카카오 로그인 API입니다.")
    public ApiResponse<LoginResponseDTO.LoginTokenResponseDto> kakaoLogin(@RequestParam String accessCode, @RequestParam String redirectUri) {
        return ApiResponse.onSuccess(authService.kakaoLogin(accessCode, redirectUri));
    }
}
