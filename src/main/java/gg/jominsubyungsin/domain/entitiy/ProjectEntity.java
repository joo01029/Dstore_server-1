package gg.jominsubyungsin.domain.entitiy;

import lombok.*;

import javax.persistence.*;
import java.util.ArrayList;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Entity
@Setter
@Getter
@Table(name ="project")
public class ProjectEntity {
  @Id
  @Column(name="project_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column
  private String title;

  @Column
  private String content;

  @ManyToMany(mappedBy = "projects")
  private List<UserEntitiy> users = new ArrayList<>();

  @OneToMany(mappedBy = "project_id")
  private List<FileEntity> files = new ArrayList<>();

  public void add(UserEntitiy user){
    users.add(user);
  }

  public void add(FileEntity fileEntity){
    files.add(fileEntity);
  }

  @Builder
  public void ProjectEntity(Long id, String title, String content, List<UserEntitiy> users, List<FileEntity> files){
    this.id = id;
    this.title = title;
    this.content = content;
    for(UserEntitiy user:users){
      this.add(user);
    }
    for(FileEntity file:files){
      this.add(file);
    }
  }
}
