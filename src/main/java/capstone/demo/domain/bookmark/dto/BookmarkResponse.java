package capstone.demo.domain.bookmark.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class BookmarkResponse {
    private Long dictionaryId;
    private boolean bookmarked;

    public static BookmarkResponse of(Long id, boolean bookmarked) {
        return BookmarkResponse.builder()
                .dictionaryId(id)
                .bookmarked(bookmarked)
                .build();
    }
}
