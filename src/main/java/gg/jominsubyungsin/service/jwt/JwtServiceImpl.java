package gg.jominsubyungsin.service.jwt;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.enums.JwtAuth;
import io.jsonwebtoken.*;
import io.jsonwebtoken.security.SignatureException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpServerErrorException;
import org.springframework.web.server.ResponseStatusException;

import javax.crypto.spec.SecretKeySpec;
import javax.swing.*;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;
import java.util.HashMap;
import java.util.Map;

@Service
public class JwtServiceImpl implements JwtService{
  @Autowired
  UserRepository userRepository;

  @Value("${auth.access}")
  private String ACCESSSECRET_KEY;
  @Value("${auth.refresh}")
  private String REFRESHSECRET_KEY;

  @Override
  public String createToken(String subject, long ttlMillis, JwtAuth authType){
    if(ttlMillis <= 0)
      throw new RuntimeException("Expiry time must be greater than Zero : ["+ttlMillis+"] ");

    try {
      Key signingKey = makeSigningKey(authType);

      return encodingToken(subject, signingKey, ttlMillis, authType);
    }catch (Exception e){
      e.printStackTrace();
      throw e;
    }
  }

  public Key makeSigningKey(JwtAuth authType){
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] secretKey;

    try {
      if (authType == JwtAuth.REFRESH) {
        secretKey = DatatypeConverter.parseBase64Binary(REFRESHSECRET_KEY);
      } else {
        secretKey = DatatypeConverter.parseBase64Binary(ACCESSSECRET_KEY);
      }
      return new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());
    }catch (Exception e){
      e.printStackTrace();
      throw e;
    }
  }

  public String encodingToken(String subject, Key secretKey,long ttlMillis, JwtAuth authType){
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;

    try {
      return Jwts.builder()
              .setSubject(subject)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
              .signWith(secretKey, signatureAlgorithm)
              .compact();
    }catch (Exception e){
      e.printStackTrace();
      throw e;
    }
  }

  @Override
  public UserEntity accessTokenDecoding(String token) {
    try {
      Claims claims = decodingToken(token, ACCESSSECRET_KEY);
      System.out.println(claims.getSubject());
      return userRepository.findByEmail(claims.getSubject()).orElseGet(() -> {
        throw new ResponseStatusException(HttpStatus.BAD_REQUEST, "존재하지 않는 유저");
      });

    }catch(Exception e){
      throw e;
    }

  }

  public Claims decodingToken(String token, String key){
    Claims claims;
    try{
      claims = Jwts.parserBuilder()
              .setSigningKey(DatatypeConverter.parseBase64Binary(key))
              .build()
              .parseClaimsJws(token)
              .getBody();

      return claims;
    }catch (ExpiredJwtException e){
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.GONE,"토큰 만료");
    }catch (SignatureException | MalformedJwtException e){
      e.printStackTrace();
      throw new ResponseStatusException(HttpStatus.UNAUTHORIZED, "토큰 위조");
    }catch (Exception e){
      e.printStackTrace();
      throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
    }


  }

  @Override
  public String refreshTokenDecoding(String token) {
    try {
      Claims claims = decodingToken(token, REFRESHSECRET_KEY);
      return claims.getSubject();
    }catch (Exception e){
      throw e;
    }
  }
}
