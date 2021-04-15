package gg.jominsubyungsin.admin.service;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.repository.UserRepository;
import gg.jominsubyungsin.admin.domain.repository.UserListRepository;
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


    /**
     * 유저 리스트
     * @return
     */
    @Override
    public List<UserEntity> getUserList(){
        List<UserEntity> userList;
        Page<UserEntity> userEntityPage;
        List<UserEntity> allUserList;

        try {
            // userEntityPage = userListRepository.findAll(pageable);
            allUserList = userRepository.findAll();

            return allUserList;
        } catch (Exception e) {
            throw e;
        }
    }
}
