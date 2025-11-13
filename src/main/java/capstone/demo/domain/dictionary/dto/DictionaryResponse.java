package capstone.demo.domain.dictionary.dto;


import lombok.*;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryResponse {
    private Long id;
    private String gestureName;
    private String gestureUrl;
}
