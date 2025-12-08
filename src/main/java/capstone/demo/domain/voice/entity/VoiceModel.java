package capstone.demo.domain.voice.entity;

import lombok.Getter;

@Getter
public enum VoiceModel {
    KOREAN("일반"),
    HEARING("청각 장애"),
    CP("뇌성 마비"),
    CUSTOM("어댑터");

    private final String description;

    VoiceModel(String description) {
        this.description = description;
    }
}
