package gg.jominsubyungsin.domain.dto.user;

import lombok.Getter;
import lombok.Setter;
<<<<<<< HEAD
import gg.jominsubyungsin.domain.entitiy.UserEntity;
=======
import gg.jominsubyungsin.domain.entity.UserEntity;
>>>>>>> 4d3a949bb1041a05d5ac86f234cce4a4be9c6e7e
import org.springframework.web.multipart.MultipartFile;

import javax.validation.constraints.Email;

@Getter @Setter
public class UserDto {
  private Long id;
  @Email
  private String email;
  private String password;
  private String name;
  private String introduce;
  private MultipartFile profileImage;

  public UserEntity toEntity(){
    return UserEntity.builder()
            .id(id)
            .email(email)
            .password(password)
            .name(name)
            .build();
  }
}
