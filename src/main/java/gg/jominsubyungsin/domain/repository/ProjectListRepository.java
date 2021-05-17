package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.PagingAndSortingRepository;
import org.springframework.data.repository.query.Param;

import java.util.List;

public interface ProjectListRepository extends PagingAndSortingRepository<ProjectEntity, Long> {
	Page<ProjectEntity> findByUsers(UserEntity user, Pageable pageable);
	@Query(
			value = "select project FROM project_tag_connect where tag in (:tags) group by project having(count(tag) >= :length)",
			countQuery = "select count (*) from project",
			nativeQuery = true

			)
	List<Long> findProjectTags(Pageable pageable, @Param("tags") List<Long> tags, @Param("length") int length);

}

//select p.*, t.* FROM jmsbs.project as p join jmsbs.tag_project_connect as pt on p.id = pt.project_id join jmsbs.tag as t on t.id = pt.tag_id where pt.tag_id in (:tags)group by pt.project_id, p.id having(count(pt.tag_id) = :length)
//-- #pageable