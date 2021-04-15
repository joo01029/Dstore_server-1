package gg.jominsubyungsin.admin.service;

import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import java.util.List;

public interface AdminService {
    List<UserEntity> getUserList();

}
