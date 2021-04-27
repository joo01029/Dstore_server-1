package gg.jominsubyungsin.admin.service.user;

import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;

import java.util.List;

public interface AdminUserService {
    List<UserEntity> getUserList();
    void dropUser(Long id);
    void addPerUser(UserDto userPermissionDto);
    void delPerUser(UserDto user);
    List<UserEntity> getAdminUserList();
    List<UserEntity> getGeneralUserList();
}
