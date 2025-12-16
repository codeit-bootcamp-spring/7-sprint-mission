package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotEmptyException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.basic.MessageServiceImpl;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(SpringExtension.class)
@DisplayName("MessageService Test")
class MessageServiceTest {

    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private MessageRepository messageRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Nested
    @DisplayName("메시지를 생성할 수 있다.")
    class CreateMessage {

        @Test
        @DisplayName("메시지 생성 성공")
        void createMessage_Success() {
            // given
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();

            User testUser = new User(null, null, null);
            ReflectionTestUtils.setField(testUser, "id", userId);
            UserDto userDto = new UserDto(userId, null, null, null, null);

            Channel testChannel = new Channel();
            ReflectionTestUtils.setField(testChannel, "id", channelId);

            MessageCreateRequest request = MessageCreateRequest.builder()
                    .authorId(userId)
                    .channelId(channelId)
                    .content("test")
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(testUser));
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(testChannel));

            MessageDto result
                    = MessageDto.builder()
                    .content("test")
                    .channelId(channelId)
                    .author(userDto)
                    .build();
            when(messageMapper.toDto(any(Message.class))).thenReturn(result);

            // when
            MessageDto message = messageService.createMessage(request, null);

            // then
            assertThat(message.author()).isEqualTo(userDto);
            assertThat(message.channelId()).isEqualTo(channelId);
            assertThat(message.content()).isEqualTo("test");

            verify(userRepository, times(1)).findById(userId);
            verify(channelRepository, times(1)).findById(channelId);
            verify(messageRepository, times(1)).save(any(Message.class));
            verify(messageMapper, times(1)).toDto(any(Message.class));
            verify(binaryContentRepository, never()).save(any());
            verify(binaryContentStorage, never()).put(any(), any());
        }

        @Test
        @DisplayName("메시지 생성 실패 - 공백을 보낼 수 없음")
        void createMessage_NotEmptyException() {
            // given
            when(userRepository.findById(any())).thenReturn(Optional.empty());
            MessageCreateRequest request = MessageCreateRequest.builder().build();
            // when & then
            assertThatThrownBy(() -> messageService.createMessage(request, null))
                    .isInstanceOf(MessageNotEmptyException.class)
                    .hasMessage("메시지는 비어있을 수 없습니다.");

            verify(messageRepository, never()).save(any(Message.class));
        }

        @Test
        @DisplayName("메시지 생성 실패 - 사용자를 찾을 수 없음")
        void createMessage_NotFoundAuthor() {
            // given
            UUID userId = UUID.randomUUID();
            MessageCreateRequest request = MessageCreateRequest.builder()
                    .authorId(userId)
                    .channelId(UUID.randomUUID())
                    .content("test").build();
            when(userRepository.findById(userId)).thenReturn(Optional.empty());
            // when & then
            assertThatThrownBy(() -> messageService.createMessage(request, null))
                    .isInstanceOf(UserNotFoundException.class)
                    .hasMessage("사용자를 찾을 수 없습니다.");

            verify(userRepository, times(1)).findById(any());
            verify(messageRepository, never()).save(any(Message.class));
        }

        @Test
        @DisplayName("메시지 생성 실패 - 채널을 찾을 수 없음")
        void createMessage_NotFoundChannel() {
            // given
            UUID userId = UUID.randomUUID();
            User newUser = new User(null, null, null);
            when(userRepository.findById(userId)).thenReturn(Optional.of(newUser));

            UUID channelId = UUID.randomUUID();
            MessageCreateRequest request = MessageCreateRequest.builder()
                    .authorId(userId)
                    .channelId(channelId)
                    .content("test")
                    .build();
            when(channelRepository.findById(channelId)).thenReturn(Optional.empty());
            // when & then
            assertThatThrownBy(() -> messageService.createMessage(request, null))
                    .isInstanceOf(ChannelNotFoundException.class)
                    .hasMessage("채널을 찾을 수 없습니다.");

            verify(channelRepository, times(1)).findById(any());
            verify(messageRepository, never()).save(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메시지를 수정할 수 있다.")
    class UpdateMessage {

        @Test
        @DisplayName("메시지 수정 성공")
        void updateMessage_Success() {
            // given
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();
            UUID messageId = UUID.randomUUID();

            Message testMessage = new Message("old", null, null, null);
            ReflectionTestUtils.setField(testMessage, "id", messageId);
            when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));

            MessageUpdateRequest request = MessageUpdateRequest.builder()
                    .newContent("new").build();

            UserDto userDto = UserDto.builder().id(userId).build();
            MessageDto result = MessageDto.builder()
                    .id(messageId)
                    .content("new")
                    .author(userDto)
                    .channelId(channelId)
                    .build();
            when(messageMapper.toDto(any(Message.class))).thenReturn(result);

            // when
            MessageDto message = messageService.updateMessage(messageId, request);

            // then
            assertThat(testMessage.getContent()).isEqualTo("new");
            assertThat(message.content()).isEqualTo("new");

            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, times(1)).save(any(Message.class));
        }

        @Test
        @DisplayName("메시지 수정 실패 - 메시지를 찾을 수 없음")
        void updateMessage_NotFound_ThrowExcemption() {
            // given
            UUID messageId = UUID.randomUUID();
            when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.updateMessage(messageId, null))
                    .isInstanceOf(MessageNotFoundException.class)
                    .hasMessage("메시지를 찾을 수 없습니다.");
            verify(messageRepository, times(1)).findById(messageId);
        }

        @Test
        @DisplayName("메시지 수정 실패 - 공백을 보낼 수 없음")
        void updateMessage_NotEmptyException() {
            // given
            UUID messageId = UUID.randomUUID();

            Message testMessage = new Message("test", null, null, null);
            ReflectionTestUtils.setField(testMessage, "id", messageId);
            when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));

            MessageUpdateRequest request = new MessageUpdateRequest("");

            // when & then
            assertThatThrownBy(() -> messageService.updateMessage(messageId, request))
                    .isInstanceOf(MessageNotEmptyException.class)
                    .hasMessage("메시지는 비어있을 수 없습니다.");
            assertThat(testMessage.getContent()).isEqualTo("test");

            verify(messageRepository, never()).save(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메시지를 삭제할 수 있다.")
    class DeleteMessage {

        @Test
        @DisplayName("메시지 삭제 성공")
        void deleteMessage_Success() {
            // given
            UUID messageId = UUID.randomUUID();
            Message testMessage = new Message("old", null, null, null);
            ReflectionTestUtils.setField(testMessage, "id", messageId);
            when(messageRepository.findById(messageId)).thenReturn(Optional.of(testMessage));

            // when
            messageService.deleteMessage(messageId);

            // then
            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, times(1)).deleteById(messageId);
        }

        @Test
        @DisplayName("메시지 삭제 실패 - 메시지를 찾을 수 없음")
        void deleteMessage_NotFound_ThrowException() {
            // given
            UUID messageId = UUID.randomUUID();
            when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.deleteMessage(messageId))
                    .isInstanceOf(MessageNotFoundException.class)
                    .hasMessage("메시지를 찾을 수 없습니다.");
            verify(messageRepository, times(1)).findById(messageId);

        }
    }
}