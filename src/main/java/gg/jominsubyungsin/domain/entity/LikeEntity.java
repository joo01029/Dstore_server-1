package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name = "ProjectLike")
public class LikeEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	Long id;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(nullable = false)
	ProjectEntity project;

	@ManyToOne
	@JoinColumn(nullable = false)
	UserEntity user;

	@Column(nullable = false)
	Boolean state;

	public LikeEntity (ProjectEntity project, UserEntity user, Boolean state){
		this.project = project;
		this.user = user;
		this.state = state;
	}
}
