package gg.jominsubyungsin.admin.service.user;

import gg.jominsubyungsin.admin.domain.dto.query.SelectUserForAdminDto;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;

import java.util.List;

public interface AdminUserService {
//    List<SelectUserForAdminDto> getUserList();
    void dropUser(Long id);
    void addPerUser(UserDto userPermissionDto);
    void delPerUser(UserDto user);
    List<SelectUserForAdminDto> getAdminUserList();
    List<SelectUserForAdminDto> getGeneralUserList();
}
