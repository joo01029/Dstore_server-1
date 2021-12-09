package gg.dstore.service.like;

import gg.dstore.domain.dto.user.dataIgnore.SelectUserDto;
import gg.dstore.domain.entity.LikeEntity;
import gg.dstore.domain.entity.ProjectEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface LikeService {
	List<SelectUserDto> getUserList(Long id, Pageable pageable, UserEntity me);

	void changeLikeState(Long id, UserEntity user);

	Long LikeNum(Long id);

	LikeEntity getLikeState(ProjectEntity project, UserEntity user);

	void setLikeFalse(LikeEntity like);
}
