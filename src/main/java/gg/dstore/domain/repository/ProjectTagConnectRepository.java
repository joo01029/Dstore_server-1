package gg.dstore.domain.repository;

import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.ProjectTagConnectEntity;
import gg.dstore.domain.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectTagConnectRepository extends JpaRepository<ProjectTagConnectEntity, Long> {
	List<ProjectTagConnectEntity> findByProject(ProjectEntity projectEntity);

	void removeByProjectAndTag(ProjectEntity projectEntity, TagEntity tagEntity);

	Optional<ProjectTagConnectEntity> findByProjectAndTag(ProjectEntity projectEntity, TagEntity tagEntity);
}
