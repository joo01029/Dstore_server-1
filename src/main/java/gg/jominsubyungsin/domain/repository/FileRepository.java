package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.FileEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
	Optional<FileEntity> findByIdAndProjectId(Long id, ProjectEntity projectEntity);
}
