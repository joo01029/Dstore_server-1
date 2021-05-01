package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.LikeEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
	Optional<LikeEntity> findByProjectAndUser (ProjectEntity projectEntity, UserEntity user);
	Long countByProjectAndState(ProjectEntity projectEntity, Boolean State);
}
