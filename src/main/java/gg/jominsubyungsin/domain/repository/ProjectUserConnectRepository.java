package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.ProjectUserConnectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.enums.Leader;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectUserConnectRepository extends JpaRepository<ProjectUserConnectEntity, Long> {
	List<ProjectUserConnectEntity> findByProject(ProjectEntity projectEntity);
	List<ProjectUserConnectEntity> findByUser(UserEntity userEntity, Pageable pageable);
	Optional<ProjectUserConnectEntity> findByProjectAndUserAndRole(ProjectEntity projectEntity, UserEntity userEntity, Leader role);
	Optional<ProjectUserConnectEntity> findByProjectAndUser(ProjectEntity projectEntity,UserEntity userEntity);
}
