package gg.dstore.domain.repository;

import gg.dstore.domain.entity.FollowEntity;
import gg.dstore.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity,Long> {
	Optional<FollowEntity> findByFollowerAndFollowingAndFollowState(UserEntity follower, UserEntity following,Boolean followState);

	Long countByFollowerAndFollowState(UserEntity follower, Boolean followState);
	Long countByFollowingAndFollowState(UserEntity following, Boolean followState);
}
