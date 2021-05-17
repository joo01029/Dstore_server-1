package gg.jominsubyungsin.service.auth;

import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.entity.EmailAuthEntity;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.EmailAuthRepository;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.lib.EmailSender;
import gg.jominsubyungsin.lib.Hash;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.Date;
import java.util.Optional;

@RequiredArgsConstructor
@Service
public class AuthServiceImpl implements AuthService {
	private final UserRepository userRepository;
	private final EmailAuthRepository emailAuthRepository;

	private final Hash hash;
	private final EmailSender emailSender;

	@Override
	@Transactional
	public void userCreate(UserDto userDto) {
		try {
			Optional<UserEntity> findUserByEmail = userRepository.findByEmail(userDto.getEmail());
			if (findUserByEmail.isPresent()) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이미 유저가 존재함");
			}
			//이메일 인증여부
			Optional<EmailAuthEntity> findAccess = emailAuthRepository.findByEmailAndAuth(userDto.getEmail(), true);
			if (findAccess.isEmpty()) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "이메일 인증이 안됨");
			}

			UserEntity saveUser = userDto.toEntity();
			userRepository.save(saveUser);
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public UserEntity login(UserDto userDto) {
		String password = userDto.getPassword();
		String Email = userDto.getEmail();
		try {
			Optional<UserEntity> findUserByEmailAndPassword = userRepository.findByEmailAndPassword(Email, password);

			return findUserByEmailAndPassword.orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public void checkEmail(String email) {
		try {
			if (email == null || email.trim().isEmpty()) {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "메일 비었음");
			}
			Optional<UserEntity> user = userRepository.findByEmail(email);
			if (user.isPresent()) {
				throw new HttpClientErrorException(HttpStatus.CONFLICT, "중복된 이메일");
			}
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
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
		} catch (HttpClientErrorException e) {
			throw e;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
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
			String href = serverUrl + "/email-auth?code=" + code;

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
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
