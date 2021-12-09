package gg.dstore.domain.repository;

import gg.dstore.domain.entity.FileEntity;
import gg.dstore.domain.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
	Optional<FileEntity> findByIdAndProjectId(Long id, ProjectEntity projectEntity);
}
