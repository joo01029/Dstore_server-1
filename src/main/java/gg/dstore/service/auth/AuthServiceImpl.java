package gg.dstore.service.auth;

import gg.dstore.domain.dto.token.LoginJwtDto;
import gg.dstore.domain.dto.user.request.LoginDto;
import gg.dstore.domain.dto.user.request.UserDto;
import gg.dstore.domain.entity.EmailAuthEntity;
import gg.dstore.domain.entity.UserEntity;
import gg.dstore.domain.repository.EmailAuthRepository;
import gg.dstore.domain.repository.UserRepository;
import gg.dstore.enums.JwtAuth;
import gg.dstore.lib.EmailSender;
import gg.dstore.lib.Hash;
import gg.dstore.lib.Log;
import gg.dstore.service.jwt.JwtService;
import lombok.RequiredArgsConstructor;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import java.util.Date;
import java.util.Optional;


@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	private final JwtService jwtService;
	private final UserRepository userRepository;
	private final EmailAuthRepository emailAuthRepository;
	private final Log log;
	private final Hash hash;
	private final EmailSender emailSender;

	@Override
	@Transactional
	public void userCreate(UserDto userDto) {
		try {
			String hashPassword = hash.hashText(userDto.getPassword());
			userDto.setPassword(hashPassword);

			Optional<UserEntity> User = userRepository.findByEmail(userDto.getEmail());
			if (User.isPresent()) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 유저가 존재함");
			}

			//이메일 인증여부
			Optional<EmailAuthEntity> EmailAccessed = emailAuthRepository.findByEmailAndAuth(userDto.getEmail(), true);
			if (EmailAccessed.isEmpty()) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이메일 인증이 안됨");
			}

			UserEntity saveUser = userDto.toEntity();
			userRepository.save(saveUser);
		} catch (Exception e) {
			log.error("user create error");
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public LoginJwtDto login(LoginDto userDto) {
		//비밀번호 암호화
		String hashPassword = hash.hashText(userDto.getPassword());
		String email = userDto.getEmail();

		try {
			UserEntity user = userRepository.findByEmailAndPasswordAndOnDelete(email, hashPassword, false).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});

			return makeTokens(user.getEmail());
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	public LoginJwtDto makeTokens(String subject) {
		//token발행
		try {
			long accessExpiredTime =   30* 24 * 60 * 60 * 1000L;
			String accessToken = jwtService.createToken(subject, accessExpiredTime, JwtAuth.ACCESS);

			long refreshExpiredTime = 7 * 24 * 60 * 60 * 1000L;
			String refreshToken = jwtService.createToken(subject, refreshExpiredTime, JwtAuth.REFRESH);

			Date accessTokenTime = new Date(System.currentTimeMillis() + accessExpiredTime);
			Date refreshTokenTime = new Date(System.currentTimeMillis() + refreshExpiredTime);

			LoginJwtDto tokens = new LoginJwtDto();
			tokens.setAccessToken(accessToken);
			tokens.setAccessExpiredTime(accessTokenTime.getTime());
			tokens.setRefreshToken(refreshToken);
			tokens.setRefreshExpiredTime(refreshTokenTime.getTime());
			return tokens;
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional(readOnly = true)
	public void checkEmail(String email) {
		try {
			if (email == null || email.trim().isEmpty()) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "메일 비었음");
			}

			Optional<UserEntity> user = userRepository.findByEmail(email);
			if (user.isPresent()) {
				throw new HttpClientErrorException(HttpStatus.CONFLICT, "중복된 이메일");
			}
		} catch (Exception e) {
			throw e;
		}
	}

	@Override
	@Transactional
	public Boolean authEmail(String code) {
		try {
			if (code == null)
				return false;

			Optional<EmailAuthEntity> emailAuth = emailAuthRepository.findByCode(code);

			if (emailAuth.isEmpty() || emailAuth.get().getExpireAt().getTime() < new Date().getTime())
				return false;

			emailAuth.get().setAuth(true);
			emailAuth.get().setCode(null);

			emailAuthRepository.save(emailAuth.get());
			return true;
		} catch (Exception e) {
			throw e;
		}
	}

	/*
	이메일 보내기
	 */
	@Value("${server.get.url}")
	String serverUrl;

	@Override
	@Transactional
	public void sendMail(String email) {
		try {
			checkEmail(email);
			EmailAuthEntity emailAuth = emailAuthRepository.findByEmail(email).orElse(new EmailAuthEntity());
			Date expireAt = new Date();

			String code = hash.hashText(email + expireAt.toString());
			String href = "Http://"+serverUrl + "/email-auth?code=" + code;

			String content = new StringBuffer("<h2>이메일 인증</h2>")
					.append("<p>디스토어 이메일 인증</p>")
					.append("<a href=\"").append(href)
					.append("\">")
					.append("<div style=\"border: solid 3px #000000; width: 30%; margin: 0 auto; font-size: 2rem; color: rgba(0,0,0);\">")
					.append("인증 하기")
					.append("</div>")
					.append("</a>").toString();


			emailSender.sendMail(email, "이메일 인증", content);

			expireAt.setTime(expireAt.getTime() + 1000 * 60 * 5);
			emailAuth.setAuth(false);
			emailAuth.setEmail(email);
			emailAuth.setCode(code);
			emailAuth.setExpireAt(expireAt);
			emailAuthRepository.save(emailAuth);
		} catch (Exception e) {
			log.error("send email error");
			throw e;
		}
	}
}
