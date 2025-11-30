package capstone.demo.domain.translatedText.service;

import capstone.demo.domain.translatedText.TranslatedText;
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

    public TranslatedText saveTranslatedText(User user, String content) {
        return translatedTextRepository.findByUserAndContent(user, content)
                .map(existing -> {
                    existing.increaseCount();
                    return translatedTextRepository.save(existing);
                })
                .orElseGet(() -> {
                    TranslatedText newText = TranslatedText.builder()
                            .user(user)
                            .content(content)
                            .count(0)
                            .build();
                    return translatedTextRepository.save(newText);
                });
    }
}
