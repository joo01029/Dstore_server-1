package gg.jominsubyungsin.admin.domain.response;

import gg.jominsubyungsin.admin.domain.dto.query.SelectUserForAdminDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListResponse extends Response {
    private List<SelectUserForAdminDto> userEntity;
    private int totalPages;
}
