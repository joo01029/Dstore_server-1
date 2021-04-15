package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {

//  @Query("select * from ProjectEntity as p where p.id not in :beforeIds order by p.id desc limit 5")
//  List<ProjectEntity> findTop5(@Param("beforeIds")List<Long> beforeIds);
}
