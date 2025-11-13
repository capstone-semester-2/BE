package capstone.demo.domain.translatedText.controller;

import capstone.demo.domain.translatedText.service.TranslatedTextService;
import capstone.demo.domain.translatedText.dto.TranslatedTextDTO;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@RestController
@RequiredArgsConstructor
public class TranslatedTextController {

    private final TranslatedTextService translatedTextService;

    @GetMapping("text/top3")
    @Operation(summary = "번역 텍스트 Top3 조회하기",
            description = "번역된 텍스트의 count수 상위 3개의 항목을 조회하는 API입니다.")
    public ResponseEntity<ApiResponse<List<TranslatedTextDTO.TranslatedTextResponseDTO>>> getTop3(
            @AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(
                ApiResponse.onSuccess(translatedTextService.getTop3ByCount(authDetails.user()))
        );
    }

}
