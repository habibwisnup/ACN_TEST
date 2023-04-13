package com.BNI.habib.Test.Service;

import com.BNI.habib.Test.DTO.UserDTO;
import com.BNI.habib.Test.Entity.User;
import com.BNI.habib.Test.Request.UserRequest;
import com.BNI.habib.Test.Request.UserSettingRequest;
import com.BNI.habib.Test.Response.UserResponse;
import org.springframework.data.domain.Page;

import java.util.List;

public interface UserService {
    UserResponse createNewUser(User user);
    Page<UserDTO> getUsers(int page, int size);

    UserResponse getUser(long id, boolean isActive);

    UserResponse updateUser(long id, UserRequest userRequest);

    UserResponse updateUserSettings(long id, List<UserSettingRequest> userSettingsRequest);

    void softDelete(long id);

    UserResponse reActivate(long id);
}
