package com.BNI.habib.Test.Entity;

import com.BNI.habib.Test.Entity.Fields.UserFields;
import com.BNI.habib.Test.Entity.Fields.UserSettingFields;
import jakarta.persistence.*;
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
@Table(name = "user_setting")
public class UserSetting {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    @Column(name = UserSettingFields.ID)
    private Long id;

    @Column(name = UserSettingFields.USER_SETTING_KEY, nullable = false, length = 100)
    private String key;

    @Column(name = UserSettingFields.USER_SETTING_VALUE, nullable = false, length = 100)
    private String value;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = UserSettingFields.USER_ID, nullable = false)
    private User user;
}
