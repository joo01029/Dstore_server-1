package gg.jominsubyungsin.domain.entity;

import lombok.*;
import javax.persistence.*;
import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "project")
public class ProjectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column
	private String title;

	@Column
	private String content;

	@Column
	private Date createAt = new Date();

	@OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<ProjectUserConnectEntity> users = new ArrayList<>();

	@OneToMany(mappedBy = "projectId", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<FileEntity> files = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<LikeEntity> likes = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<CommentEntity> comments = new ArrayList<>();

	@OneToMany(mappedBy = "project", cascade = {CascadeType.PERSIST,CascadeType.REMOVE})
	private List<ProjectTagConnectEntity> tags = new ArrayList<>();

	public void addUsers(ProjectUserConnectEntity projectUserConnectEntity) {
		this.users.add(projectUserConnectEntity);
	}

	public void add(FileEntity fileEntity) {
		files.add(fileEntity);
		fileEntity.setProjectId(this);
	}

	public void add(LikeEntity like) {
		likes.add(like);
		like.setProject(this);
	}
	public void add(CommentEntity comment) {
		comments.add(comment);
		comment.setProject(this);
	}

	public void addTags(ProjectTagConnectEntity projectTagConnectEntity){
		this.tags.add(projectTagConnectEntity);
	}

	@Builder
	public ProjectEntity(Long id, String title, String content, List<FileEntity> files) {
		this.id = id;
		this.title = title;
		this.content = content;
		//addUsers(users);
		for (FileEntity file : files) {
			this.add(file);
		}
		//(tags);
	}
}
