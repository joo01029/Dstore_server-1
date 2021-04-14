package gg.jominsubyungsin.controller;

import gg.jominsubyungsin.domain.entitiy.UserEntitiy;
import gg.jominsubyungsin.response.Response;
import gg.jominsubyungsin.response.admin.UserListResponse;
import gg.jominsubyungsin.service.admin.AdminService;
import javassist.runtime.Desc;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
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

        List<UserEntitiy> userList;

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
        response.setUserEntitiy(userList);

        return response;
    }

}
