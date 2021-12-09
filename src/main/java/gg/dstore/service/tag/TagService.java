package gg.dstore.service.tag;

import gg.dstore.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.TagEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
	List<TagEntity> createTag(List<String> tags);
	List<String> TagList(String tag, Pageable pageable);
	List<SelectProjectDto> projectList(List<String> tags, UserEntity user, Pageable pageable);
	Long projectListCount(List<String> tags);

	void rmProjectTagConnect(String tag, ProjectEntity project);
}
