package capstone.demo.domain.voice;

import capstone.demo.domain.voice.entity.Voice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface VoiceRepository extends JpaRepository<Voice, Long> {
    @Query(value =
            "SELECT v.* " +
                    "FROM voice v " +
                    "JOIN translated_text t ON v.translated_text_id = t.translated_text_id " +
                    "WHERE v.user_id = :userId " +
                    "ORDER BY v.user_voice_id DESC " +
                    "LIMIT :size",
            nativeQuery = true)
    List<Voice> findFirstPage(@Param("userId") Long userId, @Param("size") int size);

    @Query(value =
            "SELECT v.* " +
                    "FROM voice v " +
                    "JOIN translated_text t ON v.translated_text_id = t.translated_text_id " +
                    "WHERE v.user_id = :userId " +
                    "AND v.user_voice_id < :lastId " +
                    "ORDER BY v.user_voice_id DESC " +
                    "LIMIT :size",
            nativeQuery = true)
    List<Voice> findNextPage(@Param("userId") Long userId,
                             @Param("lastId") Long lastId,
                             @Param("size") int size);

    @Query(value = "SELECT COUNT(*) FROM voice v WHERE v.user_id = :userId", nativeQuery = true)
    int countByUserId(@Param("userId") Long userId);

}
