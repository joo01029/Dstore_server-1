package gg.jominsubyungsin.admin.controller;

import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.admin.response.UserListResponse;
import gg.jominsubyungsin.admin.service.AdminService;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/admin")
@Controller
public class AdminController {
    private final AdminService adminService;

    /**
     * 유저 무한 스크롤
     * @param pageable
     * @return
     */
    @GetMapping("/userlist")
    public Response showUserList(Pageable pageable){
        UserListResponse response = new UserListResponse();

        List<UserEntity> userList;

        System.out.println(pageable);

        try {
            userList = adminService.getUserList(pageable);
        } catch (Exception e){
            throw e;
        }

        response.setResult(true);
        response.setMessage("페이지의 유저 보내기 성공");
        response.setHttpStatus(HttpStatus.OK);
        response.setStatus(HttpStatus.OK.value());
        response.setUserEntity(userList);

        return response;
    }

}
