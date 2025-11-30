package capstone.demo.domain.voice.dto;

import capstone.demo.domain.voice.Voice;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;
import java.util.List;

public class VoiceDTO {
    @Builder
    @Getter
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoiceListResponseDTO {
        private Integer totalCount;
        private List<VoiceDetailDTO> voices;

    }

    @Getter
    @Builder
    @NoArgsConstructor
    @AllArgsConstructor
    public static class VoiceDetailDTO {
        private Long voiceId;
        private String objectKey;
        private String translatedText;
        private String translatedText_objectKey;
        private LocalDateTime createdAt;

        public static VoiceDetailDTO from(Voice voice, String objectKey) {
            return VoiceDetailDTO.builder()
                    .voiceId(voice.getId())
                    .objectKey(voice.getObjectKey())
                    .translatedText(voice.getTranslatedText().getContent())
                    .translatedText_objectKey(objectKey)
                    .createdAt(voice.getCreatedAt())
                    .build();
        }
    }
}
