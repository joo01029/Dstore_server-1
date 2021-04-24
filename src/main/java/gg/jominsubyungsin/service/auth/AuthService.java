package gg.jominsubyungsin.service.auth;

import gg.jominsubyungsin.domain.dto.email.EmailDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;

public interface AuthService {
  void userCreate(UserDto userDto);
  UserEntity login(UserDto userDto);

  void sendMail(String email);

  void checkEmail(String email);

  Boolean authEmail(String code);
}
