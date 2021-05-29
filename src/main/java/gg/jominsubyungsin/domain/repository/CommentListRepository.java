package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.CommentEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface CommentListRepository extends PagingAndSortingRepository<CommentEntity,Long> {
	Page<CommentEntity> findByProjectAndOnDelete(ProjectEntity projectEntity, Boolean onDelete, Pageable pageable);
}
