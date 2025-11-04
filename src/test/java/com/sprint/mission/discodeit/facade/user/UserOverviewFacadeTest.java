package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.response.UserSimpleInfoRes;
import com.sprint.mission.discodeit.entity.User;
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

import java.nio.charset.StandardCharsets;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

class UserOverviewFacadeTest {
    private UserOverviewFacade userOverviewFacade;
    private UserCreationFacade userCreationFacade;

    private UserService userService;
    private UserStatusService userStatusService;
    private BinaryContentService binaryContentService;

    private JCFUserRepository userRepository;
    private JCFBinaryContentRepository binaryContentRepository;
    private JCFUserStatusRepository userStatusRepository;

    private byte[] sampleData;
    private BinaryContentCreateReq profileImageReq;

    @BeforeEach
    void cleanRepositories() {
        if (userRepository != null) userRepository.getData().clear();
        if (binaryContentRepository != null) binaryContentRepository.getData().clear();
        if (userStatusRepository != null) userStatusRepository.getData().clear();
    }

    @BeforeEach
    void setup() {
        // Repository
        userRepository = new JCFUserRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userStatusRepository = new JCFUserStatusRepository();

        // Service
        userService = new BasicUserService(userRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository);

        // Facade
        userCreationFacade = new UserCreationFacade(userService, binaryContentService, userStatusService);
        userOverviewFacade = new UserOverviewFacade(userService, binaryContentService, userStatusService);

        // Data
        sampleData = "profile image data".getBytes(StandardCharsets.UTF_8);
        profileImageReq = new BinaryContentCreateReq(sampleData, "profile.png", "image/png");
    }

    @Test
    void findAll_shouldReturnAllUsersWithCorrectProfileAndOnlineStatus() {
        // 1️⃣ 프로필 없는 유저 생성
        UserCreateReq noProfileReq = new UserCreateReq(
                "noprof@example.com",
                "NoProfileUser",
                "password123",
                new BinaryContentCreateReq(null, null, null)
        );
        userCreationFacade.createUser(noProfileReq);

        // 2️⃣ 프로필 있는 유저 생성
        UserCreateReq withProfileReq = new UserCreateReq(
                "withprof@example.com",
                "WithProfileUser",
                "password456",
                profileImageReq
        );
        userCreationFacade.createUser(withProfileReq);

        // 3️⃣ 전체 조회
        List<UserSimpleInfoRes> users = userOverviewFacade.findAll();
        assertEquals(2, users.size());

        // 4️⃣ 검증: 각각의 유저 확인
        UserSimpleInfoRes first = users.stream()
                .filter(u -> u.nickname().equals("NoProfileUser"))
                .findFirst().orElseThrow();
        assertNull(first.profileImg());
        assertTrue(first.isOnline());

        UserSimpleInfoRes second = users.stream()
                .filter(u -> u.nickname().equals("WithProfileUser"))
                .findFirst().orElseThrow();
        assertNotNull(second.profileImg());
        assertArrayEquals(sampleData, second.profileImg().data());
        assertTrue(second.isOnline());
    }
}