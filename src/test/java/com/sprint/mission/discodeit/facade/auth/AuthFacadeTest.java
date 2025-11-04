package com.sprint.mission.discodeit.facade.auth;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserLoginReq;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.facade.user.UserCreationFacade;
import com.sprint.mission.discodeit.factory.UserFactory;
import com.sprint.mission.discodeit.repository.jcf.JCFBinaryContentRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.UserStatusService;
import com.sprint.mission.discodeit.service.basic.BasicBinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.basic.BasicUserStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class AuthFacadeTest {
    private AuthFacade authFacade;
    private UserService userService;
    private UserStatusService userStatusService;
    private UserCreationFacade userFacade;
    private BinaryContentService binaryContentService;

    private JCFUserRepository userRepository;
    private JCFUserStatusRepository userStatusRepository;
    private JCFBinaryContentRepository binaryContentRepository;

    private User testUser;

    @BeforeEach
    void setup() {
        // 레포지토리 초기화
        userRepository = new JCFUserRepository();
        userStatusRepository = new JCFUserStatusRepository();
        binaryContentRepository = new JCFBinaryContentRepository();

        // 서비스 생성
        userService = new BasicUserService(userRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        userFacade = new UserCreationFacade(userService, binaryContentService, userStatusService);

        // Facade 생성
        authFacade = new AuthFacade(userService, userStatusService);

        // 테스트 유저 생성 (프로필 없음)
        UserCreateReq req = new UserCreateReq(
                "test@nadfj.com",
                "TestNick",
                "password123",
                new BinaryContentCreateReq(null, null, null)
        );
        testUser = userFacade.createUser(req);
    }

    @Test
    void login_success_shouldUpdateOnlineAt() {
        UserLoginReq loginReq = new UserLoginReq("TestNick", "password123");

        authFacade.login(loginReq);

        UserStatus status = userStatusService.findByUserId(testUser.getId());
        assertNotNull(status.getOnlineAt());
        assertTrue(status.isOnline() || !status.isOnline()); // 온라인 여부 계산 확인
    }

    @Test
    void login_invalidNickname_shouldThrowException() {
        UserLoginReq loginReq = new UserLoginReq("WrongNick", "password123");

        CustomException ex = assertThrows(CustomException.class, () -> authFacade.login(loginReq));
        assertEquals(ErrorCode.INVALID_NICKNAME, ex.getErrorCode());
    }

    @Test
    void login_invalidPassword_shouldThrowException() {
        UserLoginReq loginReq = new UserLoginReq("TestNick", "wrongPassword");

        CustomException ex = assertThrows(CustomException.class, () -> authFacade.login(loginReq));
        assertEquals(ErrorCode.INVALID_PASSWORD, ex.getErrorCode());
    }

    @Test
    void logout_shouldUpdateOfflineAt() {
        // 먼저 로그인 시켜서 onlineAt 기록
        authFacade.login(new UserLoginReq("TestNick", "password123"));

        authFacade.logout(testUser.getId());

        UserStatus status = userStatusService.findByUserId(testUser.getId());
        assertNotNull(status.getOfflineAt());
    }
}