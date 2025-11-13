package capstone.demo.domain.translatedText.dto;

import jakarta.persistence.Column;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

public class TranslatedTextDTO {

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    static public class TranslatedTextResponseDTO {
        private Long id;
        private String content;
        private Integer count;

    }
}
