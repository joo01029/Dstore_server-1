package gg.jominsubyungsin.domain.dto.user;

import lombok.Getter;
import lombok.Setter;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
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

  public UserEntitiy toEntity(){
    return UserEntitiy.builder()
            .id(id)
            .email(email)
            .password(password)
            .name(name)
            .build();
  }
}
