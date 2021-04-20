package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Entity

public class FileEntity {
  @Id
  @Column(name="file_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true, name="file_location")
  private String fileLocation;

  @Column(name="file_type")
  private String type;

  @Column(name="Thumnail")
  private Integer thumnail;

  @JsonIgnore
  @ManyToOne
  @JoinColumn
  private ProjectEntity projectId;

  @Builder
  public FileEntity(Long id, Integer thumnail, String type, String fileLocation){
    this.id = id;
    this.type = type;
    this.thumnail = thumnail;
    this.fileLocation = fileLocation;
  }
}
