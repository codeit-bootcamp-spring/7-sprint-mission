package com.sprint.mission.discodeit.service.userstatus;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.user.UserCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.userStatus.UserStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.repository.UserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserStatusServiceIntegrationTest {

    @Autowired
    private UserStatusService userStatusService;

    @Autowired
    private UserService userService;

    @Autowired
    private UserStatusRepository userStatusRepository;

    @BeforeEach
    void setUp() {
        userService.resetUserRepository();
    }

    @Test
    @DisplayName("[정상 케이스] userstatus 변경")
    void patchUserStatus() throws IOException {

        //given
        UserCreateRequestDto userDto1 = TestFixture.userCreateFactory();
        UserDto user = userService.createUser(userDto1, null);


        //when
        UserStatusPatchRequestDto userStatusPatchRequestDto = TestFixture.userStatusPatchFactory();
        userStatusService.patchUserStatus(user.id(), userStatusPatchRequestDto);

        //then
        var actualResult = userStatusRepository.findAll().stream().
                filter(x -> x.getUser().getId().equals(user.id())).findFirst().orElseThrow().getLastActiveAt();
        var expectedResult = userStatusPatchRequestDto.newLastActiveAt();

        assertEquals(expectedResult,actualResult);


    }
}