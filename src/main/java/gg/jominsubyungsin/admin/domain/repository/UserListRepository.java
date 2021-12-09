package gg.jominsubyungsin.admin.domain.repository;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.enums.Role;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserListRepository extends PagingAndSortingRepository<UserEntity, Long> {
    public Page<UserEntity> findByRole(Pageable pageable, Role role);

}
