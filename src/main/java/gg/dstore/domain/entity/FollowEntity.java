package gg.dstore.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "follow")
public class FollowEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long Id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private UserEntity follower;

	@ManyToOne
	@JoinColumn(nullable = false)
	private UserEntity following;

	@Column(nullable = false)
	private Boolean followState;

	public FollowEntity(UserEntity follower, UserEntity following, Boolean followState){
		this.follower = follower;
		this.following = following;
		this.followState = followState;
	}
}
