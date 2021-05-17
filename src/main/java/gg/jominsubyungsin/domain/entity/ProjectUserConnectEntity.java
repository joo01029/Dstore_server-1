package gg.jominsubyungsin.domain.entity;

import gg.jominsubyungsin.enums.Leader;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projectUserConnect")
public class ProjectUserConnectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false, name = "project")
	private ProjectEntity project;

	@ManyToOne
	@JoinColumn(nullable = false, name = "user")
	private UserEntity user;

	@Column
	@Enumerated(EnumType.STRING)
	private Leader role;

	public ProjectUserConnectEntity(ProjectEntity project, UserEntity user, Leader role){
		this.project = project;
		this.user = user;
		this.role = role;
	}
}
