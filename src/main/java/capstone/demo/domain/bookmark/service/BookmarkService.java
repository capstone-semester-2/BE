package capstone.demo.domain.bookmark.service;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.bookmark.dto.BookmarkResponse;
import capstone.demo.domain.bookmark.repository.BookmarkRepository;
import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.service.DictionaryService;
import capstone.demo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final DictionaryService dictionaryService;

    public BookmarkResponse addBookmark(User user, Long dictionaryId) {
        Dictionary dictionary = dictionaryService.getById(dictionaryId);
        Bookmark bookmark = Bookmark.builder().dictionary(dictionary).user(user).build();

        bookmarkRepository.save(bookmark);

        return BookmarkResponse.builder()
                .id(dictionary.getId())
                .message("북마크 저장 성공")
                .build();

    }
}
