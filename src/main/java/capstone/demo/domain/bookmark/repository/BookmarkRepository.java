package capstone.demo.domain.bookmark.repository;

import capstone.demo.domain.bookmark.Bookmark;
import capstone.demo.domain.dictionary.Dictionary;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface BookmarkRepository extends JpaRepository<Bookmark, Long> {

    @Query(value = """
    SELECT d.* 
    FROM dictionary d
    JOIN dictionary_bookmark b ON d.dictionary_id = b.dictionary_id
    WHERE b.user_id = :userId
    ORDER BY d.dictionary_id DESC
    LIMIT :size
    """,
            nativeQuery = true)
    List<Dictionary> getFirstPage(Long userId, int size);


    @Query(value = """
    SELECT d.* 
    FROM dictionary d
    JOIN dictionary_bookmark b ON d.dictionary_id = b.dictionary_id
    WHERE b.user_id = :userId
        AND d.dictionary_id < :lastId   
    ORDER BY d.dictionary_id DESC
    LIMIT :size
    """,
            nativeQuery = true)
    List<Dictionary> getNextPage(Long userId, Long lastId, int size);
}
