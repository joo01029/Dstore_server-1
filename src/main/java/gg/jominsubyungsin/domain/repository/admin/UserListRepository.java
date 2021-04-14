package gg.jominsubyungsin.domain.repository.admin;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import org.springframework.data.repository.PagingAndSortingRepository;

import java.awt.print.Pageable;
import java.util.List;

public interface UserListRepository extends PagingAndSortingRepository<UserEntitiy, Long> {

    UserEntitiy findFirstByOrderByIdDesc();
}
