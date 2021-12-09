package gg.dstore.domain.repository;

import gg.dstore.domain.entity.FollowEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface FollowListRepository extends PagingAndSortingRepository<FollowEntity, Long> {
	Page<FollowEntity> findByFollowingAndFollowState(UserEntity following, Boolean followState, Pageable pageable);
	Page<FollowEntity> findByFollowerAndFollowState(UserEntity follower, Boolean followState, Pageable pageable);
}
