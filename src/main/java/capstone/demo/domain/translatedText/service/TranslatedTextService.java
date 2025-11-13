package capstone.demo.domain.translatedText.service;

import capstone.demo.domain.translatedText.repository.TranslatedTextRepository;
import capstone.demo.domain.translatedText.dto.TranslatedTextDTO;
import capstone.demo.domain.user.entity.User;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
@RequiredArgsConstructor
public class TranslatedTextService {

    private final TranslatedTextRepository translatedTextRepository;

    public List<TranslatedTextDTO.TranslatedTextResponseDTO> getTop3ByCount(User user) {
        return translatedTextRepository.findTop3ByUserOrderByCountDesc(user)
                .stream()
                .map(t -> TranslatedTextDTO.TranslatedTextResponseDTO.builder()
                        .id(t.getId())
                        .content(t.getContent())
                        .count(t.getCount())
                        .build())
                .toList();
    }
}
