package gg.jominsubyungsin.service.jwt;

public interface JwtService {
  String createToken(String subject, long ttlMillis, boolean MakeTokenForRefresh);

  String getAccessTokenSubject(String token);

  String getRefreshTokenSubject(String token);
}
