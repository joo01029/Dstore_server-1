package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import gg.jominsubyungsin.enums.Role;
import lombok.*;
import org.hibernate.annotations.Fetch;
import org.hibernate.annotations.FetchMode;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Setter
@Getter
@Entity
@Table(name = "user")
public class UserEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true)
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

	@Column
	private Boolean onDelete = false;

	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<ProjectUserConnectEntity> projects = new ArrayList<>();

	@Enumerated(EnumType.STRING)
	@Column(nullable = false)
	private Role role;

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<LikeEntity> likes = new ArrayList<>();

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<CommentEntity> comments = new ArrayList<>();

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "follower", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<FollowEntity> follower = new ArrayList<>();

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "following", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<FollowEntity> following = new ArrayList<>();

	@Fetch(FetchMode.SELECT)
	@OneToMany(mappedBy = "user", cascade = CascadeType.REMOVE, fetch = FetchType.EAGER)
	private List<ReportEntity> reports = new ArrayList<>();

	public void add(LikeEntity like) {
		likes.add(like);
	}

	public void add(CommentEntity comment) {
		comment.setUser(this);
		this.comments.add(comment);
	}

	public void add(ProjectUserConnectEntity project) {
		projects.add(project);
	}

	public void addFollower(FollowEntity follower) {
		this.follower.add(follower);
		follower.setFollower(this);
	}

	public void addFollowing(FollowEntity following) {
		this.following.add(following);
		following.setFollowing(this);
	}

	public void addReport(ReportEntity report) {
		reports.add(report);
		report.setUser(this);
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
