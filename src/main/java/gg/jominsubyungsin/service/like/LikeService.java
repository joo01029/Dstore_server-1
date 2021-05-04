package gg.jominsubyungsin.service.like;

import gg.jominsubyungsin.domain.dto.like.dataIgnore.SelectLikeDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeService {
	List<SelectLikeDto> getUserList(Long id, Pageable pageable, UserEntity me);
	void changeLikeState(Long id, UserEntity user);
	Long LikeNum(Long id);
}
