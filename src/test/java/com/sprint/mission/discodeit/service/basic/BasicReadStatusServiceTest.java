package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusUpdateRequestDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entityElement.ReadStatusElement;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import java.time.Instant;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
class BasicReadStatusServiceTest {

    private static final Logger log = LoggerFactory.getLogger(BasicReadStatusServiceTest.class);
    @Autowired
    private ReadStatusRepository readStatusRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private BasicReadStatusService basicReadStatusService;

    @BeforeEach
    void setUp() {
        userRepository.resetUserRepository();
        channelRepository.resetChannelRepository();
        readStatusRepository.resetRepository();
    }

    @Test
    @DisplayName("[정상 케이스] - 읽음 상태 생성")
    void createReadStatus(){
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var readStatus = basicReadStatusService.
                createReadStatus(
                        new ReadStatusCreateRequestDto
                                (channel.getId(),user.getId(), Instant.now()));

        //when
        var actualResult = readStatusRepository.readReadStatus(readStatus.getId()).orElseThrow();

        //then
        Assertions.assertThat(actualResult.getId()).isEqualTo(readStatus.getId());
    }

    @Test
    @DisplayName("[예외 케이스] - 이미 존재하는 읽음 상태 에러 발생")
    void createReadStatusError(){
        //given
        var alreadyReadStatus = readStatusRepository.createReadStatus(ReadStatus.builder()
                        .channelId(UUID.randomUUID())
                        .userId(UUID.randomUUID())
                .build());
        var readStatus = new ReadStatusCreateRequestDto(
                alreadyReadStatus.getId(),
                alreadyReadStatus.getUserId(),
                Instant.now());

        //when
        assertThrows(IllegalArgumentException.class, () -> basicReadStatusService.createReadStatus(readStatus));
        //then
    }

    @Test
    @DisplayName("[예외 케이스] - 존재하지 않는 채널 에러 발생")
    void createReadStatusNotExistChannel(){
        //given

        var readStatus = new ReadStatusCreateRequestDto(
                UUID.randomUUID(),
                UUID.randomUUID(),
                Instant.now());
        //when
        assertThrows(IllegalArgumentException.class, () -> basicReadStatusService.createReadStatus(readStatus));
        //then
    }


    @Test
    @DisplayName("[정상 케이스] - 읽음 상태 삭제")
    void deleteReadStatus() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var readStatus = basicReadStatusService.
                createReadStatus(
                        new ReadStatusCreateRequestDto
                                (channel.getId(),user.getId(), Instant.now()));
        //when
        basicReadStatusService.deleteReadStatus(readStatus.getId());
        var actualResult = readStatusRepository.readAllReadStatus().stream().noneMatch(rs -> rs.getId().equals(readStatus.getId()));
        //then
        assertTrue(actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] - 읽음 상태 업데이트")
    void updateReadStatus() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var readStatus = basicReadStatusService.
                createReadStatus(
                        new ReadStatusCreateRequestDto
                                (channel.getId(),user.getId(), Instant.now()));
        //when
        basicReadStatusService.updateReadStatus(
                new ReadStatusUpdateRequestDto<>
                        (ReadStatusElement.CHANNEL_ID,
                                readStatus.getId(), UUID.fromString("00000000-0000-0000-0000-000000000000"))
        );

        var actualResult = basicReadStatusService.readReadStatus(readStatus.getId());
        //then
        assertEquals( UUID.fromString("00000000-0000-0000-0000-000000000000"),actualResult.getChannelId());
    }

    @Test
    @DisplayName("[정상 케이스] - 읽음 상태 조회")
    void readReadStatus() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var readStatus = basicReadStatusService.
                createReadStatus(
                        new ReadStatusCreateRequestDto
                                (channel.getId(),user.getId(), Instant.now()));

        //when
        var actualResult = basicReadStatusService.readReadStatus(readStatus.getId());
        //then
        assertEquals(readStatus,actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] - 읽음 상태 유저 id로 전체 조회")
    void findAllyByUserId() {
        //given
        var user = userRepository.saveUser(User.builder().userName("testUser").name("테스트 유저").email("").password("").build());
        var channel = channelRepository.saveChannel(Channel.builder().name("테스트 채널").description("테스트 채널 설명").build());
        var readStatus = basicReadStatusService.
                createReadStatus(
                        new ReadStatusCreateRequestDto
                                (channel.getId(),user.getId(), Instant.now()));

        var channel2 = channelRepository.saveChannel(Channel.builder().name("테스트 채널2").description("테스트 채널 설명2").build());
        var readStatus2 = basicReadStatusService.
                createReadStatus(
                        new ReadStatusCreateRequestDto(
                                channel2.getId(),user.getId(), Instant.now()
                        )
                );

        //when
        var actualResult = basicReadStatusService.findAllyByUserId(user.getId());

        //then
        assertEquals(2,actualResult.size());



    }
}