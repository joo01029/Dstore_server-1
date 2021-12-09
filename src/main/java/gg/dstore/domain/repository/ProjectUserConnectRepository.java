package gg.dstore.domain.repository;

import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.ProjectUserConnectEntity;
import gg.dstore.domain.entity.UserEntity;
import gg.dstore.enums.Leader;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface ProjectUserConnectRepository extends JpaRepository<ProjectUserConnectEntity, Long> {
	List<ProjectUserConnectEntity> findByProject(ProjectEntity projectEntity);
	List<ProjectUserConnectEntity> findByUserAndGetOut(UserEntity userEntity,Boolean getOut);
	Optional<ProjectUserConnectEntity> findByProjectAndUserAndRole(ProjectEntity projectEntity, UserEntity userEntity, Leader role);
	Optional<ProjectUserConnectEntity> findByProjectAndUser(ProjectEntity projectEntity,UserEntity userEntity);
	Long countByUserAndGetOut(UserEntity userEntity, Boolean getOut);

}
