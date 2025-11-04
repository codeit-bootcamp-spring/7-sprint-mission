package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserUpdateReq;
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

class UserUpdateFacadeTest {
    private UserUpdateFacade userUpdateFacade;
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
        userUpdateFacade = new UserUpdateFacade(userService, binaryContentService, userStatusService);

        // Data
        sampleData = "original profile".getBytes(StandardCharsets.UTF_8);
        profileImageReq = new BinaryContentCreateReq(sampleData, "original.png", "image/png");
    }

    @Test
    void updateUser_withoutNewProfile_shouldUpdateBasicInfoOnly() {
        // 1️⃣ 유저 생성 (프로필 없이)
        UserCreateReq createReq = new UserCreateReq(
                "basic@email.com",
                "OldNick",
                "oldPassword",
                new BinaryContentCreateReq(null, null, null)
        );
        userCreationFacade.createUser(createReq);
        User oldUser = userService.findByEmail("basic@email.com");

        // 2️⃣ 업데이트 요청 (프로필 없음)
        UserUpdateReq updateReq = new UserUpdateReq(
                "basic@email.com",
                "NewNick",
                "newPassword",
                new BinaryContentCreateReq(null, null, null)
        );
        userUpdateFacade.updateUser(oldUser.getId(), updateReq);

        // 3️⃣ 검증
        User updatedUser = userService.findById(oldUser.getId());
        assertEquals("NewNick", updatedUser.getNickname());
        assertEquals("newPassword", updatedUser.getPassword());
        assertNull(updatedUser.getProfileId()); // 프로필 없음 유지
    }

    @Test
    void updateUser_withNewProfile_shouldReplaceBinaryContent() {
        // 1️⃣ 유저 생성 (기존 프로필 포함)
        UserCreateReq createReq = new UserCreateReq(
                "imageuser@email.com",
                "ImageNick",
                "password123",
                profileImageReq
        );
        userCreationFacade.createUser(createReq);
        User oldUser = userService.findByEmail("imageuser@email.com");
        UUID oldProfileId = oldUser.getProfileId();
        assertNotNull(oldProfileId);

        // 2️⃣ 새 프로필 이미지 준비
        byte[] newData = "new image data".getBytes(StandardCharsets.UTF_8);
        BinaryContentCreateReq newProfileReq = new BinaryContentCreateReq(
                newData,
                "new_profile.png",
                "image/png"
        );

        // 3️⃣ 업데이트 요청
        UserUpdateReq updateReq = new UserUpdateReq(
                "imageuser@email.com",
                "UpdatedNick",
                "newPassword",
                newProfileReq
        );

        userUpdateFacade.updateUser(oldUser.getId(), updateReq);

        // 4️⃣ 검증
        User updatedUser = userService.findById(oldUser.getId());
        assertEquals("UpdatedNick", updatedUser.getNickname());
        assertEquals("newPassword", updatedUser.getPassword());
        assertNotNull(updatedUser.getProfileId());
        assertNotEquals(oldProfileId, updatedUser.getProfileId()); // 새 프로필로 교체됨

        BinaryContent newProfile = binaryContentService.findById(updatedUser.getProfileId());
        assertArrayEquals(newData, newProfile.getData());
        assertEquals("new_profile.png", newProfile.getFileName());
        assertEquals("image/png", newProfile.getFileType());

        // UserStatus 갱신 확인 (예: 존재 여부)
        UserStatus status = userStatusService.findByUserId(updatedUser.getId());
        assertNotNull(status);
    }
}