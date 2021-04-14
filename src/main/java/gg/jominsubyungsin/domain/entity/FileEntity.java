package gg.jominsubyungsin.domain.entity;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

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

  @ManyToOne
  @JoinColumn
  private ProjectEntity projectId;

}
