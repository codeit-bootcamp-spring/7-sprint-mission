package com.sprint.mission.discodeit.facade.user;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.user.request.UserCreateReq;
import com.sprint.mission.discodeit.dto.user.response.UserDetailInfoRes;
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

import static org.junit.jupiter.api.Assertions.*;

class UserDetailViewFacadeTest {
    private UserDetailViewFacade userDetailViewFacade;
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
        userDetailViewFacade = new UserDetailViewFacade(userService, binaryContentService, userStatusService);

        // Data
        sampleData = "profile image data".getBytes(StandardCharsets.UTF_8);
        profileImageReq = new BinaryContentCreateReq(sampleData, "profile.png", "image/png");
    }

    @Test
    void findByNickname_withProfile_shouldReturnDetailInfo() {
        // 유저 생성
        UserCreateReq createReq = new UserCreateReq(
                "nickuser@email.com",
                "NickUser",
                "password123",
                profileImageReq
        );
        userCreationFacade.createUser(createReq);

        // 조회
        UserDetailInfoRes detailInfo = userDetailViewFacade.findByNickname("NickUser");

        // 검증
        assertEquals("NickUser", detailInfo.nickname());
        assertEquals("nickuser@email.com", detailInfo.email());
        assertNotNull(detailInfo.profileImg());
        assertArrayEquals(sampleData, detailInfo.profileImg().data());
        assertTrue(detailInfo.isOnline());
    }

    @Test
    void findByEmail_withProfile_shouldReturnDetailInfo() {
        // 유저 생성
        UserCreateReq createReq = new UserCreateReq(
                "emailuser@email.com",
                "EmailUser",
                "password456",
                profileImageReq
        );
        userCreationFacade.createUser(createReq);

        // 조회
        UserDetailInfoRes detailInfo = userDetailViewFacade.findByEmail("emailuser@email.com");

        // 검증
        assertEquals("EmailUser", detailInfo.nickname());
        assertEquals("emailuser@email.com", detailInfo.email());
        assertNotNull(detailInfo.profileImg());
        assertArrayEquals(sampleData, detailInfo.profileImg().data());
        assertTrue(detailInfo.isOnline());
    }

    @Test
    void findByNickname_withoutProfile_shouldReturnDetailInfoWithNullProfile() {
        // 유저 생성 (프로필 없음)
        UserCreateReq createReq = new UserCreateReq(
                "noprof@email.com",
                "NoProfile",
                "password789",
                new BinaryContentCreateReq(null, null, null)
        );
        userCreationFacade.createUser(createReq);
        System.out.println();

        // 조회
        UserDetailInfoRes detailInfo = userDetailViewFacade.findByNickname("NoProfile");

        // 검증
        assertEquals("NoProfile", detailInfo.nickname());
        assertEquals("noprof@email.com", detailInfo.email());
        assertNull(detailInfo.profileImg()); // 프로필 없음
        assertTrue(detailInfo.isOnline());
    }
}