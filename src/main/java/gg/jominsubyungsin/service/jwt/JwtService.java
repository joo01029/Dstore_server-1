package gg.jominsubyungsin.service.jwt;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.enums.JwtAuth;

public interface JwtService {
  String createToken(String subject, long ttlMillis, JwtAuth authType);
  UserEntity accessTokenDecoding(String token);
  String refreshTokenDecoding(String token);
}
