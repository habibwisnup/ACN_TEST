package com.BNI.habib.Test.Repository;

import com.BNI.habib.Test.Entity.UserSetting;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface UserSettingRepository extends JpaRepository<UserSetting, Long> {
    List<UserSetting> findByUserId(Long userId);
}
