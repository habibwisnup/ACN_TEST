package com.BNI.habib.Test.ServiceImpl;

import com.BNI.habib.Test.DTO.UserDTO;
import com.BNI.habib.Test.DTO.UserSettingDTO;
import com.BNI.habib.Test.Entity.Fields.UserSettingKeyLOVFields;
import com.BNI.habib.Test.Entity.User;
import com.BNI.habib.Test.Entity.UserSetting;
import com.BNI.habib.Test.Repository.UserRepository;
import com.BNI.habib.Test.Repository.UserSettingRepository;
import com.BNI.habib.Test.Request.UserRequest;
import com.BNI.habib.Test.Response.UserResponse;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;

import static org.junit.Assert.assertNotNull;
import static org.junit.Assert.assertTrue;
import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.BDDMockito.given;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
class UserServiceImplTest {
    @Mock
    private UserRepository userRepository;

    @Mock
    private UserSettingRepository userSettingRepository;

    @InjectMocks
    private UserServiceImpl userServiceImpl;

    private User user;
    private UserSetting userSetting;

    @BeforeEach
    public void setUp() {
        user = new User();
        user.setId(1L);
        user.setSsn("1234567890");
        user.setFirstName("John");
        user.setFamilyName("Doe");
        user.setIsActive(true);
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedTime(LocalDateTime.now());

        userSetting = new UserSetting();
        userSetting.setId(1L);
        userSetting.setUser(user);
        userSetting.setKey(UserSettingKeyLOVFields.BIOMETRIC_LOGIN);
        userSetting.setValue("true");
    }

    @Test
    public void testCreateNewUser() {
        when(userRepository.save(Mockito.any(User.class))).thenReturn(user);

        List<UserSetting> userSettings = new ArrayList<>();
        userSettings.add(userSetting);

        when(userSettingRepository.saveAll(Mockito.anyList())).thenReturn(userSettings);

        UserResponse userResponse = userServiceImpl.createNewUser(user);

        assertEquals(userResponse.getUser(), user);
        assertEquals(userResponse.getUserSettingsDto().size(), 1);
        assertEquals(userResponse.getUserSettingsDto().get(0).getKey(), userSetting.getKey());
        assertEquals(userResponse.getUserSettingsDto().get(0).getValue(), userSetting.getValue());
    }

    @Test
    public void testGetUsers() {
        // given
        int page = 0;
        int size = 10;
        List<User> users = new ArrayList<>();
        users.add(User.builder()
                .id(1L)
                .ssn("1234567890")
                .firstName("John")
                .middleName("Doe")
                .familyName("Smith")
                .birthDate(LocalDate.from(LocalDateTime.now().minusYears(20)))
                .isActive(true)
                .createdBy("admin")
                .createdTime(LocalDateTime.now().minusDays(1))
                .updatedBy("admin")
                .updatedTime(LocalDateTime.now().minusDays(1))
                .build());
        Page<User> pageResult = new PageImpl<>(users);
        given(userRepository.findAll(PageRequest.of(page, size))).willReturn(pageResult);

        // when
        Page<UserDTO> result = userServiceImpl.getUsers(page, size);

        // then
        assertEquals(1, result.getContent().size());
        assertEquals(1L, result.getContent().get(0).getId());
        assertEquals("1234567890", result.getContent().get(0).getSsn());
        assertEquals("John", result.getContent().get(0).getFirstName());
        assertEquals("Smith", result.getContent().get(0).getFamilyName());
        assertTrue(result.getContent().get(0).getIsActive());
        assertEquals("admin", result.getContent().get(0).getCreatedBy());
        assertEquals("admin", result.getContent().get(0).getUpdatedBy());
    }

    @Test
    public void testGetUser() {
        // given
        long userId = 1L;
        boolean isActive = true;
        User user = new User();
        user.setId(userId);
        user.setSsn("123456789");
        user.setFirstName("John");
        user.setFamilyName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setIsActive(true);
        user.setCreatedBy("admin");
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedBy("admin");
        user.setUpdatedTime(LocalDateTime.now());

        UserSetting userSetting1 = new UserSetting();
        userSetting1.setId(1L);
        userSetting1.setUser(user);
        userSetting1.setKey(UserSettingKeyLOVFields.BIOMETRIC_LOGIN);
        userSetting1.setValue("false");

        UserSetting userSetting2 = new UserSetting();
        userSetting2.setId(2L);
        userSetting2.setUser(user);
        userSetting2.setKey(UserSettingKeyLOVFields.SHOW_ONBOARDING);
        userSetting2.setValue("true");


        List<UserSetting> userSettings = new ArrayList<>();
        userSettings.add(userSetting1);
        userSettings.add(userSetting2);

        given(userRepository.findByIdAndIsActive(userId, isActive)).willReturn(Optional.of(user));
        given(userSettingRepository.findByUserId(userId)).willReturn(userSettings);

        // when
        UserResponse userResponse = userServiceImpl.getUser(userId, isActive);

        // then
        verify(userRepository).findByIdAndIsActive(userId, isActive);
        verify(userSettingRepository).findByUserId(userId);

        assertNotNull(userResponse);
        assertEquals(userResponse.getUser().getId(), userId);
        assertEquals(userResponse.getUser().getSsn(), user.getSsn());
        assertEquals(userResponse.getUser().getFirstName(), user.getFirstName());
        assertEquals(userResponse.getUser().getFamilyName(), user.getFamilyName());
        assertEquals(userResponse.getUser().getBirthDate(), user.getBirthDate());
        assertEquals(userResponse.getUser().getIsActive(), user.getIsActive());
        assertEquals(userResponse.getUser().getCreatedBy(), user.getCreatedBy());
        assertEquals(userResponse.getUser().getUpdatedBy(), user.getUpdatedBy());
        assertEquals(userResponse.getUser().getCreatedTime(), user.getCreatedTime());
        assertEquals(userResponse.getUser().getUpdatedTime(), user.getUpdatedTime());

        assertNotNull(userResponse.getUserSettingsDto());
        assertEquals(userResponse.getUserSettingsDto().size(), 2);
        assertEquals(userResponse.getUserSettingsDto().get(0).getKey(), userSetting1.getKey());
        assertEquals(userResponse.getUserSettingsDto().get(0).getValue(), userSetting1.getValue());
        assertEquals(userResponse.getUserSettingsDto().get(1).getKey(), userSetting2.getKey());
        assertEquals(userResponse.getUserSettingsDto().get(1).getValue(), userSetting2.getValue());
    }

    @Test
    public void testUpdateUser() {
        // given
        long userId = 1L;
        UserRequest userRequest = new UserRequest();
        userRequest.setSsn("123456789");
        userRequest.setFirstName("John");
        userRequest.setFamilyName("Doe");
        userRequest.setBirthDate(LocalDate.of(1990, 1, 1));

        User user = new User();
        user.setId(userId);
        user.setSsn("123456789");
        user.setFirstName("John");
        user.setFamilyName("Doe");
        user.setBirthDate(LocalDate.of(1990, 1, 1));
        user.setIsActive(true);
        user.setCreatedBy("SYSTEM");
        user.setCreatedTime(LocalDateTime.now());
        user.setUpdatedBy("SYSTEM");
        user.setUpdatedTime(LocalDateTime.now());

        User updatedUser = new User();
        updatedUser.setId(userId);
        updatedUser.setSsn(userRequest.getSsn());
        updatedUser.setFirstName(userRequest.getFirstName());
        updatedUser.setFamilyName(userRequest.getFamilyName());
        updatedUser.setBirthDate(userRequest.getBirthDate());
        updatedUser.setIsActive(true);
        updatedUser.setCreatedBy("SYSTEM");
        updatedUser.setCreatedTime(user.getCreatedTime());
        updatedUser.setUpdatedBy("SYSTEM");
        updatedUser.setUpdatedTime(LocalDateTime.now());

        UserSetting userSetting = new UserSetting();
        userSetting.setId(1L);
        userSetting.setUser(user);
        userSetting.setKey(UserSettingKeyLOVFields.SHOW_ONBOARDING);
        userSetting.setValue("false");

        List<UserSetting> userSettings = new ArrayList<>();
        userSettings.add(userSetting);

        UserResponse expectedResponse = UserResponse.builder()
                .user(updatedUser)
                .userSettingsDto(toUserSettingDTO(userSettings))
                .build();

        given(userRepository.findByIdAndIsActive(userId, true)).willReturn(Optional.of(user));
        given(userSettingRepository.findByUserId(userId)).willReturn(userSettings);

        // when
        UserResponse actualResponse = userServiceImpl.updateUser(userId, userRequest);

        // then
        assertEquals(expectedResponse.toString(), actualResponse.toString());
        verify(userRepository, times(1)).findByIdAndIsActive(userId, true);
        verify(userSettingRepository, times(1)).findByUserId(userId);
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