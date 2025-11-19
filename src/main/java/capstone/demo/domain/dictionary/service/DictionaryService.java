package capstone.demo.domain.dictionary.service;

import capstone.demo.domain.dictionary.Dictionary;
import capstone.demo.domain.dictionary.repository.DictionaryRepository;
import capstone.demo.global.apiPayload.code.status.ErrorStatus;
import capstone.demo.global.apiPayload.exception.handler.NotFoundHandler;
import com.amazonaws.services.kms.model.NotFoundException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class DictionaryService {
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
}
