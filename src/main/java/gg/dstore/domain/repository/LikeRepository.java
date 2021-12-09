package gg.dstore.domain.repository;

import gg.dstore.domain.entity.LikeEntity;
import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface LikeRepository extends JpaRepository<LikeEntity,Long> {
	Optional<LikeEntity> findByProjectAndUser (ProjectEntity projectEntity, UserEntity user);
	Long countByProjectAndState(ProjectEntity projectEntity, Boolean State);
}
