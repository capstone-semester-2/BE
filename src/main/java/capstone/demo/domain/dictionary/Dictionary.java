package capstone.demo.domain.dictionary;

import capstone.demo.global.entity.BaseEntity;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Getter
@Builder
@Setter
@AllArgsConstructor
@NoArgsConstructor
public class Dictionary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dictionary_id")
    private Long id;

    @Column(length = 50, name = "gesture_name")
    private String gestureName;

    @Column(length = 512, name = "object_key")
    private String objectKey;


}
