package top.desq.repository;

import org.springframework.data.jpa.repository.JpaRepository;
import top.desq.entity.User;

import java.util.Optional;

public interface UserRepository extends JpaRepository<User, Long> {

    Optional<User> findByEmail(String email);
}
