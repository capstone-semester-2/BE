package capstone.demo.domain.translatedText.controller;

import capstone.demo.domain.translatedText.service.TranslatedTextService;
import capstone.demo.domain.translatedText.dto.TranslatedTextDTO;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
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
    public ResponseEntity<ApiResponse<List<TranslatedTextDTO.TranslatedTextResponseDTO>>> getTop3(
            @AuthenticationPrincipal AuthDetails authDetails) {
        return ResponseEntity.ok(
                ApiResponse.onSuccess(translatedTextService.getTop3ByCount(authDetails.user()))
        );
    }

}
