package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.BannerEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface BannerRepository extends JpaRepository<BannerEntity, Long> {
}
