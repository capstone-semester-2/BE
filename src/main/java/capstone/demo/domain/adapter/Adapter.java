package capstone.demo.domain.adapter;

import capstone.demo.domain.user.entity.User;
import capstone.demo.domain.voice.entity.VoiceModel;
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
public class Adapter extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "adapter_id")
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id", unique = true)
    private User user;

    @Column(name = "voice_model")
    @Enumerated(EnumType.STRING)
    private VoiceModel voiceModel;

    @Column(name = "adapter_number")
    private Long adapterNumber;


}
