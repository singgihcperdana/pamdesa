package org.pamdesa.repository;

import org.pamdesa.model.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import javax.transaction.Transactional;
import java.util.Optional;

@Repository
public interface UserRepository extends JpaRepository<User, String> {

  @Transactional
  @Query("SELECT u FROM User u "
      + " LEFT JOIN FETCH u.organization "
      + " LEFT JOIN FETCH u.rate "
      + " WHERE u.email = ?1")
  Optional<User> findByEmailFetchData(String username);

  Optional<User> findByEmail(String email);

  Optional<User> findByIdAndOrganization_Id(String id, String organizationId);

  Optional<User> findByMeterIdAndOrganization_Id(String meterId, String organizationId);

}
