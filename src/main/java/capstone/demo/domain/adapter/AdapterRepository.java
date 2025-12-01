package capstone.demo.domain.adapter;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.repository.CrudRepository;

import java.util.Optional;

public interface AdapterRepository extends JpaRepository<Adapter, Integer> {

    Optional<Adapter> findByUserId(Long id);
}
