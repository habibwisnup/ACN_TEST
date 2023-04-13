package com.BNI.habib.Test.Response;

import com.BNI.habib.Test.DTO.UserSettingDTO;
import com.BNI.habib.Test.Entity.User;
import com.BNI.habib.Test.Entity.UserSetting;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

import java.util.List;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class UserResponse {
    private User user;
    private List<UserSettingDTO> userSettingsDto;

    @Override
    public String toString() {
        return "UserResponse{" +
                "user=" + user +
                ", userSettings=" + userSettingsDto +
                '}';
    }
}
