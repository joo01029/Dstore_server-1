package gg.dstore.domain.entity;

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

	@OneToMany(mappedBy = "tag")
	private List<ProjectTagConnectEntity> projects = new ArrayList<>();

	public void add(ProjectTagConnectEntity projectEntity){
		projects.add(projectEntity);
	}

	public TagEntity(String tag){
		this.tag = tag;
	}

}
