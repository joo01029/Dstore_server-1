package gg.jominsubyungsin.admin.domain.repository;

import gg.jominsubyungsin.domain.entity.UserEntity;
import org.springframework.data.repository.PagingAndSortingRepository;

public interface UserListRepository extends PagingAndSortingRepository<UserEntity, Long> {

}
