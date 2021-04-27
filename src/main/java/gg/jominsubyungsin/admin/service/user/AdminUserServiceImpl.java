package gg.jominsubyungsin.admin.service.user;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.admin.domain.repository.UserListRepository;
import gg.jominsubyungsin.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserListRepository userListRepository;
    private final UserRepository userRepository;


    /**
     * 유저 리스트
     * @return
     */
    @Override
    public List<UserEntity> getUserList(){
        List<UserEntity> userList;
        Page<UserEntity> userEntityPage;
        List<UserEntity> allUserList;

        try {
            // userEntityPage = userListRepository.findAll(pageable);
            allUserList = userRepository.findAll();

            return allUserList;
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

    @Override
    public List<UserEntity> getAdminUserList(){
        List<UserEntity> adminUserList;

        try {
            adminUserList = userRepository.findByRole(Role.ADMIN);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return adminUserList;
    }

    @Override
    public List<UserEntity> getGeneralUserList(){
        List<UserEntity> generalUserList;

        try {
            generalUserList = userRepository.findByRole(Role.USER);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return generalUserList;
    }

    /**
     * 유저 삭제
     * @param id
     */
    @Override
    public void dropUser(Long id){
        try{
            userRepository.deleteById(id);
        } catch (IllegalArgumentException e){
            e.printStackTrace();
            throw new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 유저입니다.");
        }
    }

    /**
     * 유저 admin 권한 추가
     * @param user
     */
    @Override
    @Transactional
    public void addPerUser(UserDto user){
        UserEntity target = isThereUser(user.getId());

        target.setRole(Role.ADMIN);
        try {
            userRepository.save(target);
        } catch (Exception e){
            e.printStackTrace();
            throw e;
        }
    }

    /**
     * 유저 admin 권한 삭제
     * @param user
     */
    @Override
    @Transactional
    public void delPerUser(UserDto user){
        UserEntity target = isThereUser(user.getId());

        target.setRole(Role.USER);
        try {
            userRepository.save(target);
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
    }

//    @Override
//    @Transactional
//    public void okToEmail(Long id) {
//        UserEntity target = isThereUser(id);
//
//        target.set
//    }

    /**
     * 유저 존재 확인
     * @param id
     * @return target(entity)
     */
    private UserEntity isThereUser(Long id){
        return userRepository.findById(id)
                .orElseThrow(
                        () -> new HttpClientErrorException(HttpStatus.BAD_REQUEST, "없는 회원입니다")
                );
    }

}
