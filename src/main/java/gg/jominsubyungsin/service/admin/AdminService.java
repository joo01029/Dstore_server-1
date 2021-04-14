package gg.jominsubyungsin.service.admin;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    List<UserEntitiy> getUserList(Pageable pageable);

}
