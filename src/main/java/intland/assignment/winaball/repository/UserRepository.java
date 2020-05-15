package intland.assignment.winaball.repository;

import intland.assignment.winaball.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

public interface UserRepository extends JpaRepository<User, Integer> {

  User findByEmail(String email);

  boolean existsUserByEmail(String email);
}
