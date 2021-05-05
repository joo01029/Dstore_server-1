package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor
@Setter
@Getter
@Entity(name = "file")

public class FileEntity {
	@Id
	@Column(name = "file_id")
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;

	@Column(unique = true, name = "file_location")
	private String fileLocation;

	@Column(name = "file_type",nullable = false)
	private String type;

	@Column(name = "Thumnail")
	private Boolean thumnail;

	@JsonIgnore
	@ManyToOne
	@JoinColumn(nullable = false)
	private ProjectEntity projectId;

	@Builder
	public FileEntity(Long id, Boolean thumnail, String type, String fileLocation) {
		this.id = id;
		this.type = type;
		this.thumnail = thumnail;
		this.fileLocation = fileLocation;
	}
}
