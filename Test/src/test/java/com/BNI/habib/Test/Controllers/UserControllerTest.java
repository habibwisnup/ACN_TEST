import static org.junit.Assert.assertEquals;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.ArrayList;
import java.util.Arrays;
import java.util.Collections;
import java.util.List;

import com.BNI.habib.Test.Constant.ApiPath;
import com.BNI.habib.Test.Controllers.UserController;
import com.BNI.habib.Test.DTO.UserDTO;
import com.BNI.habib.Test.DTO.UserSettingDTO;
import com.BNI.habib.Test.Entity.User;
import com.BNI.habib.Test.Entity.UserSetting;
import com.BNI.habib.Test.Request.UserRequest;
import com.BNI.habib.Test.Request.UserSettingRequest;
import com.BNI.habib.Test.Response.UserResponse;
import com.BNI.habib.Test.Service.UserService;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;
import org.junit.Before;
import org.junit.Test;
import org.junit.runner.RunWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.mockito.junit.MockitoJUnitRunner;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.PageImpl;
import org.springframework.http.MediaType;
import org.springframework.test.web.servlet.MockMvc;
import org.springframework.test.web.servlet.MvcResult;
import org.springframework.test.web.servlet.setup.MockMvcBuilders;

@RunWith(MockitoJUnitRunner.class)
public class UserControllerTest {

    private MockMvc mockMvc;
    @Autowired
    private ObjectMapper objectMapper;

    @Mock
    private UserService userService;

    @InjectMocks
    private UserController userController;

    @Before
    public void init() {
        MockitoAnnotations.initMocks(this);
        mockMvc = MockMvcBuilders.standaloneSetup(userController).build();
    }

    @Test
    public void testGetUsers() throws Exception {
        List<UserDTO> users = new ArrayList<>();
        users.add(UserDTO.builder()
                .firstName("john")
                .familyName("doe")
                .id(1L)
                .build());
        when(userService.getUsers(0, 10)).thenReturn(new PageImpl<>(users));

        MvcResult result = mockMvc.perform(get(ApiPath.BASE_PATH + ApiPath.BNI_PATH)
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isOk())
                .andReturn();

        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testGetUserById() throws Exception {
        User user = User.builder()
                .firstName("john")
                .familyName("doe")
                .id(1L)
                .build();
        UserSetting userSetting = UserSetting.builder()
                .id(1L)
                .key("key")
                .value("value")
                .user(user)
                .build();
        List<UserSetting> userSettings = Collections.singletonList(userSetting);
        UserResponse userResponse = UserResponse.builder()
                .user(user)
                .userSettingsDto(toUserSettingDTO(userSettings))
                .build();
        when(userService.getUser(1L, true)).thenReturn(userResponse);

        // when
        MvcResult result = mockMvc.perform(get(ApiPath.BASE_PATH + ApiPath.BNI_PATH + "/1")
                        .contentType(MediaType.APPLICATION_JSON))
                .andExpect(status().isBadRequest())
                .andReturn();

        // then
        assertEquals(result.getResponse().getStatus(), 400);
    }

    private List<UserSettingDTO> toUserSettingDTO(List<UserSetting> userSettings) {
        List<UserSettingDTO> userSettingDTOList = new ArrayList<>();
        for (int i = 0; i < userSettings.size(); i++) {
            UserSettingDTO userSettingDTO = new UserSettingDTO();
            userSettingDTO.setKey(userSettings.get(i).getKey());
            userSettingDTO.setValue(userSettings.get(i).getValue());

            userSettingDTOList.add(userSettingDTO);
        }
        return userSettingDTOList;
    }
    @Test
    public void testCreateNewUser() throws Exception {
        // given
        UserRequest userRequest = new UserRequest();
        userRequest.setFirstName("john");
        userRequest.setFamilyName("doe");
        userRequest.setSsn("1510448003699029");

        User user = toUser(userRequest);
        UserResponse userResponse = UserResponse.builder()
                .user(user)
                .userSettingsDto(new ArrayList<>())
                .build();
        when(userService.createNewUser(user)).thenReturn(userResponse);

        String requestBody = new ObjectMapper().writeValueAsString(userRequest);

        // when
        MvcResult result = mockMvc.perform(post(ApiPath.BASE_PATH + ApiPath.BNI_PATH)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // then
        assertEquals(result.getResponse().getStatus(), 200);
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

    @Test
    public void testUpdateUser() throws Exception {
        long userId = 1L;
        UserRequest expectedUserRequest = new UserRequest();
        expectedUserRequest.setFirstName("John");
        expectedUserRequest.setFamilyName("Doe");
        expectedUserRequest.setSsn("1234567890");
        UserResponse userResponse = new UserResponse();
        when(userService.updateUser(userId, expectedUserRequest)).thenReturn(userResponse);

        String userRequestJson = "{\"firstName\":\"John\",\"familyName\":\"Doe\",\"ssn\":\"1234567890\"}";

        // when
        MvcResult result = mockMvc.perform(put(ApiPath.BASE_PATH + ApiPath.BNI_PATH + "/" + userId)
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(userRequestJson))
                .andExpect(status().isOk())
                .andReturn();

        // then
        ArgumentCaptor<UserRequest> userRequestCaptor = ArgumentCaptor.forClass(UserRequest.class);
        verify(userService).updateUser(eq(userId), userRequestCaptor.capture());
        UserRequest actualUserRequest = userRequestCaptor.getValue();
        assertEquals(expectedUserRequest.toString(), actualUserRequest.toString());
        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testUpdateUserSettings() throws Exception {
        // given
        long userId = 1L;
        List<UserSettingRequest> userSettingsRequest = new ArrayList<>();
        UserResponse userResponse = new UserResponse();
        when(userService.updateUserSettings(userId, userSettingsRequest)).thenReturn(userResponse);

        // when
        String requestBody = "[]";
        MvcResult result = mockMvc.perform(put(ApiPath.BASE_PATH + ApiPath.BNI_PATH + "/" + userId + "/settings")
                        .contentType(MediaType.APPLICATION_JSON)
                        .content(requestBody))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(userService).updateUserSettings(userId, userSettingsRequest);
        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testDeleteUser() throws Exception {
        // given
        long userId = 1L;

        // when
        MvcResult result = mockMvc.perform(delete(ApiPath.BASE_PATH + ApiPath.BNI_PATH + "/" + userId))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(userService).softDelete(userId);
        assertEquals(result.getResponse().getStatus(), 200);
    }

    @Test
    public void testReactivateUser() throws Exception {
        // given
        long userId = 1L;
        UserResponse userResponse = new UserResponse();
        when(userService.reActivate(userId)).thenReturn(userResponse);

        // when
        MvcResult result = mockMvc.perform(put(ApiPath.BASE_PATH + ApiPath.BNI_PATH + "/" + userId + "/refresh"))
                .andExpect(status().isOk())
                .andReturn();

        // then
        verify(userService).reActivate(userId);
        assertEquals(result.getResponse().getStatus(), 200);
    }
}