package capstone.demo.domain.voice.service;

import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.service.DictionaryService;
import capstone.demo.domain.emitter.EmitterService;
import capstone.demo.domain.fileload.S3FileService;
import capstone.demo.domain.translatedText.TranslatedText;
import capstone.demo.domain.translatedText.service.TranslatedTextService;
import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.voice.Voice;
import capstone.demo.domain.voice.VoiceRepository;
import capstone.demo.domain.voice.dto.AiResultDTO;
import capstone.demo.domain.voice.dto.AsyncResponseDTO;
import capstone.demo.domain.voice.dto.FileUploadCompleteDTO;
import capstone.demo.domain.voice.dto.VoiceDTO;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.GeneralException;
import capstone.demo.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Map;
import java.util.concurrent.CompletableFuture;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
@Slf4j
public class VoiceService {

    private final S3FileService s3FileService;
    private final AiClientService aiClientService;
    private final EmitterService emitterService;
    private final ThreadPoolTaskExecutor voiceExecutor;
    private final VoiceRepository voiceRepository;
    private final TranslatedTextService translatedTextService;
    private final DictionaryService dictionaryService;

    @Value("${amazon.aws.bucket}")
    private String bucketName;

    public AsyncResponseDTO.AsyncTranslateDTO handleUploadComplete(
            User user, FileUploadCompleteDTO.UploadCompleteRequest request) {
            Long userId = user.getId();
            log.info("ai 요청 준비");

            CompletableFuture.supplyAsync(() -> {
                        String presignedUrl = s3FileService.generatePreSignGetUrl(request.getObjectKey(), bucketName);
                        return aiClientService.requestVoiceAnalysis(request.getEmitterId(), presignedUrl);
                    }, voiceExecutor)
                    .thenAccept(result -> {
                        emitterService.sendToEmitter(userId, request.getEmitterId(),"complete", result);
                        saveVoiceAnalysis(user, request.getObjectKey(), result);
                        log.info("sse 결과 출력 성공");
                    })
                    .exceptionally(ex -> {
                        emitterService.sendToEmitter(userId, request.getEmitterId(),"error", ex.getMessage());
                        log.info("sse 결과 출력 error");
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

        List<String> contents = voices.stream()
                .map(v -> v.getTranslatedText().getContent())
                .toList();

        List<Dictionary> dictionaries = dictionaryService.findAllByGestureNameIn(contents);

        Map<String, String> dictMap = dictionaries.stream()
                .collect(Collectors.toMap(
                        Dictionary::getGestureName,
                        Dictionary::getObjectKey
                ));

        return VoiceDTO.VoiceListResponseDTO.builder()
                .totalCount(totalCount)
                .voices(
                        voices.stream()
                                .map(voice -> VoiceDTO.VoiceDetailDTO.from(
                                        voice,
                                        dictMap.get(voice.getTranslatedText().getContent()) // 매칭된 objectKey
                                ))
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

         s3FileService.deleteObjectFromS3(voice.getObjectKey(), bucketName);

        voiceRepository.delete(voice);
    }


    public void saveVoiceAnalysis(User user, String objectKey, AiResultDTO.AiResultResponseDTO dto) {

        Map<String, String> responseText = dto.getResult();

        TranslatedText translatedText = translatedTextService.saveTranslatedText(user, responseText.get("text"));

        Voice voice = Voice.builder()
                .user(user)
                .objectKey(objectKey)
                .translatedText(translatedText)
                .build();

        voiceRepository.save(voice);
    }

}
