package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@NoArgsConstructor
@Getter
@Setter
@Entity
@Table(name="comment")
public class CommentEntity {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(nullable = false)
	private String comment;

	@Column(nullable = false)
	private Date createAt = new Date();

	@JsonIgnore
	@ManyToOne
	@JoinColumn(nullable = false)
	private ProjectEntity project;

	@ManyToOne
	@JoinColumn(nullable = false)
	private UserEntity user;

	public CommentEntity(String comment,ProjectEntity project, UserEntity user){
		this.comment = comment;
		this.project = project;
		this.user = user;
	}
}
