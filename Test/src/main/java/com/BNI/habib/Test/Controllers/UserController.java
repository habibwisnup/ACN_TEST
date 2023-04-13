package com.BNI.habib.Test.Controllers;

import com.BNI.habib.Test.Constant.ApiPath;
import com.BNI.habib.Test.Constant.ResponseCode;
import com.BNI.habib.Test.DTO.UserDTO;
import com.BNI.habib.Test.Entity.User;
import com.BNI.habib.Test.Request.UserRequest;
import com.BNI.habib.Test.Request.UserSettingRequest;
import com.BNI.habib.Test.Response.BaseResponse;
import com.BNI.habib.Test.Response.CommonResponse;
import com.BNI.habib.Test.Response.UserResponse;
import com.BNI.habib.Test.Service.UserService;
import io.swagger.v3.oas.annotations.Operation;
import jakarta.validation.Valid;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.web.bind.annotation.*;
import java.util.List;

@RestController
@RequestMapping(ApiPath.BASE_PATH)
public class UserController {

    @Autowired
    private UserService userService;

    @GetMapping(path = ApiPath.BNI_PATH)
    public BaseResponse<Page<UserDTO>> getUsers(
            @RequestParam(defaultValue = "0") int page,
            @RequestParam(defaultValue = "10") int size) {
        return CommonResponse.constructResponse(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(), null,
                this.userService.getUsers(page, size));
    }

    @GetMapping(path = ApiPath.BNI_PATH+"/{id}")
    public BaseResponse<UserResponse> getUsersById(
            @RequestParam long id) {
        return CommonResponse.constructResponse(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(), null,
                this.userService.getUser(id, true));
    }

    @Operation(summary = "Post Data")
    @PostMapping(path = ApiPath.BNI_PATH)
    public BaseResponse<UserResponse> postService(
            @Valid @RequestBody UserRequest userRequest) {
        return CommonResponse.constructResponse(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(), null,
                this.userService.createNewUser(toUser(userRequest)));
    }

    @PutMapping(path = ApiPath.BNI_PATH+"/{id}")
    public BaseResponse<UserResponse> updateUser(
            @PathVariable long id,
            @Valid @RequestBody UserRequest userRequest) {

        return CommonResponse.constructResponse(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(), null,
                this.userService.updateUser(id, userRequest));
    }

    @PutMapping(path = ApiPath.BNI_PATH+"/{id}/settings")
    public BaseResponse<UserResponse> updateUserSettings(
            @PathVariable long id,
            @Valid @RequestBody List<UserSettingRequest> userSettingsRequest) {

        return CommonResponse.constructResponse(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(), null,
                this.userService.updateUserSettings(id, userSettingsRequest));
    }

    @DeleteMapping(path = ApiPath.BNI_PATH+"/{id}")
    public BaseResponse<UserResponse> deleteUser(
            @PathVariable long id) {
        this.userService.softDelete(id);
        return CommonResponse.constructResponse(ResponseCode.DATA_DELETE.getCode(),
                ResponseCode.DATA_DELETE.getMessage(), null, null);
    }

    @PutMapping(path = ApiPath.BNI_PATH+"/{id}/refresh")
    public BaseResponse<UserResponse> reactivateUser(
            @PathVariable long id) {

        return CommonResponse.constructResponse(ResponseCode.SUCCESS.getCode(),
                ResponseCode.SUCCESS.getMessage(), null,
                this.userService.reActivate(id));
    }

    private User toUser(UserRequest userRequest){
        return User.builder()
                .ssn(userRequest.getSsn())
                .firstName(userRequest.getFirstName())
                .middleName(userRequest.getMiddleName())
                .familyName(userRequest.getFamilyName())
                .birthDate(userRequest.getBirthDate())
                .createdBy("SYSTEM")
                .updatedBy("SYSTEM")
                .isActive(true)
                .build();
    }
}
