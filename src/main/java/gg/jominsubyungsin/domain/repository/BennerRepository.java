package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.BennerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BennerRepository extends JpaRepository<BennerEntity, Long> {
}
