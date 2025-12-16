package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

import com.sprint.mission.discodeit.dto.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.dto.response.UserResponseDto;
import com.sprint.mission.discodeit.dto.update.UpdateMessageDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.ChannelMapper;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.io.IOException;
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
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;

@ExtendWith(MockitoExtension.class)
@DisplayName("메세지 서비스 테스트")
class BasicMessageServiceTest {

  @Mock
  private MessageRepository messageRepository;
  @Mock
  private UserRepository userRepository;
  @Mock
  private ChannelRepository channelRepository;
  @Mock
  private BinaryContentRepository binaryContentRepository;
  @Mock
  private MessageMapper messageMapper;
  @Mock
  private PageResponseMapper pageResponseMapper;
  @Mock
  private BinaryContentStorage storage;
  @InjectMocks
  private BasicMessageService messageService;

  private UUID userId;
  private UUID channelId;
  private UUID messageId;
  private UserResponseDto userResponse;
  private CreateMessageRequestDto request;
  private UpdateMessageDto updated;
  private MessageResponseDto response;
  private Message message;
  private User user;
  private Channel channel;

  @BeforeEach
  void setUp() {
    userId = UUID.randomUUID();
    channelId = UUID.randomUUID();
    messageId = UUID.randomUUID();

    request = new CreateMessageRequestDto(
        "메시지 테스트",
        channelId,
        userId
    );

    updated = new UpdateMessageDto(
        "수정된 메시지"
    );

    response = new MessageResponseDto(
        messageId,
        null,
        null,
        "메시지 테스트",
        channelId,
        userResponse,
        null
    );

    user = new User("진우", "a@a.com", "1234", null);
    channel = new Channel(ChannelType.PUBLIC, "공개 채널", "채널 설명");

    message = new Message(
        "메시지 테스트",
        channel,
        user,
        null
    );
  }

  @Nested
  @DisplayName("메시지 생성")
  class CreateMessage {

    @Test
    @DisplayName("메시지를 생성 할 수 있다")
    void createMessage_Success() throws IOException {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.of(user));
      when(channelRepository.findById(channelId))
          .thenReturn(Optional.of(channel));
      when(messageRepository.save(any(Message.class)))
          .thenReturn(message);
      when(messageMapper.toDto(any(Message.class)))
          .thenReturn(response);

      // when
      MessageResponseDto result = messageService.createMessage(request, null);

      // then
      assertThat(result).isNotNull();
      assertThat(result.content()).isEqualTo("메시지 테스트");
      assertThat(result.channelId()).isEqualTo(channelId);
      assertThat(result.id()).isEqualTo(messageId);

      verify(userRepository).findById(userId);
      verify(channelRepository).findById(channelId);
      verify(messageRepository).save(any(Message.class));
      verify(messageMapper).toDto(any(Message.class));
    }

    @Test
    @DisplayName("존재하지 않는 유저로 메시지 생성 시 예외 발생")
    void createMessage_UserNotFound() {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> messageService.createMessage(request, null))
          .isInstanceOf(UserNotFoundException.class);

      verify(userRepository).findById(userId);
      verify(channelRepository, never()).findById(channelId);
      verify(messageRepository, never()).save(any(Message.class));
      verify(messageMapper, never()).toDto(any(Message.class));
    }

    @Test
    @DisplayName("존재하지 않은 채널로 메시지 생성 시 예외 발생")
    void createMessage_ChannelNotFound() {
      // given
      when(userRepository.findById(userId))
          .thenReturn(Optional.of(user));
      when(channelRepository.findById(channelId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> messageService.createMessage(request, null))
          .isInstanceOf(ChannelNotFoundException.class);

      verify(userRepository).findById(userId);
      verify(channelRepository).findById(channelId);
      verify(messageRepository, never()).save(any(Message.class));
      verify(messageMapper, never()).toDto(any(Message.class));
    }
  }

  @Nested
  @DisplayName("메시지 수정")
  class UpdateMessage {

    @Test
    @DisplayName("메시지를 수정할 수 있다")
    void updateMessage_Success() {
      // given
      when(messageRepository.findById(messageId))
          .thenReturn(Optional.of(message));
      when(messageMapper.toDto(any(Message.class)))
          .thenReturn(response);

      // when
      MessageResponseDto result = messageService.updateMessage(messageId, updated);

      // then
      assertThat(result).isNotNull();
      assertThat(result).isEqualTo(response);

      verify(messageRepository).findById(messageId);
      verify(messageMapper).toDto(any(Message.class));
    }

    @Test
    @DisplayName("존재하지 않은 메시지 수정시 예외 발생")
    void updateMessage_NotFound() {
      // given
      when(messageRepository.findById(messageId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> messageService.updateMessage(messageId, updated))
          .isInstanceOf(MessageNotFoundException.class);

      verify(messageRepository).findById(messageId);
    }
  }

  @Nested
  @DisplayName("메시지 삭제")
  class DeleteMessage {

    @Test
    @DisplayName("메시지를 삭제할 수 있다")
    void deleteMessage_Success() {
      // given
      when(messageRepository.findById(messageId))
          .thenReturn(Optional.of(message));

      // when
      messageService.deleteMessage(messageId);

      // then
      verify(messageRepository).findById(messageId);
      verify(messageRepository).delete(message);
    }

    @Test
    @DisplayName("존재하지 않은 메시지 삭제시 예외 발생")
    void deleteMessage_NotFound() {
      // given
      when(messageRepository.findById(messageId))
          .thenReturn(Optional.empty());

      // when & then
      assertThatThrownBy(() -> messageService.deleteMessage(messageId))
          .isInstanceOf(MessageNotFoundException.class);

      verify(messageRepository).findById(messageId);
      verify(messageRepository, never()).delete(any(Message.class));
    }
  }

  @Nested
  @DisplayName("모든 채널 메시지 조회")
  class GetAllMessages {

    @Test
    @DisplayName("특정 채널 메시지 조회할 수 있다")
    void findAllMessages_Success() {
      // given
      PageRequest pageable = PageRequest.of(0, 10);
      when(channelRepository.existsById(channelId))
          .thenReturn(true);
      List<Message> messages = List.of(message);
      Slice<Message> messageSlice = new SliceImpl<>(messages, pageable, false);

      when(messageRepository.findByChannelId(channelId, pageable))
          .thenReturn(messageSlice);

      PageResponse<MessageResponseDto> pageResponse = new PageResponse<>(List.of(response),
          0, 10, false, 1L);

      when(pageResponseMapper.fromSlice(any(Slice.class)))
          .thenReturn(pageResponse);

      // when
      PageResponse<MessageResponseDto> result = messageService.findAllByChannelId(channelId,
          pageable);

      // then
      assertThat(result).isNotNull();
      assertThat(result.content()).hasSize(1);
      assertThat(result.hasNext()).isFalse();

      verify(channelRepository).existsById(channelId);
      verify(messageRepository).findByChannelId(channelId, pageable);
      verify(pageResponseMapper).fromSlice(any(Slice.class));
    }

    @Test
    @DisplayName("존재하지 않는 채널 메시지 조회시 예외 발생")
    void findByChannel_NotFound() {
      // given
      when(channelRepository.existsById(channelId))
          .thenReturn(false);

      // when & then
      assertThatThrownBy(() -> messageService.findAllByChannelId(
          channelId, PageRequest.of(0, 10)))
          .isInstanceOf(ChannelNotFoundException.class);

      verify(channelRepository).existsById(channelId);
      verify(messageRepository, never()).findByChannelId(any(UUID.class), any(Pageable.class));
    }
  }
}