package capstone.demo.domain.translatedText;

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
public class TranslatedText extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "translated_text_id")
    private Long id;

    @Column(length = 512)
    private String content;

    private Integer count;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    public synchronized void increaseCount() {
        this.count = this.count + 1;
    }
}

