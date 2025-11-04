package capstone.demo.domain.user.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import capstone.demo.domain.user.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {
    Optional<User> findByEmailAndDeletedAtIsNull(String email);
}
