package gg.jominsubyungsin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "tag")
public class TagEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, nullable = false)
	private String tag;

	@ManyToMany
	@JoinTable(name = "tag_project_connect",
			joinColumns = @JoinColumn(name = "tag_Id"),
			inverseJoinColumns = @JoinColumn(name = "id"))
	private List<ProjectEntity> projects = new ArrayList<>();

	public void add(ProjectEntity projectEntity){
		projects.add(projectEntity);
	}

	public TagEntity(String tag){
		this.tag = tag;
	}

}
