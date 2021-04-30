package gg.jominsubyungsin.admin.service.user;
import gg.jominsubyungsin.admin.domain.dto.query.SelectUserForAdminDto;
import gg.jominsubyungsin.admin.domain.repository.UserDetailRepository;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.admin.domain.repository.UserListRepository;
import gg.jominsubyungsin.enums.Role;
import lombok.RequiredArgsConstructor;
import org.apache.catalina.User;
import org.springframework.data.domain.Page;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Service;
import org.springframework.web.client.HttpClientErrorException;

import javax.transaction.Transactional;
import java.util.ArrayList;
import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminUserServiceImpl implements AdminUserService {
    private final UserListRepository userListRepository;
    private final UserDetailRepository userRepository;


//    /**
//     * 유저 리스트
//     * @return
//     */
//    @Override
//    public List<SelectUserForAdminDto> getUserList(){
//        List<SelectUserForAdminDto> userList;
//        Page<SelectUserForAdminDto> userEntityPage;
//        List<SelectUserForAdminDto> allUserList;
//
//        try {
//            // userEntityPage = userListRepository.findAll(pageable);
//
//            return allUserList;
//        } catch (Exception e) {
//            e.printStackTrace();
//            throw e;
//        }
//    }

    @Override
    public List<SelectUserForAdminDto> getAdminUserList(){
        List<SelectUserForAdminDto> adminUserList = new ArrayList<>();

        List<UserEntity> userEntities;

        try {
            userEntities = userRepository.findByRole(Role.ADMIN);

            for (UserEntity userEntity : userEntities) {
                System.out.println("admin user's id : " + userEntity.getId());
                SelectUserForAdminDto userDto = new SelectUserForAdminDto(userEntity);
                adminUserList.add(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        return adminUserList;
    }

    @Override
    public List<SelectUserForAdminDto> getGeneralUserList(){
        List<SelectUserForAdminDto> generalUserList = new ArrayList<>();

        List<UserEntity> userEntities;

        try {
            userEntities = userRepository.findByRole(Role.USER);

            for (UserEntity userEntity:userEntities) {
                System.out.println("general user's id : " + userEntity.getId());
                SelectUserForAdminDto userDto = new SelectUserForAdminDto(userEntity);
                generalUserList.add(userDto);
            }
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
