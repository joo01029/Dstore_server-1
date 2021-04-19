package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface ProjectListRepository extends PagingAndSortingRepository<ProjectEntity, Long> {
}
