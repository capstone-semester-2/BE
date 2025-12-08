package capstone.demo.domain.voice.controller;

import capstone.demo.domain.emitter.EmitterService;
import capstone.demo.domain.voice.dto.AsyncResponseDTO;
import capstone.demo.domain.fileload.dto.FileUploadCompleteDTO;
import capstone.demo.domain.voice.dto.VoiceDTO;
import capstone.demo.domain.voice.entity.VoiceModel;
import capstone.demo.domain.voice.service.VoiceService;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import capstone.demo.global.security.jwt.TokenProvider;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

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
            description = "S3 업로드 완료 후 objectKey, uuid(emitterId), 모델을 종류를 전달받아, DB 저장 및 AI 서버 분석 요청을 수행합니다.")
    public ResponseEntity<ApiResponse<AsyncResponseDTO.AsyncTranslateDTO>> uploadCompleteAndAnalyze(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody FileUploadCompleteDTO.UploadCompleteRequest request,
            @RequestParam VoiceModel voiceModel) {

        AsyncResponseDTO.AsyncTranslateDTO response = voiceService.uploadCompleteAndAnalyze(authDetails.user(), request, voiceModel);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

    @PostMapping("/voices/ai-learning")
    @Operation(summary = "음성 학습 API",
            description = "S3 업로드 완료 후 objectKeys, 모델을 종류를 전달받아, DB 저장 및 AI 모델 학습을 요청합니다.")
    public ResponseEntity<ApiResponse<AsyncResponseDTO.AsyncTranslateDTO>> uploadCompleteAndLearning(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody FileUploadCompleteDTO.UploadCompleteAndLearningRequest request,
            @RequestParam VoiceModel voiceModel) {

        AsyncResponseDTO.AsyncTranslateDTO response = voiceService.handleUploadCompleteAndLearning(authDetails.user(), request, voiceModel);

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

    @GetMapping("/voices/islearned")
    @Operation(summary = "사용자 음성 학습 여부", description = "사용자의 음성 학습 여부를 확인합니다.")
    public ResponseEntity<ApiResponse<VoiceDTO.isLearnedVoiceDTO>> isLearnedVoice(
            @AuthenticationPrincipal AuthDetails authDetails
    ){
        return ResponseEntity.ok(
                ApiResponse.onSuccess(voiceService.isLearnedVoice(authDetails.user()))
        );
    }




    @DeleteMapping("/voices/reset-learning")
    @Operation(summary = "사용자 음성 학습 초기화", description = "사용자의 음성 학습을 초기화합니다.")
    public ResponseEntity<ApiResponse<>>




}
