package capstone.demo.domain.voice.controller;

import capstone.demo.domain.emitter.EmitterService;
import capstone.demo.domain.voice.dto.AiResultDTO;
import capstone.demo.domain.voice.dto.AsyncResponseDTO;
import capstone.demo.domain.voice.dto.FileUploadCompleteDTO;
import capstone.demo.domain.voice.dto.VoiceDTO;
import capstone.demo.domain.voice.service.VoiceService;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import capstone.demo.global.security.jwt.TokenProvider;
import capstone.demo.global.util.GlobalAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
@Slf4j
public class VoiceController {

    private final EmitterService emitterService;
    private final VoiceService voiceService;
    private final TokenProvider tokenProvider;

    @GetMapping(value = "/voices/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE 연결",
            description = "클라이언트와 서버에 SSE 통신을 연결하는 API입니다")
    public SseEmitter connect(@RequestParam String accessToken) {
        Long userId = Long.valueOf(tokenProvider.getUserIdFromToken(accessToken));
        log.info("sse 연결");

        return emitterService.connect(userId);

    }

    @PostMapping("/voices/upload-complete")
    @Operation(summary = "음성 업로드 완료",
            description = "S3 업로드 완료 후 objectKey와 uuid(emitterId)를 전달받아, DB 저장 및 AI 서버 분석 요청을 수행합니다.")
    public ResponseEntity<ApiResponse<AsyncResponseDTO.AsyncTranslateDTO>> uploadComplete(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody FileUploadCompleteDTO.UploadCompleteRequest request) {

        AsyncResponseDTO.AsyncTranslateDTO response = voiceService.handleUploadComplete(authDetails.user(), request);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }


    @GetMapping("/voices")
    @Operation(summary = "음성 기록 무한 스크롤 조회",
            description = "lastId 기반으로 음성 기록을 무한 스크롤 방식으로 조회합니다.")
    public ResponseEntity<ApiResponse<VoiceDTO.VoiceListResponseDTO>> getVoiceList(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestParam(required = false) Long lastId,
            @RequestParam(defaultValue = "5") int size
    ) {
        return ResponseEntity.ok(
                ApiResponse.onSuccess(voiceService.getVoiceList(authDetails.user(), lastId, size))
        );
    }

    @DeleteMapping("/voices/{voiceId}")
    @Operation(summary = "음성 기록 삭제", description = "voiceId 기반으로 음성 기록을 삭제합니다.")
    public ResponseEntity<ApiResponse<String>> deleteVoice(
            @PathVariable Long voiceId,
            @AuthenticationPrincipal AuthDetails authDetails
    ) {

        voiceService.deleteVoice(authDetails.user(), voiceId);

        return ResponseEntity.ok(ApiResponse.onSuccess("삭제 완료"));
    }



}
