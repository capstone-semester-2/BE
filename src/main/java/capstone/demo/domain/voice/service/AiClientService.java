package capstone.demo.domain.voice.service;

import capstone.demo.domain.fileload.dto.PreSignedUrlInfo;
import capstone.demo.domain.voice.dto.AiResultDTO;
import capstone.demo.domain.voice.entity.VoiceModel;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.List;
import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class AiClientService {

    private final WebClient webClient;

    @Value("${ai.server.translate-url}")
    private String translateUrl;

    @Value("${ai.server.train-url}")
    private String trainUrl;

    public AiResultDTO.AiResultResponseDTO requestVoiceAnalysis(String emitterId, String presignedUrl, VoiceModel voiceModel, Long adapterNumberToUse) {

        String aiServerUrl = translateUrl + voiceModel.toString().toLowerCase();
        System.out.println("aiServerUrl = " + aiServerUrl);

        Map<String, Object> body = Map.of(
                "audioUrl", presignedUrl,
                "emitterId", emitterId,
                "adapterId", adapterNumberToUse
        );
        log.info("ai 서버 요청 보낸 후");

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AiResultDTO.AiResultResponseDTO.class)
                .block();    // 응답 DTO로 반환
    }


    public AiResultDTO.AiLearningResultResponseDTO requestVoiceLearning(String emitterId, List<PreSignedUrlInfo> presignedUrls, VoiceModel voiceModel) {
        String aiServerUrl = trainUrl + "adapter-train";

        System.out.println("aiServerUrl = " + aiServerUrl);

        Map<String, Object> body = Map.of(
                "engine", voiceModel.toString().toLowerCase(),
                "pairs", presignedUrls,
                "emitterId", emitterId
        );
        log.info("ai 학습 요청 보낸 후");

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AiResultDTO.AiLearningResultResponseDTO.class)
                .block();    // 응답 DTO로 반환
    }
}
