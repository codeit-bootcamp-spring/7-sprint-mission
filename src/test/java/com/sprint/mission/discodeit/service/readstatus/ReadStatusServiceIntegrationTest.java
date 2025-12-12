package com.sprint.mission.discodeit.service.readstatus;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.readStatus.ReadStatusPatchRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.response.readStatus.ReadStatusDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.BaseEntity;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;

import static org.junit.jupiter.api.Assertions.*;
@SpringBootTest
@Transactional
class ReadStatusServiceIntegrationTest {

    @Autowired
    ReadStatusService readStatusService;

    @Autowired
    ChannelService channelService;

    @Autowired
    UserService userService;

    @Autowired
    ReadStatusRepository readStatusRepository;


    @Test
    @DisplayName("[정상 케이스] readStatus 생성")
    void createReadStatus() throws IOException {
        //given
        UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
        ChannelDto privateChannel = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto(
                        new HashSet<>(List.of(user.id()))
                )
        );
        //when
        ReadStatusDto readStatus = readStatusService.createReadStatus(TestFixture.readStatusCreateFactory(privateChannel.id(),user.id()));


        //then
        var actualResult = readStatus.id();
        var expectedResult = readStatusRepository.findById(readStatus.id()).isPresent();

        assertTrue(expectedResult);
    }

    @Test
    @DisplayName("[정상 케이스] 유저 id로 조회")
    void findAllyByUserId() throws IOException {
        //given
        UserDto userDto =userService.createUser(TestFixture.userCreateFactory(),null);
        ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
        ChannelDto privateChannel = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto
                (new HashSet<>
                        (List.of(userDto.id())
                        )
                )
        );
        //when
        List<ReadStatusDto> allByUserId = readStatusService.findAllyByUserId(userDto.id());

        var actualResult = allByUserId.stream().map(ReadStatusDto::channelId).toList();
        var expectedResult = List.of(privateChannel.id(),publicChannel.id());
        assertEquals(
                new HashSet<>(expectedResult),
                new HashSet<>(actualResult)
        );

    }

    @Test
    @DisplayName("[정상 케이스] readStatus 전체 조회")
    void readAllReadStatus() {
        List<ReadStatusDto> readStatusDtos = readStatusService.readAllReadStatus();
        var actualResult = readStatusDtos.stream().map(ReadStatusDto::id).toList();
        var expectedResult = readStatusRepository.findAll().stream().map(BaseEntity::getId).toList();

        assertEquals(
                new HashSet<>(expectedResult)
                ,
                new HashSet<>(actualResult));
    }

    @Test
    @DisplayName("[정상 케이스] readStatus 변경")
    void patchReadStatus() throws IOException {
        //given
        UserDto user = userService.createUser(TestFixture.userCreateFactory(), null);
        ChannelDto privateChannel = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto(
                        new HashSet<>(List.of(user.id()))
                )
        );
        ReadStatusPatchRequestDto readStatusPatchRequestDto = TestFixture.readStatusPatchFactory();
        ReadStatus readStatus = readStatusRepository.findAll().stream().filter(x -> x.getUser().getId() == user.id()
                && x.getChannel().getId() == privateChannel.id()
        ).findFirst().orElseThrow();

        //when
        ReadStatusDto readStatusDto = readStatusService.patchReadStatus(readStatus.getId()
                , readStatusPatchRequestDto);

        //then
        var actualResult = readStatusDto.lastReadAt();
        var expectedResult = readStatusPatchRequestDto.newLastReadAt();

        assertEquals(expectedResult,actualResult);
    }
}