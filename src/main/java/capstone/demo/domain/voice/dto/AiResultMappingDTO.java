package capstone.demo.domain.voice.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.util.List;

public class AiResultMappingDTO {

    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class AiResultMappingResponse {
        private String requestId;
        private List<TextMapping> textMappings;
    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class TextMapping {
        private boolean exists;
        private String word;
        private Long dictionaryId;
        private String objectKey;
    }

}


