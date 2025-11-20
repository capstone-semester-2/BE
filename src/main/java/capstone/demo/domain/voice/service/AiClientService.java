package capstone.demo.domain.voice.service;

import capstone.demo.domain.voice.dto.AiResultDTO;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;
import org.springframework.web.reactive.function.client.WebClient;

import java.util.Map;


@Component
@RequiredArgsConstructor
@Slf4j
public class AiClientService {

    private final WebClient webClient;

    @Value("${ai.server.url}")
    private String ServerUrl;

    @Value("${ai.server.korean-endpoint}")
    private String koreanEndpoint;

    public AiResultDTO.AiResultResponseDTO requestVoiceAnalysis(String emitterId, String presignedUrl) {

        String aiServerUrl = ServerUrl + koreanEndpoint;

        Map<String, Object> body = Map.of(
                "audioUrl", presignedUrl,
                "emitterId", emitterId
        );
        log.info("ai 요청 보낸 후");

        return webClient.post()
                .uri(aiServerUrl)
                .bodyValue(body)
                .retrieve()
                .bodyToMono(AiResultDTO.AiResultResponseDTO.class)
                .block();    // 응답 DTO로 반환
    }



}
