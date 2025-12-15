package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.channelDto.ChannelDto;
import com.sprint.mission.discodeit.dto.channelDto.PrivateChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelCreateRequest;
import com.sprint.mission.discodeit.dto.channelDto.PublicChannelUpdateRequest;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.basic.ChannelServiceImpl;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("CreateChannel Test")
class ChannelServiceTest {

    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private ChannelMapper channelMapper;

    @InjectMocks
    private ChannelServiceImpl channelService;

    @Nested
    @DisplayName("채널을 생성할 수 있다.")
    class CreateChannel {

        @Test
        @DisplayName("공개 채널 생성 성공")
        void createPublicChannel_Success() {
            // given
            PublicChannelCreateRequest request = new PublicChannelCreateRequest("공개채널", "공지사항");
            Channel testChannel = new Channel("공개채널", "공지사항", ChannelType.PUBLIC);
            when(channelRepository.save(any(Channel.class))).thenReturn(testChannel);

            ChannelDto result
                    = new ChannelDto(null, ChannelType.PUBLIC, "공개채널", "공지사항", null, null);
            when(channelMapper.toDto(any(Channel.class))).thenReturn(result);

            // when
            ChannelDto channel = channelService.createPublicChannel(request);

            // then
            assertThat(channel.name()).isEqualTo("공개채널");
            assertThat(channel.type()).isEqualTo(ChannelType.PUBLIC);
            assertThat(channel.description()).isEqualTo("공지사항");

            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelMapper, times(1)).toDto(any(Channel.class));
        }

        @Test
        @DisplayName("프라이빗 채널 생성 성공")
        void createPrivateChannel_Success() {
            // given
            PrivateChannelCreateRequest request = new PrivateChannelCreateRequest(null);
            Channel testChannel = new Channel(null, null, ChannelType.PRIVATE);
            when(channelRepository.save(any(Channel.class))).thenReturn(testChannel);

            ChannelDto result
                    = new ChannelDto(null, ChannelType.PRIVATE, null, null, null, null);
            when(channelMapper.toDto(any(Channel.class))).thenReturn(result);

            // when
            ChannelDto channel = channelService.createPrivateChannel(request);

            // then
            assertThat(channel.type()).isEqualTo(ChannelType.PRIVATE);

            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelMapper, times(1)).toDto(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널 정보를 수정할 수 있다.")
    class UpdateChannel {

        @Test
        @DisplayName("공개 채널 정보 수정 성공")
        void updatePublicChannel_Success() {
            // given
            UUID channelId = UUID.randomUUID();

            Channel testChannel = new Channel("oldName", "oldDesc", ChannelType.PUBLIC);
            ReflectionTestUtils.setField(testChannel, "id", channelId);
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(testChannel));

            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest("newName", "newDesc");

            ChannelDto result = new ChannelDto(channelId, ChannelType.PUBLIC, "newName", "newDesc", null, null);
            when(channelMapper.toDto(any(Channel.class))).thenReturn(result);

            // when
            ChannelDto channel = channelService.updateChannel(channelId, request);

            // then
            assertThat(channel.name()).isEqualTo("newName");
            assertThat(channel.description()).isEqualTo("newDesc");
            assertThat(testChannel.getName()).isEqualTo("newName");
            assertThat(testChannel.getDescription()).isEqualTo("newDesc");

            verify(channelRepository, times(1)).save(any(Channel.class));
            verify(channelRepository, times(1)).findById(channelId);
            verify(channelMapper, times(1)).toDto(any(Channel.class));
        }
        
        @Test
        @DisplayName("채널 정보 수정 실패: 채널이 존재하지 않음")
        void updatePublicChannel_NotFound_ThrowException() {
            // given
            UUID channelId = UUID.randomUUID();
            Channel testChannel = new Channel(null, null, null);
            ReflectionTestUtils.setField(testChannel, "id", channelId);
            when(channelRepository.findById(channelId)).thenReturn(Optional.empty());
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(null, null);

            // when & then
            assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
                    .isInstanceOf(ChannelNotFoundException.class)
                    .hasMessage("채널을 찾을 수 없습니다.");

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
        }

        @Test
        @DisplayName("채널 정보 수정 실패: 프라이빗채널")
        void updatePrivateChannel_ThrowException() {
            // given
            UUID channelId = UUID.randomUUID();
            Channel testChannel = new Channel(null, null, ChannelType.PRIVATE);
            ReflectionTestUtils.setField(testChannel, "id", channelId);
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(testChannel));
            PublicChannelUpdateRequest request = new PublicChannelUpdateRequest(null, null);

            // when & then
            assertThatThrownBy(() -> channelService.updateChannel(channelId, request))
                    .isInstanceOf(PrivateChannelUpdateException.class)
                    .hasMessage("프라이빗 채널은 수정할 수 없습니다.");

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
        }
    }

    @Nested
    @DisplayName("채널을 삭제할 수 있다.")
    class DeleteChannel {

        @Test
        @DisplayName("채널 삭제 성공")
        void deleteChannel_Success() {
            // given
            UUID channelId = UUID.randomUUID();
            Channel testChannel = new Channel(null, null, null);
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(testChannel));

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
                    .hasMessage("채널을 찾을 수 없습니다.");

            verify(channelRepository, times(1)).findById(channelId);
            verify(channelRepository, never()).deleteById(channelId);
            verify(channelRepository, never()).save(any(Channel.class));
        }
    }
}