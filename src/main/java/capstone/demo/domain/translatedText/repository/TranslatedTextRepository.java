package capstone.demo.domain.translatedText.repository;

import capstone.demo.domain.translatedText.TranslatedText;
import capstone.demo.domain.user.entity.User;
import org.springframework.data.repository.CrudRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TranslatedTextRepository extends CrudRepository<TranslatedText, Long> {

    List<TranslatedText> findTop3ByUserOrderByCountDesc(User user);

    Optional<TranslatedText> findByUserAndContent(User user, String content);
}
