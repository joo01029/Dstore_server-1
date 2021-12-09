package gg.dstore.domain.repository;

import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.ReportEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ReportRepository extends JpaRepository<ReportEntity, Long> {
	List<ReportEntity> findByUser (UserEntity user, Pageable pageable);
	List<ReportEntity> findByProject (ProjectEntity projectEntity, Pageable pageable);
}
