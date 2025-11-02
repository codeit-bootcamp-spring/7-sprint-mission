package com.sprint.mission.discodeit.facade.userstatus;

import com.sprint.mission.discodeit.dto.userStatus.request.UserStatusCreateReq;
import com.sprint.mission.discodeit.dto.userStatus.response.UserStatusViewRes;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import static org.junit.jupiter.api.Assertions.*;

class UserStatusCreateFacadeTest {
    private UserStatusCreateFacade userStatusCreateFacade;
    private BasicUserStatusService userStatusService;
    private UserService userService;

    private UUID userId;

    @BeforeEach
    void setUp() {
        // JCF Repository 생성
        JCFUserRepository userRepository = new JCFUserRepository();
        JCFUserStatusRepository userStatusRepository = new JCFUserStatusRepository();

        // Service 생성
        userService = new BasicUserService(userRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository);

        // Facade 생성
        userStatusCreateFacade = new UserStatusCreateFacade(userStatusService, userService);

        // 테스트용 유저 생성
        User user = userService.create(User.builder()
                .email("user@test.com")
                .nickname("testUser")
                .password("password123")
                .build());

        this.userId = user.getId();
    }

    @Test
    @DisplayName("UserStatus 생성 및 JCF Repository CRUD 확인")
    void testCreateUserStatus_CRUD() {
        UserStatusCreateReq req = new UserStatusCreateReq(userId);

        // CREATE
        UserStatus status = userStatusCreateFacade.create(req);
        assertThat(status).isNotNull();
        assertThat(status.getUserId()).isEqualTo(userId);

        UUID statusId = status.getId();

        // READ by userId
        UserStatus findStatus = userStatusService.findByUserId(userId);
        assertThat(findStatus).isNotNull();
        assertThat(findStatus.getId()).isEqualTo(statusId);

        // READ by id (DTO)
        UserStatusViewRes viewRes = userStatusService.findById(statusId);
        assertThat(viewRes).isNotNull();
        assertThat(viewRes.userId()).isEqualTo(userId);

        // UPDATE OnlineAt
        userStatusService.updateOnlineAt(statusId);
        UserStatusViewRes updatedOnline = userStatusService.findById(statusId);

        // UPDATE OfflineAt
        userStatusService.updateOfflineAt(statusId);
        UserStatusViewRes updatedOffline = userStatusService.findById(statusId);

        // DELETE
        userStatusService.delete(statusId);
        assertThrows(CustomException.class, () -> userStatusService.findById(statusId));
    }

    @Test
    @DisplayName("유저가 없으면 UserStatus 생성 시 예외 발생")
    void testCreateUserStatus_UserNotFound() {
        UserStatusCreateReq req = new UserStatusCreateReq(UUID.randomUUID());
        CustomException ex = assertThrows(CustomException.class,
                () -> userStatusCreateFacade.create(req));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }
}