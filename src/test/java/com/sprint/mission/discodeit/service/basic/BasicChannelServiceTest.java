package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFReadStatusRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicChannelServiceTest {

    private ChannelService channelService;
    private ChannelRepository channelRepository;
    private ReadStatusRepository readStatusRepository;
    private MessageRepository messageRepository;

    @BeforeEach
    void setup(){
        channelRepository = new JCFChannelRepository();
        readStatusRepository = new JCFReadStatusRepository();
        messageRepository = new JCFMessageRepository();
        channelService = new BasicChannelService(channelRepository, readStatusRepository, messageRepository);
    }

    @Test
    void createPrivateChannel() {
        //given
        List<UUID> users = List.of(UUID.randomUUID(), UUID.randomUUID());
        CreatePrivateChannelDto channelDto = new CreatePrivateChannelDto(null, null, ChannelType.PRIVATE, users);

        //when
        ChannelResponseDto privateChannel = channelService.createPrivateChannel(channelDto);

        //then
        assertNotNull(privateChannel.channelId()); // 채널이 제대로 생성되었는지?
        assertEquals(ChannelType.PRIVATE, privateChannel.channelType()); //채널 타입 확인
        assertEquals(2, privateChannel.members().size()); //유저수가 맞는지

        Channel saveChannel = channelRepository.findById(privateChannel.channelId()).orElseThrow();
        assertEquals(privateChannel.channelId(), saveChannel.getId());
        assertEquals(privateChannel.channelName(), saveChannel.getChannelName());
        assertEquals(privateChannel.description(), saveChannel.getDescription());
        assertEquals(privateChannel.channelType(), saveChannel.getType());
    }

    @Test
    void createPublicChannel() {
        //given
        CreatePublicChannelDto publicChannelDto = new CreatePublicChannelDto("공개채널", "공개채널입니다", ChannelType.PUBLIC);

        //when
        ChannelResponseDto publicChannel = channelService.createPublicChannel(publicChannelDto);

        //then
        assertNotNull(publicChannel.channelId());
        assertEquals(ChannelType.PUBLIC, publicChannel.channelType());

        Channel saveChannel = channelRepository.findById(publicChannel.channelId()).orElseThrow();
        assertEquals(publicChannel.channelId(), saveChannel.getId());
        assertEquals(publicChannel.channelName(), saveChannel.getChannelName());
        assertEquals(publicChannel.description(), saveChannel.getDescription());
        assertEquals(publicChannel.channelType(), saveChannel.getType());
    }

    @Test
    void find() {
        //given
        Channel channel = new Channel("공개채널", "어서오세요.", ChannelType.PUBLIC);
        Channel saveChannel = channelRepository.save(channel);

        //when
        ChannelResponseDto responseDto = channelService.find(saveChannel.getId());

        //then
        assertNotNull(responseDto.channelId());
        assertEquals(channel.getChannelName(), responseDto.channelName());
        assertEquals(channel.getDescription(), responseDto.description());
        assertEquals(channel.getType(), responseDto.channelType());
    }

    @Test
    void findAllByUserId() {
        //given
        UUID userId = UUID.randomUUID();
        Channel publicChannel = new Channel("공개채널", "어서오세요.", ChannelType.PUBLIC);
        channelRepository.save(publicChannel);

        Channel privateChannel = new Channel(null, null, ChannelType.PRIVATE);
        Channel savePrivateChannel = channelRepository.save(privateChannel);
        savePrivateChannel.getMembers().add(userId);

        Channel privateChannel1 = new Channel(null, null, ChannelType.PRIVATE);
        Channel savePrivateChannel1 = channelRepository.save(privateChannel1);
        savePrivateChannel1.getMembers().add(UUID.randomUUID());

        //when
        List<ChannelResponseDto> allByUserId = channelService.findAllByUserId(userId);

        //then
        assertEquals(2, allByUserId.size());

        List<UUID> channelIds = allByUserId.stream()
                .map(ChannelResponseDto::channelId)
                .toList();

        assertTrue(channelIds.contains(publicChannel.getId()));
        assertTrue(channelIds.contains(savePrivateChannel.getId()));
    }

    @Test
    void updateChannel() {
        //given
        Channel publicChannel = new Channel("공개채널", "어서오세요.", ChannelType.PUBLIC);
        Channel saveChannel = channelRepository.save(publicChannel);

        //when
        UpdateChannelDto channelDto = new UpdateChannelDto("수정한채널", "바이요", ChannelType.PUBLIC, publicChannel.getId());
        ChannelResponseDto updateChannel = channelService.updateChannel(channelDto);


        //then
        assertEquals(channelDto.channelName(), updateChannel.channelName());
        assertEquals(channelDto.description(), updateChannel.description());
        assertEquals(channelDto.channelType(), updateChannel.channelType());

        Channel channel = channelRepository.findById(saveChannel.getId()).orElseThrow();

        assertEquals(updateChannel.channelId(), channel.getId());
        assertEquals(updateChannel.channelName(), channel.getChannelName());
        assertEquals(updateChannel.description(), channel.getDescription());
        assertEquals(updateChannel.channelType(), channel.getType());


    }

    @Test
    void deleteChannel() {
        //given
        Channel publicChannel = new Channel("공개채널", "어서오세요.", ChannelType.PUBLIC);
        Channel saveChannel = channelRepository.save(publicChannel);

        //when
        channelService.deleteChannel(saveChannel.getId());

        //then
        Optional<Channel> channel = channelRepository.findById(saveChannel.getId());

        assertTrue(channel.isEmpty());
    }
}