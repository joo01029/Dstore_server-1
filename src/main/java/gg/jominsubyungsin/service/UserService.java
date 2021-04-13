package gg.jominsubyungsin.service;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import org.springframework.web.client.HttpServerErrorException;

public interface UserService {
  boolean userCreate(UserDto userDto);
  UserEntitiy login(UserDto userDto);
  boolean userUpdate(UserUpdateDto userDto) throws HttpServerErrorException;
  boolean userDelete(UserDto userDto);
  boolean userUpdateIntroduce(UserDto userDto);
  Boolean userMailAccess(String email);
}
