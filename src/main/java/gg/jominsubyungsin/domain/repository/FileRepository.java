package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.FileEntity;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FileRepository extends JpaRepository<FileEntity, Long> {
}
