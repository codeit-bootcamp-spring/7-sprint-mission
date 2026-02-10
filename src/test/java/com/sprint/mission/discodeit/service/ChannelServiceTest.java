package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.role.ChannelType;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.fixture.ChannelFixture;
import com.sprint.mission.discodeit.fixture.UserFixture;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.ChannelServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateChannel Test")
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelMapper channelMapper;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private ChannelServiceImpl channelService;

    @Nested
    @DisplayName("채널 생성")
    class CreateChannel {

        @Test
        @DisplayName("공개 채널을 생성할 수 있다.")
        void publicChannelCreate_Success() {
            // given
            PublicChannelCreateRequest request = ChannelFixture.getPublicChannelRequest(1);
            Channel channel = ChannelFixture.getPublicChannel(1);
            ChannelDto dto = ChannelFixture.getPublicChannelDto(1);

            when(channelRepository.save(any(Channel.class))).thenReturn(channel);
            when(channelMapper.toDto(any(Channel.class))).thenReturn(dto);

            // when
            ChannelDto response = channelService.createPublicChannel(request);

            // then
            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelMapper, times(1)).toDto(any(Channel.class));
            assertThat(response.name()).isEqualTo(request.name());
            assertThat(response.type()).isEqualTo(ChannelType.PUBLIC);
        }

        @Test
        @DisplayName("프라이빗 채널을 생성할 수 있다.")
        void privateChannelCreate_Success() {
            // given

            List<UUID> userIds = List.of(UUID.randomUUID(), UUID.randomUUID());
            PrivateChannelCreateRequest request = ChannelFixture.getPrivateChannelRequest(userIds);
            Channel channel = ChannelFixture.getPrivateChannel();
            ChannelDto dto = ChannelFixture.getPrivateChannelDto(userIds);

            when(channelRepository.save(any(Channel.class))).thenReturn(channel);
            when(channelMapper.toDto(any(Channel.class))).thenReturn(dto);

            // when
            ChannelDto response = channelService.createPrivateChannel(request);

            // then
            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelMapper, times(1)).toDto(any(Channel.class));
            verify(userRepository, times(1)).findAllById(userIds);
            verify(readStatusRepository, times(1)).saveAll(anyList());

            assertThat(response.type()).isEqualTo(ChannelType.PRIVATE);
            assertThat(response.participants()).isEqualTo(dto.participants());
        }

        static Stream<List<UUID>> emptyList() {
            return Stream.of(
                    null,
                    List.of()
            );
        }

        @ParameterizedTest
        @MethodSource("emptyList")
        @DisplayName("프라이빗 채널 생성 시, 참여자가 null이거나 비어있을 수 있다.")
        void privateChannelCreate_NullOrEmptyParticipant(List<UUID> participantIds) {
            // given
            PrivateChannelCreateRequest request = ChannelFixture.getPrivateChannelRequest(participantIds);
            ChannelDto dto = ChannelFixture.getPrivateChannelDto(participantIds);

            when(channelMapper.toDto(any(Channel.class))).thenReturn(dto);

            // when
            ChannelDto response = channelService.createPrivateChannel(request);

            // then
            verify(userRepository, never()).findAllById(anyList());
            verify(readStatusRepository, never()).saveAll(anyList());
            verify(channelMapper, times(1)).toDto(any(Channel.class));
            assertThat(response.participants()).isNullOrEmpty();
        }
    }

    @Nested
    @DisplayName("채널 조회")
    class SearchChannel {

        @Test
        @DisplayName("채널id로 채널을 조회할 수 있다.")
        void channelSearchWithId_Success() {
            // given
            Channel channel = ChannelFixture.getPublicChannel(1);
            ChannelDto dto = ChannelFixture.getPublicChannelDto(1);

            when(channelRepository.findById(any())).thenReturn(Optional.of(channel));
            when(channelMapper.toDto(any(Channel.class))).thenReturn(dto);

            // when
            ChannelDto response = channelService.findChannelById(channel.getId());

            // then
            verify(channelRepository, times(1)).findById(any());
            verify(channelMapper, times(1)).toDto(any(Channel.class));

            assertThat(response).isEqualTo(dto);
        }

        @Test
        @DisplayName("유저id로 유저가 참여 중인 모든 채널을 조회할 수 있다.")
        void channelListWithUserId_Success() {
            // given
            UUID userId = UUID.randomUUID();
            User user = UserFixture.getUser(1);
            List<UUID> participantIds = List.of(userId);
            when(userRepository.existsById(any())).thenReturn(true);

            Channel channel1 = ChannelFixture.getPublicChannel(1);
            Channel channel2 = ChannelFixture.getPrivateChannel();
            Channel channel3 = ChannelFixture.getPrivateChannel();
            List<Channel> channelList = List.of(channel1, channel2, channel3);
            when(channelRepository.findAll()).thenReturn(channelList);

            ChannelDto dto1 = ChannelFixture.getPublicChannelDto(1);
            ChannelDto dto2 = ChannelFixture.getPrivateChannelDto(participantIds);
            when(channelMapper.toDto(channel1)).thenReturn(dto1);
            when(channelMapper.toDto(channel2)).thenReturn(dto2);

            ReadStatus readStatus = mock(ReadStatus.class);
            List<ReadStatus> readStatuses = List.of(readStatus);
            when(readStatus.getChannel()).thenReturn(channel2);
            when(readStatusRepository.findAllByUserId(userId)).thenReturn(readStatuses);

            // when
            List<ChannelDto> response = channelService.findAllByUserId(userId);

            // then
            assertThat(response).containsExactlyInAnyOrder(dto1, dto2);
            assertThat(response).extracting(ChannelDto::type)
                    .containsExactly(ChannelType.PUBLIC, ChannelType.PRIVATE);
            verify(channelMapper, never()).toDto(channel3);

        }

        @Test
        @DisplayName("존재하지 않은 채널Id면 예외를 반환한다.")
        void channelSearch_Fail_NotFoundChannel() {
            // given
            UUID channelId = UUID.randomUUID();
            when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.findChannelById(channelId))
                    .isInstanceOf(ChannelNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);

            verify(channelRepository, times(1)).findById(channelId);
        }

        @Test
        @DisplayName("존재하지 않는 유저Id면 예외를 반환한다.")
        void allChannelWithUserId_Fail_NotFoundUser() {
            // given
            UUID userId = UUID.randomUUID();
            when(userRepository.existsById(userId)).thenReturn(false);

            // when & then
            assertThatThrownBy(() -> channelService.findAllByUserId(userId))
                    .isInstanceOf(UserNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);

            verify(userRepository, times(1)).existsById(userId);
            verify(channelRepository, never()).findAll();
            verify(channelMapper, never()).toDto(any(Channel.class));
            verify(readStatusRepository, never()).findAllByUserId(any());
        }
    }

    @Nested
    @DisplayName("채널 정보 수정")
    class UpdateChannel {

        @Test
        @DisplayName("공개 채널의 정보를 수정할 수 있다.")
        void publicChannelUpdate_Success() {
            // given
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDesc");
            Channel channel = ChannelFixture.getPublicChannel(1);
            ChannelDto dto = ChannelFixture.getUpdatedChannelDto(request, channel.getId());

            when(channelRepository.findById(any())).thenReturn(Optional.of(channel));
            when(channelMapper.toDto(any(Channel.class))).thenReturn(dto);

            // when
            ChannelDto response = channelService.updateChannel(channel.getId(), request);

            // then
            assertThat(response.name()).isEqualTo("newName");
            assertThat(response.description()).isEqualTo(dto.description());
            assertThat(response.type()).isEqualTo(ChannelType.PUBLIC).isEqualTo(dto.type());

            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelRepository, times(1)).findById(any());
            verify(channelMapper, times(1)).toDto(any(Channel.class));
        }

        @Test
        @DisplayName("채널이 존재하지 않으면 수정에 실패한다.")
        void publicChannelUpdate_Fail_NotFoundChannel() {
            // given
            UUID channelId = UUID.randomUUID();
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new", "new");

            when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
                    .isInstanceOf(ChannelNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
        }

        @Test
        @DisplayName("프라이빗 채널은 수정할 수 없다.")
        void updatePrivateChannel_ThrowException() {
            // given
            UUID channelId = UUID.randomUUID();
            Channel channel = ChannelFixture.getPrivateChannel();
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("new", "new");

            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

            // when & then
            assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
                    .isInstanceOf(PrivateChannelUpdateException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.PRIVATE_CHANNEL_UPDATE);

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 삭제")
    class DeleteChannel {

        @Test
        @DisplayName("채널을 삭제 할 수 있다.")
        void deleteChannel_Success() {
            // given
            Channel channel = ChannelFixture.getPublicChannel(1);
            UUID channelId = channel.getId();
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(channel));

            // when
            channelService.deleteChannel(channelId);

            // then
            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, times(1)).deleteById(channelId);
        }

        @Test
        @DisplayName("채널 삭제 실패: 존재하지 않는 채널")
        void deleteChannel_NotFound_ThrowException() {
            // given
            UUID channelId = UUID.randomUUID();
            when(channelRepository.findById(channelId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> channelService.deleteChannel(channelId))
                    .isInstanceOf(ChannelNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).deleteById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
        }
    }
}