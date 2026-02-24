package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.exception.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.channelNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.UserMapper;
import com.sprint.mission.discodeit.repository.jpa.ChannelsRepository;
import com.sprint.mission.discodeit.repository.jpa.MessagesRepository;
import com.sprint.mission.discodeit.repository.jpa.ReadStatusesRepository;
import com.sprint.mission.discodeit.repository.jpa.UsersRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class ChannelServiceTest {

    @InjectMocks
    private ChannelService channelService;

    // Service dependencies
    @Mock
    private ChannelsRepository channelRepository;
    @Mock
    private MessagesRepository messageRepository;
    @Mock
    private ReadStatusesRepository readStatusRepository;
    @Mock
    private UsersRepository userRepository;

    // Mapper 내부 의존성
    @Mock
    private UserMapper userMapper;

    // 👉 Mapper는 실제 객체로 사용
    @InjectMocks
    private ChannelMapper channelMapper;

    /* =========================
       createPublic
       ========================= */

//    @Test
//    void createPublic_success() {
//        // given
//        Channel channel = new Channel(ChannelType.PUBLIC, "public", "desc");
//        UUID channelId = UUID.randomUUID();
//        channel.setId(channelId);
//
//        given(channelRepository.save(any(Channel.class)))
//            .willReturn(channel);
//
//        given(readStatusRepository.findAllByChannelId(channelId))
//            .willReturn(List.of());
//
//        given(messageRepository.findFirstByChannelIdOrderByCreatedAtDesc(channelId))
//            .willReturn(List.of());
//
//        // when
//        ChannelDto result =
//            channelService.createPublic(
//                new com.sprint.mission.discodeit.mapper.dto.PublicChannelCreateRequest(
//                    "public", "desc"
//                )
//            );
//
//        // then
//        assertThat(result.name()).isEqualTo("public");
//        assertThat(result.type()).isEqualTo(ChannelType.PUBLIC);
//    }

//    @Test
//    void createPrivate_success() {
//        // given
//        UUID userId = UUID.randomUUID();
//        PrivateChannelCreateRequest request =
//            new PrivateChannelCreateRequest(List.of(userId));
//
//        Channel channel =
//            new Channel(ChannelType.PRIVATE, null, null);
//
//        User user =
//            new User("user", "u@test.com", "pw", null);
//
//        given(channelRepository.save(any(Channel.class)))
//            .willReturn(channel);
//
//        given(userRepository.findById(userId))
//            .willReturn(Optional.of(user));
//
//        given(readStatusRepository.save(any(ReadStatus.class)))
//            .willAnswer(invocation -> invocation.getArgument(0));
//
//        given(channelMapper.toDto(channel))
//            .willReturn(mock(ChannelDto.class));
//
//        // when
//        ChannelDto result =
//            channelService.createPrivate(request);
//
//        // then
//        assertThat(result).isNotNull();
//
//        then(readStatusRepository)
//            .should()
//            .save(any(ReadStatus.class));
//    }



//    @Test
//    void createPrivate_fail_user_not_found() {
//        // given
//        UUID userId = UUID.randomUUID();
//        PrivateChannelCreateRequest request =
//            new PrivateChannelCreateRequest(List.of(userId));
//
//        given(channelRepository.save(any(Channel.class)))
//            .willReturn(new Channel(ChannelType.PRIVATE, null, null));
//
//        given(userRepository.findById(userId))
//            .willReturn(Optional.empty());
//
//        // when / then
//        assertThatThrownBy(() ->
//            channelService.createPrivate(request)
//        ).isInstanceOf(UserNotFoundException.class);
//    }

    /* =========================
       findAllByUserId()
       ========================= */

//    @Test
//    void findAllByUserId_success_public_and_private() {
//        // given
//        UUID userId = UUID.randomUUID();
//
//        Channel publicChannel =
//            new Channel(ChannelType.PUBLIC, "public", "desc");
//        Channel privateChannel =
//            new Channel(ChannelType.PRIVATE, null, null);
//
//        given(channelRepository.findAll())
//            .willReturn(List.of(publicChannel, privateChannel));
//
//        given(readStatusRepository
//            .findReadStatusByUserIdAndChannelId(any(), any()))
//            .willReturn(Optional.of(mock(ReadStatus.class)));
//
//        given(channelMapper.toDto(any(Channel.class)))
//            .willReturn(mock(ChannelDto.class));
//
//        // when
//        List<ChannelDto> result =
//            channelService.findAllByUserId(userId);
//
//        // then
//        assertThat(result).hasSize(2);
//    }

    /* =========================
       update()
       ========================= */

//    @Test
//    void update_success_public_channel() {
//        // given
//        UUID channelId = UUID.randomUUID();
//
//        Channel channel =
//            new Channel(ChannelType.PUBLIC, "old", "old");
//
//        ChannelDto_Update update =
//            new ChannelDto_Update("new", "newDesc");
//
//        given(channelRepository.findById(channelId))
//            .willReturn(Optional.of(channel));
//
//        given(channelRepository.save(channel))
//            .willReturn(channel);
//
//        given(channelMapper.toDto(channel))
//            .willReturn(mock(ChannelDto.class));
//
//        // when
//        ChannelDto result =
//            channelService.update(channelId, update);
//
//        // then
//        assertThat(result).isNotNull();
//        assertThat(channel.getName()).isEqualTo("new");
//    }

    @Test
    void update_fail_private_channel() {
        // given
        UUID channelId = UUID.randomUUID();

        Channel channel =
            new Channel(ChannelType.PRIVATE, null, null);

        given(channelRepository.findById(channelId))
            .willReturn(Optional.of(channel));

        // when / then
        assertThatThrownBy(() ->
            channelService.update(channelId,
                new ChannelDto_Update("x", "y"))
        ).isInstanceOf(PrivateChannelUpdateException.class);
    }

    @Test
    void update_fail_channel_not_found() {
        // given
        UUID channelId = UUID.randomUUID();

        given(channelRepository.findById(channelId))
            .willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() ->
            channelService.update(channelId,
                new ChannelDto_Update("x", "y"))
        ).isInstanceOf(channelNotFoundException.class);
    }

    /* =========================
       delete()
       ========================= */

    @Test
    void delete_success() {
        // given
        UUID channelId = UUID.randomUUID();

        Channel channel =
            new Channel(ChannelType.PUBLIC, "name", "desc");

        given(channelRepository.findById(channelId))
            .willReturn(Optional.of(channel));

        given(readStatusRepository.findAll())
            .willReturn(List.of());

        given(messageRepository.findAll())
            .willReturn(List.of());

        // when
        channelService.delete(channelId);

        // then
        then(channelRepository)
            .should()
            .deleteById(channel.getId());
    }

    @Test
    void delete_fail_channel_not_found() {
        // given
        UUID channelId = UUID.randomUUID();

        given(channelRepository.findById(channelId))
            .willReturn(Optional.empty());

        // when / then
        assertThatThrownBy(() ->
            channelService.delete(channelId)
        ).isInstanceOf(channelNotFoundException.class);
    }
}