package gg.jominsubyungsin.service.auth;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.EmailAuthEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.EmailAuthRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.lib.EmailSender;
import gg.jominsubyungsin.lib.Hash;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;

import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@Service
public class AuthServiceImpl implements AuthService{
  @Autowired
  private UserRepository userRepository;
  @Autowired
  private EmailAuthRepository emailAuthRepository;

  @Autowired
  Hash hash;
  @Autowired
  EmailSender emailSender;

  @Override
  @Transactional
  public void userCreate(UserDto userDto) {
    Optional<UserEntity> findUserByEmail;
    try {
      findUserByEmail = userRepository.findByEmail(userDto.getEmail());
    }catch (Exception e){
      throw e;
    }

    if(findUserByEmail.isPresent()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이미 유저가 존재함");
    }
    try {
      Optional<EmailAuthEntity> findAccess = emailAuthRepository.findByEmailAndAuth(userDto.getEmail(), true);
      if(findAccess.isEmpty()){
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "이메일 인증이 안됨");
      }
      UserEntity saveUser = userDto.toEntity();
      userRepository.save(saveUser);
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public UserEntity login(UserDto userDto) {
    String password = userDto.getPassword();
    String Email = userDto.getEmail();
    try {
      Optional<UserEntity> findUserByEmailAndPassword = userRepository.findByEmailAndPassword(Email, password);

      return findUserByEmailAndPassword.orElseGet(() -> {throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");});
    }catch (Exception e){
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public void checkEmail(String email){
    if(email == null || email.trim().isEmpty()){
      throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "메일 비었음");
    }
    Optional<UserEntity> user = userRepository.findByEmail(email);
    if(user.isPresent()){
      throw new ResponseStatusException(HttpStatus.CONFLICT, "중복된 이메일");
    }
  }

  @Override
  public Boolean authEmail(String code){
    if(code == null){
      return false;
    }
    Optional<EmailAuthEntity> emailAuth = emailAuthRepository.findByCode(code);
    if(emailAuth.isEmpty() || emailAuth.get().getExpireAt().getTime() < new Date().getTime()){
      return false;
    }

    emailAuth.get().setAuth(true);
    emailAuth.get().setCode(null);

    emailAuthRepository.save(emailAuth.get());
    return true;
  }

  @Value("${server.get.url}")
  String serverUrl;
  @Override
  @Transactional
  public void sendMail(String email) {
    try {
      checkEmail(email);
      EmailAuthEntity emailAuth = emailAuthRepository.findByEmail(email).orElse(new EmailAuthEntity());
      System.out.println(emailAuth.getEmail());
      Date expireAt = new Date();

      String code = hash.hashText(email+expireAt.toString());
      String href = serverUrl+"/email-auth?code="+code;

      String content = new StringBuffer("<h2>이메일 인증</h2>")
              .append("<p>디스토어 이메일 인증</p>")
              .append("<a href=\"").append(href)
              .append("\">")
              .append("<div style=\"border: solid 3px #000000; width: 30%; margin: 0 auto; font-size: 2rem; color: rgba(0,0,0);\">")
              .append("인증 하기")
              .append("</div>")
              .append("</a>").toString();
      emailSender.sendMail(email,"이메일 인증", content);
      expireAt.setTime(expireAt.getTime() + 1000*60*5);
      emailAuth.setAuth(false);
      emailAuth.setEmail(email);
      emailAuth.setCode(code);
      emailAuth.setExpireAt(expireAt);
      emailAuthRepository.save(emailAuth);
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }
  }
}
