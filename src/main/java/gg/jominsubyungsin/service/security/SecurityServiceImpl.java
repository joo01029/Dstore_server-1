package gg.jominsubyungsin.service.security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.math.BigInteger;
import java.security.Key;
import java.security.MessageDigest;
import java.security.NoSuchAlgorithmException;
import java.util.Date;

@Service
public class SecurityServiceImpl implements SecurityService{
  @Override
  public String hashPassword(String password) {
    try {
      MessageDigest md = MessageDigest.getInstance("SHA-512");
      md.update(password.getBytes(), 0, password.getBytes().length);
      return new BigInteger(1, md.digest()).toString(16);
    } catch (NoSuchAlgorithmException e) {
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "비밀번호 암호화 실패");
    }
  }


}
