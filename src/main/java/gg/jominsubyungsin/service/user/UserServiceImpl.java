package gg.jominsubyungsin.service.user;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.dto.user.UserUpdateDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.dto.query.SelectUserDto;
import gg.jominsubyungsin.domain.repository.UserRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;
import org.springframework.web.client.HttpServerErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

@Service
public class UserServiceImpl implements UserService {
	@Autowired
	private UserRepository userRepository;

	@Override
	public boolean userUpdate(UserUpdateDto userUpdateDto) throws HttpServerErrorException {
		try {
			String changePassword = userUpdateDto.getChangePassword();
			String changeName = userUpdateDto.getChangeName();

			return userRepository.findByEmailAndPassword(userUpdateDto.getEmail(), userUpdateDto.getPassword())
					.map(found -> {
						found.setPassword(Optional.ofNullable(changePassword).orElse(found.getPassword()));
						found.setName(Optional.ofNullable(changeName).orElse(found.getName()));
						userRepository.save(found);
						return true;
					}).orElse(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public boolean userUpdateIntroduce(UserDto userDto) {
		String introduce = userDto.getIntroduce();
		try {
			return userRepository.findByEmail(userDto.getEmail())
					.map(found -> {
						found.setIntroduce(introduce);
						userRepository.save(found);
						return true;
					}).orElse(false);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public boolean userDelete(UserDto userDto) {
		try {
			Optional<UserEntity> findUser = userRepository.findByEmailAndPassword(userDto.getEmail(), userDto.getPassword());

			if (findUser.isEmpty())
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");

			userRepository.deleteByEmail(userDto.getEmail());
			return true;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public SelectUserDto findUser(Long id) {
		try {
			Optional<UserEntity> findUser = userRepository.findById(id);

			return new SelectUserDto(findUser.orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			}));
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public UserEntity findUserById(Long id) {
		try {
			Optional<UserEntity> findUser = userRepository.findById(id);

			return findUser.orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public UserEntity findUser(String email) {
		try {
			Optional<UserEntity> findUser = userRepository.findByEmail(email);

			return findUser.orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "유저가 존재하지 않음");
			});
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public List<SelectUserDto> findUserLikeName(String name, String email) {
		try {
			List<UserEntity> findUsers = userRepository.findByNameLike(name, email);
			List<SelectUserDto> userList = new ArrayList<>();

			for (UserEntity findUser : findUsers)
				userList.add(new SelectUserDto(findUser));

			return userList;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	public boolean checkUserSame(String email, Long id) {
		UserEntity findUser;
		try {
			findUser = userRepository.findByEmail(email).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 못찾음");
			});
			if (findUser.getId().equals(id))
				return true;
			else
				return false;
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}

	@Override
	@Transactional
	public void updateProfileImage(String email, String fileUrl) {
		UserEntity findUser;
		try {
			findUser = userRepository.findByEmail(email).orElseGet(() -> {
				throw new HttpClientErrorException(HttpStatus.NOT_FOUND, "유저 못찾음");
			});
			findUser.setProfileImage(fileUrl);
			userRepository.save(findUser);
		} catch (Exception e) {
			e.printStackTrace();
			throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "서버 에러");
		}
	}
}
