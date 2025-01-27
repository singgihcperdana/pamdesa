package org.pamdesa.repository;

import org.pamdesa.model.projection.PathAndMethod;
import org.pamdesa.model.entity.RoleEndpoint;
import org.pamdesa.model.enums.UserRole;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface RoleEndpointRepository extends JpaRepository<RoleEndpoint, Long> {

    @Query("SELECT new org.pamdesa.model.projection.PathAndMethod(e.path, re.method) " +
            " FROM RoleEndpoint re JOIN re.role r JOIN re.endpoint e " +
            " WHERE r.userRole IN :roles")
    List<PathAndMethod> findPathsByRoleIn(@Param("roles") List<UserRole> roles);

    @Query("SELECT CASE WHEN COUNT(re) > 0 THEN true ELSE false END " +
            "FROM RoleEndpoint re JOIN re.role r JOIN re.endpoint e " +
            "WHERE r.userRole IN :roles AND e.path = :path AND re.method = :method")
    boolean existsByRoleAndPathAndMethod(@Param("roles") List<UserRole> roles,
                                         @Param("path") String path,
                                         @Param("method") String method);
}
