package capstone.demo.domain.voice.service;

import capstone.demo.domain.emitter.EmitterService;
import capstone.demo.domain.fileload.S3FileService;
import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.voice.Voice;
import capstone.demo.domain.voice.VoiceRepository;
import capstone.demo.domain.voice.dto.AsyncResponseDTO;
import capstone.demo.domain.voice.dto.FileUploadCompleteDTO;
import capstone.demo.domain.voice.dto.VoiceDTO;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;
import capstone.demo.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.concurrent.CompletableFuture;

@Service
@RequiredArgsConstructor
public class VoiceService {

    private final S3FileService s3FileService;
    private final AiClientService aiClientService;
    private final EmitterService emitterService;
    private final ThreadPoolTaskExecutor voiceExecutor;
    private final VoiceRepository voiceRepository;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    public AsyncResponseDTO.AsyncTranslateDTO handleUploadComplete(
            Long userId, FileUploadCompleteDTO.UploadCompleteRequest request) {

            CompletableFuture.supplyAsync(() -> {
                        String presignedUrl = s3FileService.generatePreSignGetUrl(request.getObjectKey(), bucketName);
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

    @Transactional(readOnly = true)
    public VoiceDTO.VoiceListResponseDTO getVoiceList(User user, Long lastId, int size) {

        Long userId = user.getId();
        List<Voice> voices = (lastId == null)
                ? voiceRepository.findFirstPage(userId, size)
                : voiceRepository.findNextPage(userId, lastId, size);

        int totalCount = voiceRepository.countByUserId(userId);

        return VoiceDTO.VoiceListResponseDTO.builder()
                .totalCount(totalCount)
                .voices(
                        voices.stream()
                                .map(VoiceDTO.VoiceDetailDTO::from)
                                .toList()
                )
                .build();
    }

    @Transactional
    public void deleteVoice(User user, Long voiceId) {

        Voice voice = voiceRepository.findById(voiceId)
                .orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_VOICE));

        if (!voice.getUser().getId().equals(user.getId())) {
            throw new GeneralException(ErrorStatus._UNAUTHORIZED);
        }

        // S3 파일 삭제
//         s3FileService.deleteObjectFromS3(voice.getObjectKey(), bucketName);

        voiceRepository.delete(voice);
    }
}
