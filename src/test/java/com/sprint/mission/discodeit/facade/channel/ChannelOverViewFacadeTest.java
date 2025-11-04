package com.sprint.mission.discodeit.facade.channel;

import com.sprint.mission.discodeit.dto.channel.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.channel.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.facade.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class ChannelOverViewFacadeTest {
    private ChannelOverViewFacade channelOverViewFacade;
    private JCFChannelRepository channelRepository;
    private JCFMessageRepository messageRepository;
    private ChannelService channelService;
    private MessageService messageService;

    private UUID userA;
    private UUID userB;

    @BeforeEach
    void setUp() {
        channelRepository = new JCFChannelRepository();
        messageRepository = new JCFMessageRepository();

        messageService = new BasicMessageService(messageRepository);
        channelService = new BasicChannelService(channelRepository);
        ChannelMapper channelMapper = new ChannelMapper(messageService);

        channelOverViewFacade = new ChannelOverViewFacade(channelService, channelMapper);

        userA = UUID.randomUUID();
        userB = UUID.randomUUID();

        // --- Public 채널 2개 ---
        Channel public1 = Channel.builder()
                .managerId(userA)
                .name("PublicChannel1")
                .description("Desc1")
                .publicType(ChannelType.PUBLIC)
                .build();

        Channel public2 = Channel.builder()
                .managerId(userB)
                .name("PublicChannel2")
                .description("Desc2")
                .publicType(ChannelType.PUBLIC)
                .build();

        // --- Private 채널 1개 (userA, userB 참여) ---
        Channel private1 = Channel.builder()
                .managerId(userA)
                .users(List.of(userA, userB))
                .publicType(ChannelType.PRIVATE)
                .build();

        channelRepository.save(public1);
        channelRepository.save(public2);
        channelRepository.save(private1);

        // 메시지 추가 (Public1, Private1에 각각)
        Message msg1 = new Message(public1.getId(), userA, "Hello from A");
        Message msg2 = new Message(private1.getId(), userB, "Private Hello");
        messageRepository.save(msg1);
        messageRepository.save(msg2);
    }

    @Test
    @DisplayName("🧩 userA가 참여한 채널들(Public+Private)만 반환되어야 한다")
    void findAllMyChannels_userA_shouldReturnPublicAndPrivate() {
        List<ChannelInfoRes> result = channelOverViewFacade.findAllMyChannels(userA);

        // --- Public 2개 + Private 1개 = 3개 채널이어야 함 ---
        assertEquals(3, result.size());

        // --- 타입별 확인 ---
        boolean hasPublic = result.stream()
                .anyMatch(r -> ((ChannelPublicInfoRes
                        ) r).type().equals("public"));
        boolean hasPrivate = result.stream()
                .anyMatch(r -> r instanceof ChannelPrivateInfoRes);

        assertTrue(hasPublic);
        assertTrue(hasPrivate);

        // --- 메시지 시간 검증 ---
        Optional<ChannelInfoRes> public1 = result.stream()
                .filter(r -> r instanceof ChannelPublicInfoRes pub && "PublicChannel1".equals(pub.name()))
                .findFirst();

        assertTrue(public1.isPresent());
        ChannelPublicInfoRes pubRes = (ChannelPublicInfoRes) public1.get();
        assertNotNull(pubRes.lastMessageTime());
        assertEquals("Desc1", pubRes.description());
    }

    @Test
    @DisplayName("🧩 userB가 참여한 채널들만 반환되어야 한다 (Public+Private)")
    void findAllMyChannels_userB_shouldReturnPublicAndPrivate() {
        List<ChannelInfoRes> result = channelOverViewFacade.findAllMyChannels(userB);

        // --- Public 2개 + Private 1개 = 3개 채널이어야 함 ---
        assertEquals(3, result.size());

        // --- Private 채널의 참여자 확인 ---
        Optional<ChannelPrivateInfoRes> privateResOpt = result.stream()
                .filter(r -> r instanceof ChannelPrivateInfoRes)
                .map(r -> (ChannelPrivateInfoRes) r)
                .findFirst();

        assertTrue(privateResOpt.isPresent());
        ChannelPrivateInfoRes privateRes = privateResOpt.get();
        assertTrue(privateRes.users().contains(userB));
    }
}