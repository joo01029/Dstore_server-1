package gg.jominsubyungsin.domain.dto.tag.dataIgnore;

import gg.jominsubyungsin.domain.entity.TagEntity;
import lombok.Getter;
import lombok.Setter;

@Getter
@Setter
public class tagDto {
	private Long id;
	private String tag;

	public tagDto(TagEntity tagEntity){
		id = tagEntity.getId();
		tag = tagEntity.getTag();
	}
}
