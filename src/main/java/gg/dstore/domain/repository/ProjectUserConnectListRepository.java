package gg.dstore.domain.repository;

import gg.dstore.domain.entity.ProjectUserConnectEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectUserConnectListRepository extends PagingAndSortingRepository<ProjectUserConnectEntity, Long> {
	Page<ProjectUserConnectRepository> findByUser(UserEntity userEntity, Pageable pageable);
}
