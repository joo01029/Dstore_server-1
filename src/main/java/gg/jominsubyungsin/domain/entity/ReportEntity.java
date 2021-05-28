package gg.jominsubyungsin.domain.entity;

import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import javax.persistence.*;
import java.util.Date;

@Entity
@Getter
@Setter
@NoArgsConstructor
@Table(name = "report")
public class ReportEntity {
	@Id
	@GeneratedValue
	private Long id;

	@ManyToOne
	@JoinColumn(nullable = false)
	private ProjectEntity project;

	@ManyToOne
	@JoinColumn(nullable = false)
	private UserEntity user;

	@Column(nullable = false)
	private Date reportedAt = new Date();
}
