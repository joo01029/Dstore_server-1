package gg.jominsubyungsin.admin.domain.dto.user.response;

import gg.jominsubyungsin.admin.domain.dto.user.dataIgnore.SelectUserForAdminDto;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListResponse extends Response {
    private List<SelectUserForAdminDto> users;
    private int totalPages;
}
