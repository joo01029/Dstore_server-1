package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
	Optional<ProjectEntity> findById(Long id);
	Long countByUsers(UserEntity user);
	void deleteById(Long id);
	List<ProjectEntity> findByUsers(Long id);
	@Query(value = "select count(*)FROM project_tag_connect where tag in (:tags) group by project having(count(tag) >= :length)", nativeQuery = true)
	Long countprojectTags(@Param("tags") List<Long> tag, @Param("length") Integer length);
	List<ProjectEntity> findAllByIdIsInOrderByIdDesc(List<Long> id);
}
