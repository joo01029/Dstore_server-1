package gg.dstore.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "projectTagConnect")
public class ProjectTagConnectEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private ProjectEntity project;

	@ManyToOne
	@JoinColumn(nullable = false)
	private TagEntity tag;

	public ProjectTagConnectEntity(ProjectEntity projectEntity, TagEntity tagEntity){
		project = projectEntity;
		tag = tagEntity;
	}

}
