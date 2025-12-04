package capstone.demo.domain.dictionary.repository;

import capstone.demo.domain.dictionary.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface DictionaryRepository extends JpaRepository<Dictionary, Long> {
    @Query(value = "SELECT * FROM dictionary ORDER BY dictionary_id DESC LIMIT :size",
            nativeQuery = true)
    List<Dictionary> getFirstPage(@Param("size") int size);

    @Query(
            value = """
        SELECT * 
        FROM dictionary
        WHERE dictionary_id < :lastId
        ORDER BY dictionary_id DESC 
        LIMIT :size
        """,
            nativeQuery = true
    )
    List<Dictionary> getNextPage(@Param("lastId") Long lastId,
                                  @Param("size") int size);


    @Query(value = "SELECT * FROM dictionary " +
            "WHERE gesture_name LIKE %:keyword% " +
            "ORDER BY dictionary_id DESC",
            nativeQuery = true)
    List<Dictionary> findAllByKeyword(String keyword);

    Optional<Dictionary> findByGestureName(String gestureName);
}
