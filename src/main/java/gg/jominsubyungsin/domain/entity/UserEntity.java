package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gg.jominsubyungsin.enums.Role;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user")
public class UserEntity {
	@Id
	@Column(name = "user_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
	@Email
	private String email;

	@JsonIgnore
	@Column(nullable = false)
	private String password;

	@Column(length = 50, nullable = false)
	private String name;

	@Column
	private String introduce;

	@Column
	private String profileImage;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<ProjectUserConnectEntity> projects;

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<LikeEntity> likes = new ArrayList<>();

	@OneToMany(mappedBy = "user", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<CommentEntity> comments = new ArrayList<>();

	@OneToMany(mappedBy = "follower", cascade = {CascadeType.PERSIST, CascadeType.REMOVE})
	private List<FollowEntity> follower = new ArrayList<>();

	@OneToMany(mappedBy = "following", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<FollowEntity> following = new ArrayList<>();

	public void add(LikeEntity like){
		likes.add(like);
	}
	public void add(CommentEntity comment){
		comments.add(comment);
	}
	public void add(ProjectUserConnectEntity project){
		projects.add(project);
	}
	public void addFollower(FollowEntity follower){
		this.follower.add(follower);
		follower.setFollower(this);
	}

	public void addFollowing(FollowEntity following){
		this.following.add(following);
		following.setFollowing(this);
	}

	@Builder
	public UserEntity(Long id, String email, String password, String name) {
		this.id = id;
		this.email = email;
		this.password = password;
		this.name = name;
		this.introduce = null;
		this.profileImage = null;
		this.role = Role.USER;
	}


}
