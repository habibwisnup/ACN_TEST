package com.BNI.habib.Test.Request;

import jakarta.validation.constraints.NotNull;
import jakarta.validation.constraints.Pattern;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.ToString;
import lombok.experimental.SuperBuilder;

@Getter
@Setter
@SuperBuilder
@NoArgsConstructor
@ToString
public class UserSettingRequest {
    @NotNull
    private String biometricLogin;
    @NotNull
    private String pushNotification;
    @NotNull
    private String smsNotification;
    @NotNull
    private String showOnboarding;

    @Pattern(regexp = "^[1-5](,[1-5]){4}$")
    @NotNull
    private String widgetOrder;
}
