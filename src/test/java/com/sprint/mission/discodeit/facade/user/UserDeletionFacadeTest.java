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

import java.nio.charset.StandardCharsets;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class UserDeletionFacadeTest {
    private UserDeletionFacade userDeletionFacade;
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
        if (userRepository != null) userRepository.data.clear();
        if (binaryContentRepository != null) binaryContentRepository.data.clear();
        if (userStatusRepository != null) userStatusRepository.data.clear();
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
        userDeletionFacade = new UserDeletionFacade(userService, binaryContentService, userStatusService);

        // Data
        sampleData = "profile data".getBytes(StandardCharsets.UTF_8);
        profileImageReq = new BinaryContentCreateReq(sampleData, "profile.png", "image/png");
    }

    @Test
    void deleteUser_shouldRemoveUserStatusUserAndBinaryContent() {
        // 1️⃣ 유저 생성 (프로필 포함)
        UserCreateReq createReq = new UserCreateReq(
                "deleteuser@email.com",
                "DeleteNick",
                "deletePassword",
                profileImageReq
        );
        userCreationFacade.createUser(createReq);

        // 2️⃣ 생성된 엔티티 조회
        User createdUser = userService.findByEmail("deleteuser@email.com");
        UUID userId = createdUser.getId();
        UUID profileId = createdUser.getProfileId();

        // 존재 확인
        assertNotNull(createdUser);
        assertNotNull(userStatusService.findByUserId(userId));
        assertNotNull(binaryContentService.findById(profileId));

        // 3️⃣ 삭제 실행
        userDeletionFacade.deleteUser(userId);

        // 4️⃣ 검증
        assertThrows(RuntimeException.class, () -> userService.findById(userId));
        assertThrows(RuntimeException.class, () -> userStatusService.findByUserId(userId));
        assertThrows(RuntimeException.class, () -> binaryContentService.findById(profileId));
    }

    @Test
    void deleteUser_withoutProfile_shouldRemoveUserAndStatusOnly() {
        // 1️⃣ 유저 생성 (프로필 없음)
        UserCreateReq createReq = new UserCreateReq(
                "noprofile@email.com",
                "NoProfileNick",
                "noProfilePass",
                new BinaryContentCreateReq(null, null, null)
        );
        userCreationFacade.createUser(createReq);

        // 2️⃣ 조회
        User user = userService.findByEmail("noprofile@email.com");
        UUID userId = user.getId();

        assertNotNull(user);
        assertNull(user.getProfileId());
        assertNotNull(userStatusService.findByUserId(userId));

        // 3️⃣ 삭제 실행
        userDeletionFacade.deleteUser(userId);

        // 4️⃣ 검증
        assertThrows(RuntimeException.class, () -> userService.findById(userId));
        assertThrows(RuntimeException.class, () -> userStatusService.findByUserId(userId));
    }
}