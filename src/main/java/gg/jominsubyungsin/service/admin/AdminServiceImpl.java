package gg.jominsubyungsin.service.admin;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.domain.repository.admin.UserListRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.util.List;

@RequiredArgsConstructor
@Service
public class AdminServiceImpl implements AdminService {
    private final UserListRepository userListRepository;
    private final UserRepository userRepository;


    @Override
    public List<UserEntitiy> getUserList(Pageable pageable){
        List<UserEntitiy> userList;
        Page<UserEntitiy> userEntityPage;

        try {
            userEntityPage = userListRepository.findAll(pageable);
            System.out.println(userEntityPage);
            userList = userEntityPage.getContent();

            return userList;
        } catch (Exception e) {
            throw e;
        }
    }
}
