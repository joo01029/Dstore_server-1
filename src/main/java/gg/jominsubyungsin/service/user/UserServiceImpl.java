package gg.jominsubyungsin.service.user;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService{
  @Autowired
  private UserRepository userRepository;

  @Override
  @Transactional
  public boolean userCreate(UserDto userDto) {
    Optional<UserEntitiy> findUserByEmail;
    try {
      findUserByEmail = userRepository.findByEmail(userDto.getEmail());
    }catch (Exception e){
      throw e;
    }

    if(findUserByEmail.isPresent()){
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 유저가 존재함");
    }
    try {
      UserEntitiy saveUser = userDto.toEntity();
      userRepository.save(saveUser);
      return true;
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public UserEntitiy login(UserDto userDto) {
    String password = userDto.getPassword();
    String Email = userDto.getEmail();
    try {
      Optional<UserEntitiy> findUserByEmailAndPassword = userRepository.findByEmailAndPassword(Email, password);


      if(findUserByEmailAndPassword.isPresent() && 0 == findUserByEmailAndPassword.get().getMailAccess()){
        throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이메일 인증 안됨");
      }

      return findUserByEmailAndPassword.orElseGet(() -> {throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");});
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public boolean userUpdate(UserUpdateDto userUpdateDto) throws HttpServerErrorException {
    try {
      return userRepository.findByEmailAndPassword(userUpdateDto.getEmail(), userUpdateDto.getPassword())
              .map(found -> {
                found.setPassword(Optional.ofNullable(userUpdateDto.getChangePassword()).orElse(found.getPassword()));
                found.setName(Optional.ofNullable(userUpdateDto.getChangeName()).orElse(found.getName()));
                userRepository.save(found);

                return true;
              }).orElse(false);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

  }

  @Override
  @Transactional
  public boolean userUpdateIntroduce(UserDto userDto) {
    try {
      return userRepository.findByEmail(userDto.getEmail())
              .map(found -> {
                found.setIntroduce(userDto.getIntroduce());
                userRepository.save(found);

                return true;
              }).orElse(false);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }

  }

  @Override
  @Transactional
  public boolean userDelete(UserDto userDto) {
    Optional<UserEntitiy> findUser;
    try {
      findUser = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());
    }catch (Exception e){
      throw e;
    }
    if(findUser.isEmpty()){
      throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
    }

    try{
      userRepository.deleteByEmail(userDto.getEmail());
      return true;
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  @Transactional
  public Boolean userMailAccess(String email){
    try{
      return userRepository.findByEmail(email)
              .map(found->{
                found.setMailAccess(1);

                return true;
              }).orElse(false);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }
}
