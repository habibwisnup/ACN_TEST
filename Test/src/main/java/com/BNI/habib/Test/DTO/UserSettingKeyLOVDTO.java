package com.BNI.habib.Test.DTO;

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
public class UserSettingKeyLOVDTO {
    private boolean biometricLogin;

    private boolean pushNotification;

    private boolean smsNotification;

    private boolean showOnboarding;

    private String widgetOrder;
}
