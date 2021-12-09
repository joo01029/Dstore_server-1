package gg.jominsubyungsin.admin.service.user;

import gg.jominsubyungsin.admin.domain.dto.user.dataIgnore.SelectUserForAdminDto;
import gg.jominsubyungsin.domain.dto.user.request.UserDto;
import gg.jominsubyungsin.domain.response.Response;
import gg.jominsubyungsin.enums.Role;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminUserService {
//    List<SelectUserForAdminDto> getUserList();
    void dropUser(Long id);
    void addPerUser(UserDto userPermissionDto);
    void delPerUser(UserDto user);
    List<SelectUserForAdminDto> getAdminUserList();
    List<SelectUserForAdminDto> getGeneralUserList();
    Response getUserList(Pageable pageable, Role role);
}
