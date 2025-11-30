package capstone.demo.domain.bookmark.service;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.bookmark.dto.BookmarkResponse;
import capstone.demo.domain.bookmark.repository.BookmarkRepository;
import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.service.DictionaryService;
import capstone.demo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BookmarkService {
    private final BookmarkRepository bookmarkRepository;
    private final DictionaryService dictionaryService;

    public BookmarkResponse toggleBookmark(User user, Long dictionaryId) {

        Optional<Bookmark> existing =
                bookmarkRepository.findByUserAndDictionaryId(user, dictionaryId);

        // 이미 있으면 삭제 → "북마크 해제됨" 반환
        if (existing.isPresent()) {
            bookmarkRepository.delete(existing.get());
            return BookmarkResponse.of(dictionaryId, false);
        }

        Dictionary dictionary = dictionaryService.getById(dictionaryId);

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .dictionary(dictionary)
                .build();

        bookmarkRepository.save(bookmark);

        return BookmarkResponse.of(dictionaryId, true); // bookmarked = true
    }

    public List<Dictionary> getFirstPage(User user, int size) {
        return bookmarkRepository.getFirstPage(user.getId(), size);
    }

    public List<Dictionary> getNextPage(User user, Long lastId, int size) {
        return bookmarkRepository.getNextPage(user.getId(), lastId, size);

    }

    public Set<Long> getBookmarkDictionaryIds(User user) {
        return bookmarkRepository.findAllByUser(user).stream()
                .map(b -> b.getDictionary().getId())
                .collect(Collectors.toSet());
    }
}
