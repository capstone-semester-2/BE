package capstone.demo.domain.voice.entity;

import capstone.demo.domain.translatedText.TranslatedText;
import capstone.demo.domain.user.entity.User;
import capstone.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@Getter
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Voice extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "user_voice_id")
    private Long id;

    @Column(name = "object_key", length = 512)
    private String objectKey;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "translated_text_id", nullable = false)
    private TranslatedText translatedText;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;


}
