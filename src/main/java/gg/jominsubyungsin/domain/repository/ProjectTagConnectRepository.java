package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.ProjectTagConnectEntity;
import gg.jominsubyungsin.domain.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTagConnectRepository extends JpaRepository<ProjectTagConnectEntity, Long> {
	List<ProjectTagConnectEntity> findByProject(ProjectEntity projectEntity);

	void removeByProjectAndTag(ProjectEntity projectEntity, TagEntity tagEntity);

	Optional<ProjectTagConnectEntity> findByProjectAndTag(ProjectEntity projectEntity, TagEntity tagEntity);
}
