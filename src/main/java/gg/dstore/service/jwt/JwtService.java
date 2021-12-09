package gg.dstore.service.jwt;

import gg.dstore.domain.entity.UserEntity;
import gg.dstore.enums.JwtAuth;

public interface JwtService {
	String createToken(String subject, long ttlMillis, JwtAuth authType);

	UserEntity accessTokenDecoding(String token);

	String refreshTokenDecoding(String token);
}
