package gg.jominsubyungsin.domain.entitiy;

import lombok.AccessLevel;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.java.Log;

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

  @ManyToOne
  @JoinColumn(name = "project_id")
  private ProjectEntity project;

}
