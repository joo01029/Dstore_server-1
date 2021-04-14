package gg.jominsubyungsin.service.user;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.web.client.HttpServerErrorException;

public interface UserService {
  boolean userCreate(UserDto userDto);
  UserEntity login(UserDto userDto);
  boolean userUpdate(UserUpdateDto userDto) throws HttpServerErrorException;
  boolean userDelete(UserDto userDto);
  boolean userUpdateIntroduce(UserDto userDto);
  Boolean userMailAccess(String email);
}
