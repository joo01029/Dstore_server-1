package gg.jominsubyungsin.service.like;

import gg.jominsubyungsin.domain.dto.user.dataIgnore.SelectUserDto;
import gg.jominsubyungsin.domain.entity.LikeEntity;
import gg.jominsubyungsin.domain.entity.ProjectEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeService {
	List<SelectUserDto> getUserList(Long id, Pageable pageable, UserEntity me);

	void changeLikeState(Long id, UserEntity user);

	Long LikeNum(Long id);

	LikeEntity getLikeState(ProjectEntity project, UserEntity user);

	void setLikeFalse(LikeEntity like);
}
