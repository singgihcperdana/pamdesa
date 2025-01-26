package org.pamdesa.repository;

import org.pamdesa.model.entity.Role;
import org.pamdesa.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RoleRepository extends JpaRepository<Role, Long> {
  Optional<Role> findByUserRole(UserRole userRole);
}
