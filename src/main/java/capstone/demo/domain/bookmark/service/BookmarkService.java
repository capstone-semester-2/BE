package capstone.demo.domain.bookmark.service;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.bookmark.repository.BookmarkRepository;
import capstone.demo.domain.dictionary.Dictionary;
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

    public Optional<Bookmark> findByUserAndDictionaryId(User user, Long dictionaryId) {
        return bookmarkRepository.findByUserAndDictionaryId(user, dictionaryId);
    }

    public void delete(Bookmark bookmark) {
        bookmarkRepository.delete(bookmark);
    }

    public void save(Bookmark bookmark) {
        bookmarkRepository.save(bookmark);
    }
}
