package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.FollowEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.util.List;

public interface FollowListRepository extends PagingAndSortingRepository<FollowEntity, Long> {
	Page<FollowEntity> findByFollowingAndFollowState(UserEntity following, Boolean followState, Pageable pageable);
	Page<FollowEntity> findByFollowerAndFollowState(UserEntity follower, Boolean followState, Pageable pageable);
}
