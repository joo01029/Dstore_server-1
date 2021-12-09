package gg.dstore.admin.domain.repository;

import gg.dstore.domain.entity.UserEntity;
import gg.dstore.enums.Role;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface UserDetailRepository extends JpaRepository<UserEntity, Long> {
    List<UserEntity> findByRole(Role role);
}
