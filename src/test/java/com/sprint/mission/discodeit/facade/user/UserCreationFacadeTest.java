package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
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
import org.springframework.boot.test.context.SpringBootTest;

import java.nio.charset.StandardCharsets;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
class UserCreationFacadeTest {
    private UserCreationFacade userFacade;
    private UserService userService;
    private UserStatusService userStatusService;
    private BinaryContentService binaryContentService;
    private UserCreateReq reqWithoutProfile;
    private UserCreateReq reqWithProfile;
    private JCFUserRepository userRepository;
    private JCFBinaryContentRepository binaryContentRepository;
    private JCFUserStatusRepository userStatusRepository;
    byte[] sampleData;
    BinaryContentCreateReq profileImageReq;

    @BeforeEach
    void cleanRepositories() {
        userRepository.getData().clear();
        binaryContentRepository.getData().clear();
        userStatusRepository.getData().clear();
    }

    @BeforeEach
    void setup() {
        // 1️⃣ Repository 직접 생성
        userRepository = new JCFUserRepository();
        binaryContentRepository = new JCFBinaryContentRepository();
        userStatusRepository = new JCFUserStatusRepository();

        // 2️⃣ Service 생성
        userService = new BasicUserService(userRepository);
        binaryContentService = new BasicBinaryContentService(binaryContentRepository);
        userStatusService = new BasicUserStatusService(userStatusRepository);

        // 3️⃣ Facade 생성
        userFacade = new UserCreationFacade(userService, binaryContentService, userStatusService);

        // 4️⃣ 테스트용 DTO
        reqWithoutProfile = new UserCreateReq(
                "noimage@email.com",
                "NoImageNick",
                "password123",
                new BinaryContentCreateReq(null, null, null)
        );

        sampleData = "dummy image data".getBytes(StandardCharsets.UTF_8);
        profileImageReq = new BinaryContentCreateReq(
                sampleData,
                "profile.png",
                "image/png"
        );
        reqWithProfile = new UserCreateReq(
                "withimage@email.com",
                "WithImageNick",
                "password456",
                profileImageReq
        );
    }

    @Test
    void createUser_withoutProfile_shouldCreateUserAndStatus() {
        // Facade 호출
        userFacade.createUser(reqWithoutProfile);

        // User 확인
        User user = userService.findByEmail("noimage@email.com");
        assertNotNull(user);
        assertEquals("NoImageNick", user.getNickname());
        assertNull(user.getProfileId()); // 프로필 없으므로 null

        // UserStatus 확인
        UserStatus status = userStatusService.findByUserId(user.getId());
        assertNotNull(status);
    }

    @Test
    void createUser_withProfile_shouldCreateUserStatusAndBinaryContent() {
        // Facade 호출
        userFacade.createUser(reqWithProfile);

        // User 확인
        User user = userService.findByEmail("withimage@email.com");
        assertNotNull(user);
        assertEquals("WithImageNick", user.getNickname());
        System.out.println(user);
        assertNotNull(user.getProfileId()); // 프로필 있음

        // BinaryContent 확인
        BinaryContent profile = binaryContentService.findById(user.getProfileId());
        assertNotNull(profile);
        assertArrayEquals("dummy image data".getBytes(StandardCharsets.UTF_8), profile.getData());
        assertEquals("profile.png", profile.getFileName());
        assertEquals("image/png", profile.getFileType());

        // UserStatus 확인
        UserStatus status = userStatusService.findByUserId(user.getId());
        assertNotNull(status);
    }
}