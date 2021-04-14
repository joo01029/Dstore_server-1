package gg.jominsubyungsin.response.admin;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.response.Response;
import lombok.Getter;
import lombok.Setter;
import org.springframework.data.domain.Page;

import java.util.List;

@Getter
@Setter
public class UserListResponse extends Response {
    private List<UserEntitiy> userEntitiy;
}
