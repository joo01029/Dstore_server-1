package gg.jominsubyungsin.domain.entity;

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
	@JoinColumn
	private UserEntity follower;

	@ManyToOne
	@JoinColumn
	private UserEntity following;

	@Column
	private Boolean followState;

	public FollowEntity(UserEntity follower, UserEntity following, Boolean followState){
		this.follower = follower;
		this.following = following;
		this.followState = followState;
	}
}
