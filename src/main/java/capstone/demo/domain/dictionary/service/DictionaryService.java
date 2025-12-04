package capstone.demo.domain.dictionary.service;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.dictionary.dto.DictionaryBookmarkResponse;
import capstone.demo.domain.bookmark.service.BookmarkService;
import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.dto.DictionaryResponse;
import capstone.demo.domain.dictionary.repository.DictionaryRepository;
import capstone.demo.domain.user.entity.User;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.handler.NotFoundHandler;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;
import java.util.Set;

@Service
@RequiredArgsConstructor
public class DictionaryService {
//    private final S3FileService s3FileService;
    private final DictionaryRepository dictionaryRepository;
    private final BookmarkService bookmarkService;

    public List<DictionaryResponse> getFirstPage(User user, int size) {
        Set<Long> bookmarkedIds = bookmarkService.getBookmarkDictionaryIds(user);

        List<Dictionary> dictionaries = dictionaryRepository.getFirstPage(size);

        return dictionaries.stream()
                .map(d -> DictionaryResponse.builder()
                        .id(d.getId())
                        .gestureName(d.getGestureName())
                        .objectKey(d.getObjectKey())
                        .isbookmarked(bookmarkedIds.contains(d.getId()))
                        .build())
                .toList();


    }

    public List<DictionaryResponse> getNextPage(User user, Long lastId, int size) {
        Set<Long> bookmarkedIds = bookmarkService.getBookmarkDictionaryIds(user);

        List<Dictionary> dictionaries = dictionaryRepository.getNextPage(lastId, size);

        return dictionaries.stream()
                .map(d -> DictionaryResponse.builder()
                        .id(d.getId())
                        .gestureName(d.getGestureName())
                        .objectKey(d.getObjectKey())
                        .isbookmarked(bookmarkedIds.contains(d.getId()))
                        .build())
                .toList();

    }

    public Dictionary getById(Long id) {
        return dictionaryRepository.findById(id).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_DICTIONARY));
    }

    public List<Dictionary> searchAll(String keyword) {
        return dictionaryRepository.findAllByKeyword(keyword);
    }

    public Dictionary getByGestureName(String gestureName) {
        return dictionaryRepository.findByGestureName(gestureName).orElse(null);
    }


    public DictionaryBookmarkResponse toggleBookmark(User user, Long dictionaryId) {

        Optional<Bookmark> existing =
                bookmarkService.findByUserAndDictionaryId(user, dictionaryId);

        // 이미 있으면 삭제 → "북마크 해제됨" 반환
        if (existing.isPresent()) {
            bookmarkService.delete(existing.get());
            return DictionaryBookmarkResponse.of(dictionaryId, false);
        }

        Dictionary dictionary = getById(dictionaryId);

        Bookmark bookmark = Bookmark.builder()
                .user(user)
                .dictionary(dictionary)
                .build();

        bookmarkService.save(bookmark);

        return DictionaryBookmarkResponse.of(dictionaryId, true); // bookmarked = true
    }


//    public void updateObjectKeysFromS3(String bucketName) {
//
//        // 1. S3에서 videos/ 아래 key 전체 조회 (정렬된 상태)
//        List<String> s3Keys = s3FileService.listObjectKeys("videos/", bucketName);
//
//        // S3는 prefix 폴더 자체("videos/")의 빈 엔트리를 포함할 수 있으므로 제거
//        s3Keys = s3Keys.stream()
//                .filter(key -> !key.equals("videos/"))
//                .sorted()
//                .toList();
//
//        // 2. DB에서 gesture_name 정렬 순으로 dictionary 조회
//        List<Dictionary> dictionaries = dictionaryRepository.findAll(Sort.by("id"));
//
//        if (dictionaries.size() != s3Keys.size()) {
//            throw new IllegalStateException("DB row count and S3 object count do not match!");
//        }
//
//        for (int i = 0; i < dictionaries.size(); i++) {
//            Dictionary dict = dictionaries.get(i);
//            dict.setObjectKey(s3Keys.get(i));  // 엔티티에 setter 필요
//        }
//
//        dictionaryRepository.saveAll(dictionaries);
//    }
}
