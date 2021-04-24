package gg.jominsubyungsin.admin.response;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import lombok.Getter;
import lombok.Setter;

import java.util.List;

@Getter
@Setter
public class UserListResponse extends Response {
    private List<UserEntity> userEntity;
    private int totalPages;
}
