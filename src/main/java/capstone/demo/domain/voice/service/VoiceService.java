package capstone.demo.domain.voice.service;

import capstone.demo.domain.emitter.EmitterService;
import capstone.demo.domain.fileload.FileLoadService;
import capstone.demo.domain.voice.dto.AsyncResponseDTO;
import capstone.demo.domain.voice.dto.FileUploadCompleteDTO;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;

import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class VoiceService {

    private final FileLoadService fileLoadService;
    private final AiClientService aiClientService;
    private final EmitterService emitterService;
    private final ThreadPoolTaskExecutor voiceExecutor;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    public AsyncResponseDTO.AsyncTranslateDTO handleUploadComplete(
            Long userId, FileUploadCompleteDTO.UploadCompleteRequest request) {

            CompletableFuture.supplyAsync(() -> {
                        String presignedUrl = fileLoadService.generatePreSignGetUrl(request.getObjectKey(), bucketName);
                        return aiClientService.requestVoiceAnalysis(request.getEmitterId(), presignedUrl);
                    }, voiceExecutor)
                    .thenAccept(result -> {
                        emitterService.sendToEmitter(userId, request.getEmitterId(),"complete", result);
                    })
                    .exceptionally(ex -> {
                        emitterService.sendToEmitter(userId, request.getEmitterId(),"error", ex.getMessage());
                        return null;
                    });

        return AsyncResponseDTO.AsyncTranslateDTO.builder()
                .message("번역이 진행중입니다.")
                .build();
    }
}
