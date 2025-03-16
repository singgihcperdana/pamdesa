package org.pamdesa.repository;

import org.pamdesa.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, Long> {

  @Transactional
  @Query("SELECT u FROM User u "
      + " LEFT JOIN FETCH u.organization "
      + " LEFT JOIN FETCH u.rate "
      + " WHERE u.username = ?1")
  Optional<User> findByUsernameFetchData(String username);

  Optional<User> findByUsername(String username);

  Boolean existsByUsername(String username);

  Boolean existsByEmail(String email);
}
