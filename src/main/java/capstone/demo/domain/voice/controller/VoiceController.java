package capstone.demo.domain.voice.controller;

import capstone.demo.domain.emitter.EmitterService;
import capstone.demo.domain.voice.dto.AiResultDTO;
import capstone.demo.domain.voice.dto.AsyncResponseDTO;
import capstone.demo.domain.voice.dto.FileUploadCompleteDTO;
import capstone.demo.domain.voice.dto.VoiceDTO;
import capstone.demo.domain.voice.service.VoiceService;
import capstone.demo.global.apiPayload.ApiResponse;
import capstone.demo.global.security.AuthDetails;
import capstone.demo.global.util.GlobalAuthUtil;
import io.swagger.v3.oas.annotations.Operation;
import lombok.RequiredArgsConstructor;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.security.core.annotation.AuthenticationPrincipal;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import java.util.List;
import java.util.Map;

@RestController
@RequiredArgsConstructor
public class VoiceController {

    private final EmitterService emitterService;
    private final VoiceService voiceService;

    @GetMapping(value = "/voices/stream", produces = MediaType.TEXT_EVENT_STREAM_VALUE)
    @Operation(summary = "SSE 연결",
            description = "클라이언트와 서버에 SSE 통신을 연결하는 API입니다")
    public SseEmitter connect(@AuthenticationPrincipal AuthDetails authDetails) {
        Long userId = GlobalAuthUtil.extractUserId(authDetails);
        return emitterService.connect(userId);
    }

    @PostMapping("/voices/upload-complete")
    @Operation(summary = "음성 업로드 완료",
            description = "S3 업로드 완료 후 objectKey와 uuid(emitterId)를 전달받아, DB 저장 및 AI 서버 분석 요청을 수행합니다.")
    public ResponseEntity<ApiResponse<AsyncResponseDTO.AsyncTranslateDTO>> uploadComplete(
            @AuthenticationPrincipal AuthDetails authDetails,
            @RequestBody FileUploadCompleteDTO.UploadCompleteRequest request) {

        Long userId = GlobalAuthUtil.extractUserId(authDetails);

        AsyncResponseDTO.AsyncTranslateDTO response = voiceService.handleUploadComplete(userId, request);

        return ResponseEntity.ok(ApiResponse.onSuccess(response));
    }

//    @PostMapping("/voices/ai-callback")
//    @Operation(summary = "AI 분석 결과 콜백",
//            description = "AI 서버에서 분석이 완료되면 이 API로 결과를 전송합니다.")
//    public ResponseEntity<String> receiveAiResult(
//            @AuthenticationPrincipal AuthDetails authDetails,
//            @RequestBody AiResultDTO.AiResultRequestDTO result) {
//
//        Long userId = GlobalAuthUtil.extractUserId(authDetails);
//
//        Map<String, String> analysisResult = result.getResult();
//
//        AiResultDTO.AiResultResponseDTO data = AiResultDTO.AiResultResponseDTO.builder()
//                        .requestId(result.getRequestId())
//                        .text(analysisResult.get("text"))
//                        .build();
//
//        emitterService.sendToEmitter(userId, result.getRequestId(), "complete", data);
//
//        return ResponseEntity.ok("SSE 전송 완료 for user " + userId);
//    }


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
