package org.pamdesa.repository;

import org.pamdesa.model.entity.Rate;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface RateRepository extends JpaRepository<Rate, String> {

  Optional<Rate> findByCode(String code);

}
