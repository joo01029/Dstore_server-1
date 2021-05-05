package gg.jominsubyungsin.service.tag;

import gg.jominsubyungsin.domain.entity.TagEntity;

import java.util.List;

public interface TagService {
	List<TagEntity> createTag(List<String> tags);
}
