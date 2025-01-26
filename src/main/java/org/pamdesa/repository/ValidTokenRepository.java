package org.pamdesa.repository;


import org.pamdesa.model.entity.ValidToken;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface ValidTokenRepository extends JpaRepository<ValidToken, Long> {

    Optional<ValidToken> findByToken(String token);

    void deleteByExpirationTimeBefore(java.time.LocalDateTime now);

    void deleteByToken(String token);
}
