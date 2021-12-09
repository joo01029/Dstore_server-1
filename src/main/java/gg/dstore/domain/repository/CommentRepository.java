package gg.dstore.domain.repository;

import gg.dstore.domain.entity.CommentEntity;
import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
	Long countByProjectAndOnDelete(ProjectEntity prjectm, Boolean onDelete);
	Optional<CommentEntity> findByIdAndUser(Long id, UserEntity user);
}
