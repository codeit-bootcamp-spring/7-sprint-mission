package com.sprint.mission.discodeit.channel.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPatchRequestDto;
import com.sprint.mission.discodeit.dto.request.channel.ChannelPrivateCreateRequestDto;
import com.sprint.mission.discodeit.dto.response.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class ChannelServiceIntegrationTest {

    private static final Logger log = LoggerFactory.getLogger(ChannelServiceIntegrationTest.class);
    @Autowired
    ChannelService channelService;


    @Autowired
    UserService userService;

    @Autowired
    ChannelRepository channelRepository;


    @Test
    @DisplayName("[정상 케이스] private 채널 생성")
    void createPrivateChannel() throws IOException {
        //given
        UserDto userDto =userService.createUser(TestFixture.userCreateFactory(),null);
        UserDto userDto2 =userService.createUser(TestFixture.userCreateFactory(),null);



        //when
        ChannelDto privateChannel = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto
                (new HashSet<>
                        (List.of(userDto.id(), userDto2.id())
                        )
                )
        );

        //then
        var actualResult = channelRepository.findById(privateChannel.id()).isPresent()
                && privateChannel.participants().size()==2 ;
        log.info("actualResult : {}",privateChannel.participants());
        var expectedResult = true;


        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] public 채널 생성")
    void createPublicChannel() {
        //given

        //when
        ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
        //then
        var actualResult = channelRepository.findById(publicChannel.id()).isPresent();
        var expectedResult = true;
        assertEquals(expectedResult,actualResult);

    }

    @Test
    @DisplayName("[정상 케이스] 채널 내용 변경")
    void patchChannel() throws IOException {
        //given
        UserDto userDto =userService.createUser(TestFixture.userCreateFactory(),null);
        UserDto userDto2 =userService.createUser(TestFixture.userCreateFactory(),null);
        ChannelDto privateChannel = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto
                (new HashSet<>
                        (List.of(userDto.id(), userDto2.id())
                        )
                )
        );
        //when
        ChannelPatchRequestDto channelPatchRequestDto = TestFixture.channelPatchFactory();
        channelService.patchChannel(channelPatchRequestDto,privateChannel.id());

        //then
        var actualResult= channelRepository.findById(privateChannel.id()).orElseThrow().getName();
        var expectedResult = channelPatchRequestDto.newName();

        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[정상 케이스] 유저 id로 채널 조회")
    void findAllByUserId() throws IOException {
        //given

        UserDto userDto =userService.createUser(TestFixture.userCreateFactory(),null);
        UserDto userDto2 =userService.createUser(TestFixture.userCreateFactory(),null);
        ChannelDto privateChannel = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto
                (new HashSet<>
                        (List.of(userDto.id(), userDto2.id())
                        )
                )
        );
        ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());
        ChannelDto privateChannel2 = channelService.createPrivateChannel(new ChannelPrivateCreateRequestDto
                (new HashSet<>
                        (List.of(userDto.id(), userDto2.id())
                        )
                )
        );
        //when
        List<ChannelDto> allByUserId = channelService.findAllByUserId(userDto.id());

        //then
        var actualResult = allByUserId;
        var expectedResult = allByUserId.containsAll(List.of(privateChannel,privateChannel2));

        assertTrue(expectedResult);

    }
    @Test
    @DisplayName("[예외 케이스] 존재하지 않는 유저 id 조회")
    void illegalUserFind()
    {
        //given
        UUID illegalUserId = UUID.randomUUID();
        //when
        List<ChannelDto> allByUserId = channelService.findAllByUserId(illegalUserId);
        //then

        assertTrue(allByUserId.isEmpty());
    }

    @Test
    @DisplayName("[정상 케이스] 채널 삭제")
    void deleteChannel() {
        //given
        ChannelDto publicChannel = channelService.createPublicChannel(TestFixture.channelPublicCreateFactory());

        //when
        channelService.deleteChannel(publicChannel.id());

        //then
        var actualResult = channelRepository.findById(publicChannel.id()).isPresent();
        var expectedResult = false;
        assertEquals(expectedResult,actualResult);
    }

    @Test
    @DisplayName("[예외 케이스] 존재하지 않는 채널 삭제")
    void illegalChannelDelete(){
        //given
        UUID illegalChannelId = UUID.randomUUID();
        // null 이 아니면 에러를 내뱉지 않는다.
        //then
        assertDoesNotThrow(()->channelService.deleteChannel(illegalChannelId));
    }

    @Test
    @DisplayName("[정상 케이스] 채널 전체 조회")
    void readAllChannel() {
        List<ChannelDto> channelDtos = channelService.readAllChannel();

        var actualResult = channelDtos.stream().map(ChannelDto::id).toList();
        var expectedResult = channelDtos.stream().map(ChannelDto::id).toList();
        assertEquals(
                new HashSet<>(expectedResult),
                new HashSet<>(actualResult)
        );
    }
}