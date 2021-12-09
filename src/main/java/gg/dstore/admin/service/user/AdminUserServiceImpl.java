package gg.dstore.admin.service.user;
import gg.dstore.admin.domain.dto.user.dataIgnore.SelectUserForAdminDto;
import gg.dstore.admin.domain.dto.user.response.UserListResponse;
import gg.dstore.admin.domain.repository.UserDetailRepository;
import gg.dstore.domain.dto.user.request.UserDto;
import gg.dstore.domain.entity.UserEntity;
import gg.dstore.admin.domain.repository.UserListRepository;
import gg.dstore.domain.response.Response;
import gg.dstore.enums.Role;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
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

    @Override
    public Response getUserList(Pageable pageable, Role role) {
        UserListResponse response = new UserListResponse();
        Page<UserEntity> users;
        List<SelectUserForAdminDto> generalUserList = new ArrayList<>();
        Page<SelectUserForAdminDto> result;

        try {
            users = userListRepository.findByRole(pageable, role);
            for (UserEntity u : users) {
                SelectUserForAdminDto userDto = new SelectUserForAdminDto(u);
                generalUserList.add(userDto);
            }
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("유저 보기 성공");
        response.setUsers(generalUserList);
        response.setTotalPages(users.getTotalPages());

        return response;
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
