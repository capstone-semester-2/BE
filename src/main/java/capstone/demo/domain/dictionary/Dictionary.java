package capstone.demo.domain.dictionary;

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
public class Dictionary extends BaseEntity {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = "dictionary_id")
    private Long id;

    @Column(length = 50)
    private String title;

    @Column(length = 512)
    private String gestureUrl;


}
