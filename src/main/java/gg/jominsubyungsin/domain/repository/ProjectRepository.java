package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.TagEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
	Optional<ProjectEntity> findById(Long id);
	Long countByUsers(UserEntity user);
	List<ProjectEntity> findByUsers(Long id);
	@Query(value = "select count(*)FROM tag_project_connect where tag_id in (:tags) group by id having(count(tag_id) >= :length)", nativeQuery = true)
	Long countprojectTags(@Param("tags") List<Long> tag, @Param("length") Integer length);
	List<ProjectEntity> findAllByIdIsInOOrderByIdDesc(List<Long> id);
}
