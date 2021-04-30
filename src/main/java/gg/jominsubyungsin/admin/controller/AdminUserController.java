package gg.jominsubyungsin.admin.controller;

import gg.jominsubyungsin.admin.domain.dto.query.SelectUserForAdminDto;
import gg.jominsubyungsin.admin.domain.response.UserListResponse;
import gg.jominsubyungsin.admin.service.user.AdminUserService;
import gg.jominsubyungsin.domain.dto.user.UserDto;
import gg.jominsubyungsin.domain.entity.UserEntity;
import gg.jominsubyungsin.domain.response.Response;
import lombok.RequiredArgsConstructor;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpStatus;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.client.HttpServerErrorException;

import java.util.List;

@RequiredArgsConstructor
@ResponseBody
@RequestMapping("/admin/user")
@Controller
public class AdminUserController {
    private final AdminUserService adminUserService;

//    /**
//     * 유저 무한 스크롤
//     * @param pageable
//     * @return
//     */
//    @GetMapping("/list")
//    public Response showUserList(Pageable pageable){
//        UserListResponse response = new UserListResponse();
//
////        List<SelectUserForAdminDto> userList;
////        Page<SelectUserForAdminDto> userEntityPage;
//        List<SelectUserForAdminDto> allUserList;
//
//        System.out.println(pageable);
//
//        try {
////            userEntityPage = adminService.getUserList(pageable);
////            userList = userEntityPage.getContent();
//            allUserList = adminUserService.getUserList();
//        } catch (Exception e){
//            throw e;
//        }
//
//        response.setMessage("페이지의 유저 보내기 성공");
//        response.setHttpStatus(HttpStatus.OK);
//        response.setUserEntity(allUserList);
//        response.setTotalPages(0);
//
//        return response;
//    }

    @GetMapping("/general")
    public Response generalUserList() {
        UserListResponse response = new UserListResponse();

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("일반 유저 리스트 가져오기 성공");
        response.setUserEntity(findGeneralUserListAsResponse());
        response.setTotalPages(0);

        return response;
    }

    @GetMapping("/adminUser")
    public Response adminUserList() {
        UserListResponse response = new UserListResponse();

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("어드민 유저 리스트 가져오기 성공");
        response.setUserEntity(findAdminUserListAsResponse());
        response.setTotalPages(0);

        return response;
    }

    /**
     * 유저 삭제
     */
    @DeleteMapping("/delete")
    public Response deleteUser(@RequestParam Long id){
        UserListResponse response = new UserListResponse();
        List<SelectUserForAdminDto> allUserList;

        System.out.println("delete user id = " + id);

        try {
            adminUserService.dropUser(id);
            allUserList = adminUserService.getGeneralUserList();
        } catch (Exception e){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "유저 삭제 실패");
        }

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("유저 삭제 성공");
        response.setUserEntity(allUserList);
        response.setTotalPages(0);

        return response;
    }

    /**
     * 유저 권한 추가
     * @param user
     * @return
     */
    @PutMapping("/permission/add")
    public Response addPermission(@RequestBody UserDto user){
        UserListResponse response = new UserListResponse();


        try {
            adminUserService.addPerUser(user);
        } catch (Exception e){
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "역할 수정 실패");
        }

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("권한 주기 성공");
        response.setUserEntity(findGeneralUserListAsResponse());
        response.setTotalPages(0);

        return response;
    }

    /**
     * 유저 권한 삭제
     * @param user
     * @return
     */
    @DeleteMapping("/permission/delete")
    public Response delPermission(@RequestBody UserDto user){
        UserListResponse response = new UserListResponse();

        try {
            adminUserService.delPerUser(user);
        } catch (Exception e) {
            throw new HttpServerErrorException(HttpStatus.INTERNAL_SERVER_ERROR, "권한 삭제 실패");
        }

        response.setHttpStatus(HttpStatus.OK);
        response.setMessage("권한 삭제 성공");
        response.setUserEntity(findAdminUserListAsResponse());
        response.setTotalPages(0);

        return response;
    }

//    private List<SelectUserForAdminDto> findAllUserListAsResponse(){
//        List<SelectUserForAdminDto> allUserList;
//
//        try {
//            allUserList = adminUserService.getUserList();
//        } catch (Exception e){
//            throw e;
//        }
//
//        return allUserList;
//    }

    private List<SelectUserForAdminDto> findAdminUserListAsResponse(){
        List<SelectUserForAdminDto> adminUserList;

        try {
            adminUserList = adminUserService.getAdminUserList();
        } catch (Exception e) {
            throw e;
        }

        return adminUserList;
    }

    private List<SelectUserForAdminDto> findGeneralUserListAsResponse(){
        List<SelectUserForAdminDto> generalUserList;

        try {
            generalUserList = adminUserService.getGeneralUserList();
        } catch (Exception e) {
            e.printStackTrace();
            throw e;
        }
        return generalUserList;
    }
}
