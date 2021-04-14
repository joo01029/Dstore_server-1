package gg.jominsubyungsin.service.user;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
<<<<<<< HEAD
import gg.jominsubyungsin.domain.entitiy.UserEntity;
=======
import gg.jominsubyungsin.domain.entity.UserEntity;
>>>>>>> 4d3a949bb1041a05d5ac86f234cce4a4be9c6e7e
import org.springframework.web.client.HttpServerErrorException;

public interface UserService {
  boolean userCreate(UserDto userDto);
  UserEntity login(UserDto userDto);
  boolean userUpdate(UserUpdateDto userDto) throws HttpServerErrorException;
  boolean userDelete(UserDto userDto);
  boolean userUpdateIntroduce(UserDto userDto);
  Boolean userMailAccess(String email);
}
