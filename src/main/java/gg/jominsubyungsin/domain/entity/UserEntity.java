package gg.jominsubyungsin.domain.entity;

import com.fasterxml.jackson.annotation.JsonIgnore;
import lombok.*;

import javax.persistence.*;
import javax.validation.constraints.Email;
import java.util.List;

@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Setter
@Getter
@Entity
@Table(name = "user")
public class UserEntity {
  @Id
  @Column(name="user_id")
  @GeneratedValue(strategy = GenerationType.IDENTITY)
  private Long id;

  @Column(unique = true)
  @Email
  private String email;

  @Column(nullable = false)
  private int mailAccess;

  @JsonIgnore
  @Column(nullable = false)
  private String password;

  @Column(length = 50, nullable = false)
  private String name;

  @Column
  private String introduce;

  @Column
  private String profileImage;

  @ManyToMany
  @JoinTable(name="user_project_connect",
            joinColumns = @JoinColumn(name = "user_id"),
            inverseJoinColumns = @JoinColumn(name = "project_id"))
  private List<ProjectEntity> projects;

  public void add(ProjectEntity project){
    projects.add(project);
  }

  @Builder
  public UserEntity(Long id, String email, String password, String name){
    this.id = id;
    this.email = email;
    this.password = password;
    this.name = name;
    this.mailAccess = 0;
    this.introduce = null;
    this.profileImage = null;
  }



}
