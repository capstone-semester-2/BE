package capstone.demo.domain.voice.service;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;


@Component
@RequiredArgsConstructor
public class AiClientService {

//    private final RestTemplate restTemplate; //webconfig 로 수정.

    public String requestVoiceAnalysis(String emitterId, String presignedUrl) {
//        String aiServerUrl = "http://ai-server:8000/api/analyze";
//
//        Map<String, Object> body = Map.of("audioUrl", presignedUrl);
//
//        ResponseEntity<String> response = restTemplate.postForEntity(
//                aiServerUrl,
//                body,
//                String.class
//        );
//
//        return response.getBody(); // AI 서버에서 반환한 분석 결과
        return null;
    }
}
