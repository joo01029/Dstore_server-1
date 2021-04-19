package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.ProjectEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.awt.print.Pageable;
import java.util.List;

public interface ProjectRepository extends JpaRepository<ProjectEntity, Long> {
}
