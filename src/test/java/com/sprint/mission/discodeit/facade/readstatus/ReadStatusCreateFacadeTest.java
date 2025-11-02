package com.sprint.mission.discodeit.facade.readstatus;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.readstatus.request.ReadStatusCreateReq;
import com.sprint.mission.discodeit.dto.readstatus.response.ReadStatusInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.facade.readstatus.ReadStatusCreateFacade;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicReadStatusService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

class ReadStatusCreateFacadeUnitTest {

    private ReadStatusCreateFacade readStatusCreateFacade;
    private BasicReadStatusService readStatusService;
    private ChannelService channelService;
    private UserService userService;

    @BeforeEach
    void setUp() {
        // JCF Repository 생성
        JCFReadStatusRepository readStatusRepository = new JCFReadStatusRepository();
        JCFChannelRepository channelRepository = new JCFChannelRepository();
        JCFUserRepository userRepository = new JCFUserRepository();

        // Service 생성
        readStatusService = new BasicReadStatusService(readStatusRepository);
        channelService = new BasicChannelService(channelRepository);
        userService = new BasicUserService(userRepository);

        // Facade 생성
        readStatusCreateFacade = new ReadStatusCreateFacade(readStatusService, channelService, userService);

        // 테스트용 유저와 채널 생성
        User user = userService.create(User.builder()
                .email("test@test.com")
                .nickname("testUser")
                .password("password123")
                .build());
        Channel channel = channelService.create(new ChannelCreateReq(user.getId(), "Test Channel", "Description"));

        // 저장된 ID를 다음 테스트에서 사용
        this.userId = user.getId();
        this.channelId = channel.getId();
    }

    private UUID userId;
    private UUID channelId;

    @Test
    @DisplayName("ReadStatus 생성 및 JCF Repository CRUD 확인")
    void testCreateReadStatus_CRUD() {
        ReadStatusCreateReq req = new ReadStatusCreateReq(userId, channelId);

        // CREATE
        ReadStatusInfoRes res = readStatusCreateFacade.create(req);
        assertThat(res).isNotNull();
        assertThat(res.userId()).isEqualTo(userId);
        assertThat(res.channelId()).isEqualTo(channelId);

        // READ
        assertThat(readStatusService.findAllByUserId(userId))
                .extracting(rs -> rs.getUserId())
                .contains(userId);

        assertThat(readStatusService.findAllByChannelId(channelId))
                .extracting(rs -> rs.getChannelId())
                .contains(channelId);

        UUID readStatusId = readStatusService.findAllByUserId(userId).get(0).getId();

        // UPDATE
        readStatusService.update(readStatusId);
        assertThat(readStatusService.findById(readStatusId).getUpdatedAt())
                .isNotNull()
                .isAfterOrEqualTo(readStatusService.findById(readStatusId).getCreatedAt());

        // DELETE
        readStatusService.delete(readStatusId);
        assertThrows(CustomException.class, () -> readStatusService.findById(readStatusId));
    }

    @Test
    @DisplayName("채널이 없으면 예외 발생")
    void testCreateReadStatus_ChannelNotFound() {
        ReadStatusCreateReq req = new ReadStatusCreateReq(userId, UUID.randomUUID());
        CustomException ex = assertThrows(CustomException.class,
                () -> readStatusCreateFacade.create(req));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);
    }

    @Test
    @DisplayName("유저가 없으면 예외 발생")
    void testCreateReadStatus_UserNotFound() {
        ReadStatusCreateReq req = new ReadStatusCreateReq(UUID.randomUUID(), channelId);
        CustomException ex = assertThrows(CustomException.class,
                () -> readStatusCreateFacade.create(req));
        assertThat(ex.getErrorCode()).isEqualTo(ErrorCode.USER_NOT_FOUND);
    }
}
