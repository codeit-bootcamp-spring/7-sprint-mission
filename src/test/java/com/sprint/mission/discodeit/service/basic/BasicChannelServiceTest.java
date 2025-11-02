package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.channel.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.factory.ChannelFactory;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicChannelServiceTest {

    private BasicChannelService channelService;
    private JCFChannelRepository channelRepository;

    private UUID publicChannelId;
    private UUID privateChannelId;

    @BeforeEach
    void setup() {
        channelRepository = new JCFChannelRepository();
        channelService = new BasicChannelService(channelRepository);

        // Public 채널 생성
        Channel publicChannel = ChannelFactory.create(new ChannelCreateReq(UUID.randomUUID(), "PublicChannel", "Desc"));
        channelRepository.save(publicChannel);
        publicChannelId = publicChannel.getId();

        // Private 채널 생성
        Channel privateChannel = ChannelFactory.create(new ChannelCreateSecReq(UUID.randomUUID(), List.of(UUID.randomUUID())));
        channelRepository.save(privateChannel);
        privateChannelId = privateChannel.getId();
    }

    @Test
    void updatePublicChannel_shouldChangeNameAndDescription() {
        ChannelUpdateReq updateReq = new ChannelUpdateReq("UpdatedName", "UpdatedDesc");

        channelService.update(publicChannelId, updateReq);

        Channel updated = channelRepository.findById(publicChannelId).orElseThrow();
        assertEquals("UpdatedName", updated.getName());
        assertEquals("UpdatedDesc", updated.getDescription());
    }

    @Test
    void updatePrivateChannel_shouldThrowException() {
        ChannelUpdateReq updateReq = new ChannelUpdateReq("NewName", "NewDesc");

        CustomException ex = assertThrows(CustomException.class, () ->
                channelService.update(privateChannelId, updateReq));

        assertEquals(ErrorCode.CHANNEL_PRIVATE_CANNOT_MODIFY, ex.getErrorCode());
    }

    @Test
    void updateNonExistingChannel_shouldThrowException() {
        ChannelUpdateReq updateReq = new ChannelUpdateReq("Name", "Desc");
        UUID nonExistentId = UUID.randomUUID();

        CustomException ex = assertThrows(CustomException.class, () ->
                channelService.update(nonExistentId, updateReq));

        assertEquals(ErrorCode.CHANNEL_NOT_FOUND, ex.getErrorCode());
    }
}