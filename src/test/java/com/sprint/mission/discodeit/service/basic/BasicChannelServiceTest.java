package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import static org.mockito.Mockito.never;

import com.sprint.mission.discodeit.dto.request.CreatePrivateChannelDto;
import com.sprint.mission.discodeit.dto.request.CreatePublicChannelDto;
import com.sprint.mission.discodeit.dto.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateChannelDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.channel.DuplicateChannelException;
import com.sprint.mission.discodeit.exception.channel.PrivateChannelUpdateException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
@DisplayName("채널 서비스 테스트")
class BasicChannelServiceTest {

  @Mock
  private ChannelRepository channelRepository;

  @Mock
  private ReadStatusRepository readStatusRepository;

  @Mock
  private UserRepository userRepository;

  @Mock
  private ChannelMapper channelMapper;

  @InjectMocks
  private BasicChannelService channelService;

  private UUID channelId;
  private UUID userId;
  private CreatePublicChannelDto publicRequest;
  private CreatePrivateChannelDto privateRequest;
  private UpdateChannelDto updated;
  private Channel publicChannel;
  private Channel privateChannel;
  private User user;
  private ReadStatus readStatus;
  private ChannelResponseDto publicResponse;
  private ChannelResponseDto privateResponse;

  @BeforeEach
  void setUp() {
    channelId = UUID.randomUUID();
    userId = UUID.randomUUID();

    publicRequest = new CreatePublicChannelDto(
        "공개 채널",
        "채널 정보"
    );

    privateRequest = new CreatePrivateChannelDto(
        List.of(userId)
    );

    updated = new UpdateChannelDto(
        "수정된 채널",
        "수정된 정보"
    );

    publicChannel = new Channel(
        ChannelType.PUBLIC,
        "공개 채널",
        "채널 정보"
    );

    privateChannel = new Channel(
        ChannelType.PRIVATE,
        null,
        null
    );

    user = new User("진우", "a@a.com", "1234", null);
    readStatus = new ReadStatus(user, privateChannel);
    publicResponse = new ChannelResponseDto(
        channelId,
        ChannelType.PUBLIC,
        "공개 채널",
        "채널 정보",
        null,
        null
    );

    privateResponse = new ChannelResponseDto(
        channelId,
        ChannelType.PRIVATE,
        null,
        null,
        null,
        null
    );
  }

  @Nested
  @DisplayName("공개 채널 생성")
  class CreatePublicChannel {

    @Test
    @DisplayName("공개 채널을 생성할 수 있다")
    void createPublicChannel_Success() {
      // given
      when(channelRepository.findByNameAndType(publicRequest.name(), ChannelType.PUBLIC))
          .thenReturn(Optional.empty());
      when(channelRepository.save(any(Channel.class)))
          .thenReturn(publicChannel);
      when(channelMapper.toDto(publicChannel))
          .thenReturn(publicResponse);

      // when
      ChannelResponseDto response = channelService.createPublicChannel(publicRequest);

      // then
      assertThat(response).isNotNull();
      assertThat(response.name()).isEqualTo("공개 채널");
      assertThat(response.type()).isEqualTo(ChannelType.PUBLIC);

      verify(channelRepository).findByNameAndType(publicRequest.name(), ChannelType.PUBLIC);
      verify(channelRepository).save(any(Channel.class));
      verify(channelMapper).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("중복된 채널명시 예외 처리 발생")
    void createPublicChannel_DuplicateChannelName_ThrowsException() {
      // given
      when(channelRepository.findByNameAndType(publicRequest.name(), ChannelType.PUBLIC))
          .thenReturn(Optional.of(publicChannel));

      // when & then
      assertThatThrownBy(() -> channelService.createPublicChannel(publicRequest))
          .isInstanceOf(DuplicateChannelException.class);

      verify(channelRepository).findByNameAndType("공개 채널", ChannelType.PUBLIC);
      verify(channelRepository, never()).save(any(Channel.class));
    }
  }

  @Nested
  @DisplayName("비공개 채널 생성")
  class CreatePrivateChannel {

    @Test
    @DisplayName("비공개 채널을 생성할 수 있다")
    void createPrivateChannel_Success() {
      // given
      when(channelRepository.save(any(Channel.class)))
          .thenReturn(privateChannel);
      when(userRepository.findById(userId))
          .thenReturn(Optional.of(user));
      when(readStatusRepository.save(any(ReadStatus.class)))
          .thenReturn(readStatus);
      when(channelMapper.toDto(privateChannel))
          .thenReturn(privateResponse);

      // when
      ChannelResponseDto response = channelService.createPrivateChannel(privateRequest);

      // then
      assertThat(response).isNotNull();
      assertThat(response.type()).isEqualTo(ChannelType.PRIVATE);

      verify(channelRepository).save(any(Channel.class));
      verify(userRepository).findById(userId);
      verify(readStatusRepository).save(any(ReadStatus.class));
      verify(channelMapper).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("존재하지 않은 유저로 비공개 채널 생성시 예외 발생")
    void createPrivateChannel_NotFoundUser_ThrowsException() {
      // given
      when(channelRepository.save(any(Channel.class)))
          .thenReturn(privateChannel);
      when(userRepository.findById(userId))
          .thenReturn(Optional.empty());

      // when
      assertThatThrownBy(() -> channelService.createPrivateChannel(privateRequest))
          .isInstanceOf(UserNotFoundException.class);

      // then
      verify(channelRepository).save(any(Channel.class));
      verify(userRepository).findById(userId);
      verify(readStatusRepository, never()).save(any(ReadStatus.class));
    }
  }

  @Nested
  @DisplayName("채널 수정")
  class UpdateChannel {

    @Test
    @DisplayName("공개된 채널을 수정할 수 있다")
    void updatePublicChannel_Success() {
      // given
      ChannelResponseDto updateResponse = new ChannelResponseDto(
          channelId,
          ChannelType.PUBLIC,
          "수정된 채널",
          "수정된 설명",
          null,
          null
      );

      when(channelRepository.findById(channelId))
          .thenReturn(Optional.of(publicChannel));
      when(channelMapper.toDto(publicChannel))
          .thenReturn(updateResponse);

      // when
      ChannelResponseDto response = channelService.updateChannel(channelId, updated);

      // then
      assertThat(response).isNotNull();
      assertThat(response.name()).isEqualTo("수정된 채널");

      verify(channelRepository).findById(channelId);
      verify(channelMapper).toDto(any(Channel.class));
    }

    @Test
    @DisplayName("존재하지 않은 채널 수정 시 예외 발생")
    void updateChannel_NotFound_TrowsException() {
      // given
      when(channelRepository.findById(channelId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> channelService.updateChannel(channelId, updated))
          .isInstanceOf(ChannelNotFoundException.class);

      verify(channelRepository).findById(channelId);
      verify(channelRepository, never()).save(any(Channel.class));
    }

    @Test
    @DisplayName("비공개 채널 수정 시 예외 발생")
    void updatePrivateChannel_TrowsException() {
      // given
      when(channelRepository.findById(channelId))
          .thenReturn(Optional.of(privateChannel));

      // when
      assertThatThrownBy(() -> channelService.updateChannel(channelId, updated))
          .isInstanceOf(PrivateChannelUpdateException.class);

      // then
      verify(channelRepository).findById(channelId);
      verify(channelRepository, never()).save(any(Channel.class));
      verify(channelMapper, never()).toDto(any(Channel.class));
    }
  }

  @Nested
  @DisplayName("채널 삭제")
  class DeleteChannel {

    @Test
    @DisplayName("채널을 삭제할 수 있다")
    void deleteChannel_Success() {
      // given
      when(channelRepository.findById(channelId))
          .thenReturn(Optional.of(publicChannel));

      // when & then
      channelService.deleteChannel(channelId);

      verify(channelRepository).findById(channelId);
      verify(channelRepository).delete(publicChannel);
    }

    @Test
    @DisplayName("존재하지 않은 채널 삭제시 예외 발생")
    void deleteChannel_NotFound_ThrowsException() {
      // given
      when(channelRepository.findById(channelId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> channelService.deleteChannel(channelId))
          .isInstanceOf(ChannelNotFoundException.class);

      verify(channelRepository).findById(channelId);
      verify(channelRepository, never()).delete(any(Channel.class));
    }
  }

  @Nested
  @DisplayName("유저 채널 목록 조회")
  class UserChannelListLookup {

    @Test
    @DisplayName("채널에 있는 유저 목록을 조회할 수 있다")
    void findAllByUser_Success() {
      // given
      ReadStatus readStatus = new ReadStatus(user, privateChannel);

      when(readStatusRepository.findAllByUserId(userId))
          .thenReturn(List.of(readStatus));

      when(channelRepository.findAll())
          .thenReturn(List.of(publicChannel, privateChannel));

      when(channelMapper.toDto(publicChannel))
          .thenReturn(publicResponse);
      when(channelMapper.toDto(privateChannel))
          .thenReturn(privateResponse);

      // when
      List<ChannelResponseDto> response = channelService.findAllByUserId(userId);

      // then
      assertThat(response).isNotNull();
      assertThat(response).hasSize(2);

      verify(readStatusRepository).findAllByUserId(userId);
      verify(channelRepository).findAll();
      verify(channelMapper).toDto(publicChannel);
      verify(channelMapper).toDto(privateChannel);
    }
  }
}