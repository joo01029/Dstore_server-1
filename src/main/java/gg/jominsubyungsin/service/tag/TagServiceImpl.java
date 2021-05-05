package gg.jominsubyungsin.service.tag;

import gg.jominsubyungsin.domain.entity.TagEntity;
import gg.jominsubyungsin.domain.repository.TagRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.persistence.OneToMany;
import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@Service
public class TagServiceImpl implements TagService{
	@Autowired
	TagRepository tagRepository;

	@Override
	@Transactional
	public List<TagEntity> createTag(List<String> tags){
		try {
			List<TagEntity> tagEntities = new ArrayList<>();
			for (String tag : tags) {
				TagEntity tagEntity = tagRepository.findByTag(tag).orElse(
						new TagEntity(tag)
				);
				tagEntities.add(tagEntity);
			}

			return tagEntities;
		}catch (Exception e){
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
