package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.List;
import java.util.Optional;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
	Optional<ProjectEntity> findByIdAndOnDelete(Long id, Boolean onDelete);
	Long countByUsersAndOnDelete(UserEntity user, Boolean OnDelete);
	void deleteById(Long id);
	List<ProjectEntity> findByUsers(Long id);
	@Query(value = "select count(*)FROM project_tag_connect where tag in (:tags) and c.project in (select id from project where onDelete != 1) group by project having(count(tag) >= :length)", nativeQuery = true)
	Long countprojectTags(@Param("tags") List<Long> tag, @Param("length") Integer length);
	Long countByOnDelete(Boolean onDelete);
	List<ProjectEntity> findAllByIdIsInOrderByIdDesc(List<Long> id);

}
