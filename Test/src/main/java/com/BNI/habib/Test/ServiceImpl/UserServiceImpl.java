package com.BNI.habib.Test.ServiceImpl;

import com.BNI.habib.Test.Constant.ResponseCode;
import com.BNI.habib.Test.Constant.StringConstant;
import com.BNI.habib.Test.DTO.UserDTO;
import com.BNI.habib.Test.DTO.UserSettingDTO;
import com.BNI.habib.Test.Entity.Fields.UserSettingKeyLOVFields;
import com.BNI.habib.Test.Entity.User;
import com.BNI.habib.Test.Entity.UserSetting;
import com.BNI.habib.Test.Exception.BusinessLogicException;
import com.BNI.habib.Test.Repository.UserRepository;
import com.BNI.habib.Test.Repository.UserSettingRepository;
import com.BNI.habib.Test.Request.UserRequest;
import com.BNI.habib.Test.Request.UserSettingRequest;
import com.BNI.habib.Test.Response.UserResponse;
import com.BNI.habib.Test.Service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

@Service
public class UserServiceImpl implements UserService {
    @Autowired
    private UserRepository userRepository;

    @Autowired
    private UserSettingRepository userSettingRepository;

    @Override
    public UserResponse createNewUser(User user) {
        User savedUser = userRepository.save(user);
        List<UserSetting> userSettings = createDefaultUserSetting(savedUser);

        return UserResponse.builder()
                .user(savedUser)
                .userSettingsDto(toUserSettingDTO(userSettings))
                .build();
    }

    @Override
    public Page<UserDTO> getUsers(int page, int size) {
        Pageable pageable = PageRequest.of(page, size);
        Page<User> users = userRepository.findAll(pageable);
        List<UserDTO> userDTOs = users.getContent().stream().map(user -> {
            return UserDTO.builder()
                    .id(user.getId())
                    .ssn(user.getSsn())
                    .firstName(user.getFirstName())
                    .familyName(user.getFamilyName())
                    .birthDate(user.getBirthDate())
                    .isActive(user.getIsActive())
                    .createdBy(user.getCreatedBy())
                    .updatedBy(user.getUpdatedBy())
                    .createdTime(user.getCreatedTime())
                    .updatedTime(user.getUpdatedTime())
                    .build();
        }).collect(Collectors.toList());
        return new PageImpl<>(userDTOs, pageable, users.getTotalElements());
    }

    @Override
    public UserResponse getUser(long id, boolean isActive) {
        Optional<User> optionalUser = userRepository.findByIdAndIsActive(id, isActive);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<UserSetting> userSettings = userSettingRepository.findByUserId(user.getId());

            return UserResponse.builder()
                    .user(user)
                    .userSettingsDto(toUserSettingDTO(userSettings))
                    .build();
        } else {
            throw new BusinessLogicException(ResponseCode.DATA_NOT_FOUND.getCode(), ResponseCode.DATA_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public UserResponse updateUser(long id, UserRequest userRequest) {
        Optional<User> optionalUser = userRepository.findByIdAndIsActive(id, true);
        if (optionalUser.isPresent()) {
            User userUpdate = buildUserUpdate(optionalUser.get(), userRequest);
            List<UserSetting> userSettings = userSettingRepository.findByUserId(userUpdate.getId());
            userRepository.save(userUpdate);

            return UserResponse.builder()
                    .user(userUpdate)
                    .userSettingsDto(toUserSettingDTO(userSettings))
                    .build();
        } else {
            throw new BusinessLogicException(ResponseCode.DATA_NOT_FOUND.getCode(), ResponseCode.DATA_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public UserResponse updateUserSettings(long id, List<UserSettingRequest> userSettingsRequest) {
        Optional<User> optionalUser = userRepository.findByIdAndIsActive(id, true);
        if (optionalUser.isPresent()) {
            User userUpdate = optionalUser.get();
            List<UserSetting> userSettings = userSettingRepository.findByUserId(userUpdate.getId());
            List<UserSetting> userSettingsUpdate = buildUserSettingsUpdate(userSettings, userSettingsRequest);
            userSettingRepository.saveAll(userSettingsUpdate);

            return UserResponse.builder()
                    .user(userUpdate)
                    .userSettingsDto(toUserSettingDTO(userSettingsUpdate))
                    .build();
        } else {
            throw new BusinessLogicException(ResponseCode.DATA_NOT_FOUND.getCode(), ResponseCode.DATA_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public void softDelete(long id) {
        Optional<User> optionalUser = userRepository.findByIdAndIsActive(id, true);
        if (optionalUser.isPresent()) {
           User user = optionalUser.get();
           user.setIsActive(false);
           user.setDeletedTime(LocalDateTime.now());

           userRepository.save(user);
        } else {
            throw new BusinessLogicException(ResponseCode.DATA_NOT_FOUND.getCode(), ResponseCode.DATA_NOT_FOUND.getMessage() + id);
        }
    }

    @Override
    public UserResponse reActivate(long id) {
        Optional<User> optionalUser = userRepository.findByIdAndIsActive(id, false);
        if (optionalUser.isPresent()) {
            User user = optionalUser.get();
            List<UserSetting> userSettings = userSettingRepository.findByUserId(user.getId());
            user.setIsActive(true);
            user.setDeletedTime(null);

            userRepository.save(user);

            return UserResponse.builder()
                    .user(user)
                    .userSettingsDto(toUserSettingDTO(userSettings))
                    .build();
        } else {
            throw new BusinessLogicException(ResponseCode.DATA_NOT_FOUND.getCode(), ResponseCode.DATA_NOT_FOUND.getMessage() + id);
        }
    }

    public User buildUserUpdate(User user, UserRequest userRequest) {
        user.setFirstName(userRequest.getFirstName());
        user.setFamilyName(userRequest.getFamilyName());
        user.setMiddleName(userRequest.getMiddleName());
        user.setBirthDate(userRequest.getBirthDate());

        return user;
    }

    public List<UserSetting> buildUserSettingsUpdate(List<UserSetting> userSettings, List<UserSettingRequest> userSettingsRequest) {
        for (int i = 0; i < userSettings.size(); i++) {
            if (userSettings.get(i).getKey().equals(UserSettingKeyLOVFields.BIOMETRIC_LOGIN))
                userSettings.get(i).setValue(userSettingsRequest.get(0).getBiometricLogin());
            if (userSettings.get(i).getKey().equals(UserSettingKeyLOVFields.PUSH_NOTIFICATION))
                userSettings.get(i).setValue(userSettingsRequest.get(0).getPushNotification());
            if (userSettings.get(i).getKey().equals(UserSettingKeyLOVFields.SMS_NOTIFICATION))
                userSettings.get(i).setValue(userSettingsRequest.get(0).getSmsNotification());
            if (userSettings.get(i).getKey().equals(UserSettingKeyLOVFields.SHOW_ONBOARDING))
                userSettings.get(i).setValue(userSettingsRequest.get(0).getShowOnboarding());
            if (userSettings.get(i).getKey().equals(UserSettingKeyLOVFields.WIDGET_ORDER))
                userSettings.get(i).setValue(userSettingsRequest.get(0).getWidgetOrder());
        }

        return userSettings;
    }

    public List<UserSetting> createDefaultUserSetting(User user) {
        List<UserSetting> userSettings = new ArrayList<>();

        userSettings.add(createUserSetting(user, StringConstant.BIOMETRIC, StringConstant.FALSE));
        userSettings.add(createUserSetting(user, StringConstant.PUSH_NOTIFICATION, StringConstant.FALSE));
        userSettings.add(createUserSetting(user, StringConstant.SMS_NOTIFICATION, StringConstant.FALSE));
        userSettings.add(createUserSetting(user, StringConstant.SHOW_ONBOARDING, StringConstant.FALSE));
        userSettings.add(createUserSetting(user, StringConstant.WIDGET_ORDER, StringConstant.ORDER));

        return userSettingRepository.saveAll(userSettings);
    }

    public UserSetting createUserSetting(User user, String key, String value) {
        UserSetting userSetting = new UserSetting();

        userSetting.setUser(user);
        userSetting.setKey(key);
        userSetting.setValue(value);

        return userSetting;
    }

    public List<UserSettingDTO> toUserSettingDTO(List<UserSetting> userSettings) {
        List<UserSettingDTO> userSettingDTOList = new ArrayList<>();
        for (int i = 0; i < userSettings.size(); i++) {
            UserSettingDTO userSettingDTO = new UserSettingDTO();
            userSettingDTO.setKey(userSettings.get(i).getKey());
            userSettingDTO.setValue(userSettings.get(i).getValue());

            userSettingDTOList.add(userSettingDTO);
        }
        return userSettingDTOList;
    }
}
