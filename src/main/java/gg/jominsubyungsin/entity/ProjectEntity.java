package gg.jominsubyungsin.domain.entity;

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
  private List<UserEntity> users = new ArrayList<>();

  @OneToMany(mappedBy = "projectId")
  private List<FileEntity> files = new ArrayList<>();

  public void add(UserEntity user){
    users.add(user);
  }

  public void add(FileEntity fileEntity){
    files.add(fileEntity);
    fileEntity.setProjectId(this);
  }

  @Builder
  public ProjectEntity(Long id, String title, String content, List<UserEntity> users, List<FileEntity> files){
    this.id = id;
    this.title = title;
    this.content = content;
    for(UserEntity user:users){
      this.add(user);
      user.add(this);
    }
    for(FileEntity file:files){
      this.add(file);
    }
  }
}
