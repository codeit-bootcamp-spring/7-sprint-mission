package com.sprint.mission.discodeit.service.jpa;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.channel.ChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelUpdateRequest;
import com.sprint.mission.discodeit.dto.data.ChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class ChannelServiceImplTest {

    @Mock
    private ChannelRepository channelRepository;

    @InjectMocks
    private ChannelServiceImpl channelService;

    @Test
    @DisplayName("create 성공: PUBLIC 채널 생성 후 save 호출")
    void create_public_success() {
        ChannelCreateRequest req = new ChannelCreateRequest(ChannelType.PUBLIC, "general", null);

        given(channelRepository.save(any(Channel.class))).willAnswer(invocation -> {
            Channel c = invocation.getArgument(0);
            ReflectionTestUtils.setField(c, "id", UUID.randomUUID());
            return c;
        });

        ChannelDto result = channelService.create(req);

        assertThat(result).isNotNull();

        ArgumentCaptor<Channel> captor = ArgumentCaptor.forClass(Channel.class);
        then(channelRepository).should().save(captor.capture());
        Channel saved = captor.getValue();

        assertThat(saved.getType()).isEqualTo(ChannelType.PUBLIC);
        assertThat(saved.getName()).isEqualTo("general");
    }

    @Test
    @DisplayName("create 성공: PRIVATE 채널 생성 후 save 호출")
    void create_private_success() {
        ChannelCreateRequest req = new ChannelCreateRequest(
                ChannelType.PRIVATE,
                "private-room",
                List.of(UUID.randomUUID())
        );

        given(channelRepository.save(any(Channel.class))).willAnswer(invocation -> {
            Channel c = invocation.getArgument(0);
            ReflectionTestUtils.setField(c, "id", UUID.randomUUID());
            return c;
        });

        ChannelDto result = channelService.create(req);

        assertThat(result).isNotNull();

        ArgumentCaptor<Channel> captor = ArgumentCaptor.forClass(Channel.class);
        then(channelRepository).should().save(captor.capture());
        Channel saved = captor.getValue();

        assertThat(saved.getType()).isEqualTo(ChannelType.PRIVATE);
        assertThat(saved.getName()).isEqualTo("private-room");
    }

    @Test
    @DisplayName("create 실패: PRIVATE 채널인데 participantIds 비어있음 -> ChannelException")
    void create_private_fail_emptyParticipants() {
        ChannelCreateRequest req = new ChannelCreateRequest(ChannelType.PRIVATE, "private-room", List.of());

        assertThatThrownBy(() -> channelService.create(req))
                .isInstanceOf(ChannelException.class);

        then(channelRepository).should(never()).save(any());
    }

    @Test
    @DisplayName("update 성공: PUBLIC 채널 존재 -> name 변경 후 DTO 반환")
    void update_public_success() {
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel(ChannelType.PUBLIC, "old", "desc");
        ReflectionTestUtils.setField(channel, "id", channelId);

        ChannelUpdateRequest req = new ChannelUpdateRequest(channelId, "new-name");

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        ChannelDto result = channelService.update(channelId, req);

        assertThat(result).isNotNull();
        then(channelRepository).should().findById(channelId);
        assertThat(channel.getName()).isEqualTo("new-name");
    }

    @Test
    @DisplayName("update 실패: 채널 없음 -> ChannelNotFoundException")
    void update_fail_notFound() {
        UUID channelId = UUID.randomUUID();
        ChannelUpdateRequest req = new ChannelUpdateRequest(channelId, "new-name");
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        assertThatThrownBy(() -> channelService.update(channelId, req))
                .isInstanceOf(ChannelNotFoundException.class);

        then(channelRepository).should().findById(channelId);
    }

    @Test
    @DisplayName("update 실패: PRIVATE 채널 -> PrivateChannelUpdateException")
    void update_fail_privateChannel() {
        UUID channelId = UUID.randomUUID();
        Channel channel = new Channel(ChannelType.PRIVATE, "private", null);
        ReflectionTestUtils.setField(channel, "id", channelId);

        ChannelUpdateRequest req = new ChannelUpdateRequest(channelId, "new-name");

        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        assertThatThrownBy(() -> channelService.update(channelId, req))
                .isInstanceOf(PrivateChannelUpdateException.class);

        then(channelRepository).should().findById(channelId);
    }

    @Test
    @DisplayName("findByUserId 성공: PUBLIC 채널 목록 반환")
    void findByUserId_success() {
        UUID userId = UUID.randomUUID();
        Channel c1 = new Channel(ChannelType.PUBLIC, "c1", null);
        Channel c2 = new Channel(ChannelType.PUBLIC, "c2", null);

        given(channelRepository.findAllByType(ChannelType.PUBLIC)).willReturn(List.of(c1, c2));

        List<ChannelDto> result = channelService.findByUserId(userId);

        assertThat(result).hasSize(2);
        then(channelRepository).should().findAllByType(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("findByUserId 실패: userId null -> ChannelException")
    void findByUserId_fail_nullUserId() {
        assertThatThrownBy(() -> channelService.findByUserId(null))
                .isInstanceOf(ChannelException.class);

        then(channelRepository).should(never()).findAllByType(any());
    }
}
