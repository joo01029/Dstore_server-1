package gg.jominsubyungsin.admin.domain.repository;

import gg.jominsubyungsin.admin.domain.dto.query.SelectUserForAdminDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByRole(Role role);
}
