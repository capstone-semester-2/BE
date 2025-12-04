package capstone.demo.domain.voice.dto;

import lombok.*;

import java.util.Map;

public class AiResultDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    @ToString
    public static class AiResultResponseDTO {
        private String requestId; // uuid
        private String sha256;
        private Integer elapsedMs;
        private Map<String, String> result; //아래와 같음.
//            {
//                "title":"<원본파일명.wav>",
//                "text":"<인식 텍스트>",
//                "model_type":"DeepSpeech2
//            }
    }


    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiLearningResultResponseDTO {
        private String requestId; // uuid
        private Long adapterId;
    }


}
