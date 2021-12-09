package gg.dstore.domain.repository;

import gg.dstore.domain.entity.TagEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface TagRepository extends JpaRepository<TagEntity, Long> {
	Optional<TagEntity> findByTag(String tag);
}
