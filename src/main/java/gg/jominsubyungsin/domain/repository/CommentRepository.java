package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
	Long countByProjectAndOnDelete(ProjectEntity prjectm, Boolean onDelete);
	Optional<CommentEntity> findByIdAndUser(Long id, UserEntity user);
}
