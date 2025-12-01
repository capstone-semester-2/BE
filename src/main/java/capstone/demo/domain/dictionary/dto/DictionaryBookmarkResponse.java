package capstone.demo.domain.dictionary.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class DictionaryBookmarkResponse {
    private Long dictionaryId;
    private boolean bookmarked;

    public static DictionaryBookmarkResponse of(Long id, boolean bookmarked) {
        return DictionaryBookmarkResponse.builder()
                .dictionaryId(id)
                .bookmarked(bookmarked)
                .build();
    }
}
