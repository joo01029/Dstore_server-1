package gg.jominsubyungsin.domain.repository;

import gg.jominsubyungsin.domain.entity.FollowEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface FollowRepository extends JpaRepository<FollowEntity,Long> {
	Optional<FollowEntity> findByFollowerAndFollowingAndFollowState(UserEntity follower, UserEntity following,Boolean followState);

	Long countByFollowerAndFollowState(UserEntity follower, Boolean followState);
	Long countByFollowingAndFollowState(UserEntity following, Boolean followState);
}
