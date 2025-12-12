package com.sprint.mission.discodeit.user.controller;

import com.fasterxml.jackson.core.JsonProcessingException;
import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.module.paramnames.ParameterNamesModule;
import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.controller.UserController;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.http.MediaType;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.context.bean.override.mockito.MockitoBean;
import org.springframework.test.web.servlet.MockMvc;

import java.util.List;

import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.multipart;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.jsonPath;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

@WebMvcTest(UserController.class)
@ActiveProfiles("test")
@DisplayName("UserController Slice Test")
public class UserControllerSliceTest {

    private static final Logger log = LoggerFactory.getLogger(UserControllerSliceTest.class);
    @Autowired
    MockMvc mockMvc;

    @MockitoBean
    UserService userService;

    @MockitoBean
    UserStatusService userStatusService;

    private final com.fasterxml.jackson.databind.ObjectMapper objectMapper = new ObjectMapper()
            .registerModule(new ParameterNamesModule());

    private UserDto userDto;
    private User user;
    private UserStatus userStatus;

    @BeforeEach
    void setUp() {
        user = User.createUserFactory("user1","111@user","1234");
        userDto = new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                null,
                null,
                true
        );
    }

    @Test
    @DisplayName("[정상 케이스] 유저 생성 성공")
    void createUser_Success() throws Exception {

        given(userService.createUser(any(UserCreateRequestDto.class)
        ,any()
        )).willReturn(userDto);
        UserCreateRequestDto request = TestFixture.userCreateFactory();

        MockMultipartFile jsonPart = new MockMultipartFile(
                "userCreateRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );
        mockMvc.perform( multipart("/api/users")
                .file(jsonPart)
                .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
        )
                .andExpect(status().isCreated())
                .andExpect(jsonPath("$.username").value("user1"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(userService).should(times(1))
                .createUser(any(UserCreateRequestDto.class),any());

    }

    @Test
    @DisplayName("[예외 케이스] 유저 생성 실패")
    void createUser_Fail() throws Exception {

        given(userService.createUser(any(UserCreateRequestDto.class)
                ,any()
        )).willReturn(userDto);
        UserCreateRequestDto request = TestFixture.userCreateFactory();

        MockMultipartFile jsonPart = new MockMultipartFile(
                "userCreateNotRequest",
                "",
                MediaType.APPLICATION_JSON_VALUE,
                objectMapper.writeValueAsBytes(request)
        );
        mockMvc.perform( multipart("/api/users")
                        .file(jsonPart)
                        .contentType(MediaType.MULTIPART_FORM_DATA_VALUE)
                )
                .andExpect(status().isInternalServerError())
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(userService).should(never()).createUser(any(UserCreateRequestDto.class),any());
    }

    @Test
    @DisplayName("[정상 케이스] 유저 전체 조회 성공")
    void readAllUser_Success() throws Exception {
        given(userService.findAllUsers()).willReturn(List.of(userDto));
        mockMvc.perform(get("/api/users")
                .contentType(MediaType.APPLICATION_JSON)
        )
                .andExpect(status().isOk())
                .andExpect(jsonPath("$[0].username").value("user1"))
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(userService).should(times(1)).findAllUsers();
    }

    @Test
    @DisplayName("[예외 케이스] 유저 전체 조회 실패")
    void readAllUser_Fail() throws Exception {

        given(userService.findAllUsers()).willReturn(List.of(userDto));
        mockMvc.perform(get("/api/use")
                )
                .andExpect(status().isInternalServerError())
                .andDo(result -> log.info(result.getResponse().getContentAsString()));

        then(userService).should(never()).findAllUsers();
    }







}
