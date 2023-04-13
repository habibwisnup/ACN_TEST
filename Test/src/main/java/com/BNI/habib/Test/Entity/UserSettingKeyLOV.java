package com.BNI.habib.Test.Entity;
import com.BNI.habib.Test.Entity.Fields.UserSettingKeyLOVFields;
import jakarta.persistence.*;
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
@Entity
@Table(name = "user_setting_key_lov")
public class UserSettingKeyLOV {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = UserSettingKeyLOVFields.BIOMETRIC_LOGIN)
    @NotNull
    private boolean biometricLogin = false;

    @Column(name = UserSettingKeyLOVFields.PUSH_NOTIFICATION)
    @NotNull
    private boolean pushNotification = false;

    @Column(name = UserSettingKeyLOVFields.SMS_NOTIFICATION)
    @NotNull
    private boolean smsNotification = false;

    @Column(name = UserSettingKeyLOVFields.SHOW_ONBOARDING)
    @NotNull
    private boolean showOnboarding = false;

    @Column(name = UserSettingKeyLOVFields.WIDGET_ORDER)
    @Pattern(regexp = "^[1-5](,[1-5]){4}$")
    @NotNull
    private String widgetOrder = "1,2,3,4,5";
}
