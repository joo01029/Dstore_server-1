package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface CommentRepository extends JpaRepository<CommentEntity,Long> {
	Long countByProject(ProjectEntity prject);
}
