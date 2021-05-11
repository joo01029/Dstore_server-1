package gg.jominsubyungsin.service.tag;

import gg.jominsubyungsin.domain.dto.project.dataIgnore.SelectProjectDto;
import gg.jominsubyungsin.domain.entity.TagEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface TagService {
	List<TagEntity> createTag(List<String> tags);
	List<String> TagList(String tag, Pageable pageable);
	List<SelectProjectDto> projectList(List<String> tags, UserEntity user, Pageable pageable);
	Long projectListCount(List<String> tags);
}
