package capstone.demo.domain.dictionary.service;

import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.repository.DictionaryRepository;
import capstone.demo.domain.fileload.S3FileService;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.handler.NotFoundHandler;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryService {
    private final S3FileService s3FileService;
    private final DictionaryRepository dictionaryRepository;

    public List<Dictionary> getFirstPage(int size) {
        return dictionaryRepository.getFirstPage(size);
    }

    public List<Dictionary> getNextPage(Long lastId, int size) {
        return dictionaryRepository.getNextPage(lastId, size);
    }

    public Dictionary getById(Long id) {
        return dictionaryRepository.findById(id).orElseThrow(() -> new NotFoundHandler(ErrorStatus.NOT_FOUND_DICTIONARY));
    }

    public List<Dictionary> searchAll(String keyword) {
        return dictionaryRepository.findAllByKeyword(keyword);
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
