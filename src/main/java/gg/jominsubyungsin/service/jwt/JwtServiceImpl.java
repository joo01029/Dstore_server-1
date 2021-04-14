package gg.jominsubyungsin.service.jwt;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.ExpiredJwtException;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import javax.crypto.spec.SecretKeySpec;
import javax.xml.bind.DatatypeConverter;
import java.security.Key;
import java.util.Date;

@Service
public class JwtServiceImpl implements JwtService{
  @Value("${auth.access}")
  private String ACCESSSECRET_KEY;
  @Value("${auth.refresh}")
  private String REFRESHSECRET_KEY;

  @Override
  public String createToken(String subject, long ttlMillis, boolean MakeTokenForRefresh){
    if(ttlMillis <= 0)
      throw new RuntimeException("Expiry time must be greater than Zero : ["+ttlMillis+"] ");

    try {
      Key signingKey = makeSigningKey(MakeTokenForRefresh);

      return encodingToken(subject, signingKey, ttlMillis);
    }catch (Exception e){
      throw e;
    }
  }

  public Key makeSigningKey(boolean MakeTokenForRefresh){
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    byte[] secretKey;

    try {
      if (MakeTokenForRefresh) {
        secretKey = DatatypeConverter.parseBase64Binary(REFRESHSECRET_KEY);
      } else {
        secretKey = DatatypeConverter.parseBase64Binary(ACCESSSECRET_KEY);
      }
      return new SecretKeySpec(secretKey, signatureAlgorithm.getJcaName());
    }catch (Exception e){
      throw e;
    }
  }

  public String encodingToken(String subject, Key secretKey,long ttlMillis){
    SignatureAlgorithm signatureAlgorithm = SignatureAlgorithm.HS256;
    try {
      return Jwts.builder()
              .setSubject(subject)
              .setIssuedAt(new Date(System.currentTimeMillis()))
              .setExpiration(new Date(System.currentTimeMillis() + ttlMillis))
              .signWith(secretKey, signatureAlgorithm)
              .compact();
    }catch (Exception e){
      throw e;
    }
  }

  @Override
  public String getAccessTokenSubject(String token) {
    Claims claims = decodingToken(token, ACCESSSECRET_KEY);
    return claims.getSubject();

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
    } catch (ExpiredJwtException e){
      System.out.println(e);
      return null;
    } catch (Exception e){
      throw e;
    }


  }

  @Override
  public String getRefreshTokenSubject(String token) {
    try {
      Claims claims = decodingToken(token, REFRESHSECRET_KEY);
      return claims.getSubject();
    }catch (Exception e){
      throw e;
    }
  }
}
