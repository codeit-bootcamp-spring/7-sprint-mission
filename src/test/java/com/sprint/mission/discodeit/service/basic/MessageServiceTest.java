package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.global.exception.channel.NotChannelMemberException;
import com.sprint.mission.discodeit.global.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.SliceImpl;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.util.ReflectionTestUtils;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;
import static org.assertj.core.api.AssertionsForClassTypes.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;

@ExtendWith(MockitoExtension.class)
@DisplayName("메시지 서비스 단위 테스트")
class MessageServiceTest {

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @InjectMocks
    private BasicMessageService messageService;

    private MockMultipartFile mockFile() {
        return new MockMultipartFile(
                "file",
                "profile.png",
                "image/png",
                "dummy image".getBytes()
        );
    }

    @Nested
    @DisplayName("메시지 생성 테스트")
    class CreateMessageTest {

        @Test
        @DisplayName("정상적으로 메시지를 생성할 수 있다")
        void createMessage_Success() throws IOException {
            // given
            String content = "content";
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();
            MockMultipartFile mockFile = mockFile();

            User user = new User(
                    "username",
                    "email@kakao.com",
                    "password",
                    null
            );
            ReflectionTestUtils.setField(user, "id", userId);

            Channel channel = new Channel(
              "channel",
                    ChannelType.PRIVATE,
                    "description"
            );
            ReflectionTestUtils.setField(channel, "id", channelId);

            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));

            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(channel));

            when(readStatusRepository.existsByUserIdAndChannelId(userId, channelId))
                    .thenReturn(true);

            when(binaryContentRepository.save(any(BinaryContent.class)))
                    .thenAnswer(invocation -> {
                        BinaryContent bc = invocation.getArgument(0);
                        ReflectionTestUtils.setField(bc, "id", UUID.randomUUID());
                        return bc;
                    });

            when(binaryContentStorage.put(any(UUID.class), any(byte[].class)))
                    .thenReturn(UUID.randomUUID());

            when(messageRepository.save(any(Message.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(messageMapper.toResponseDto(any(Message.class)))
                    .thenAnswer(invocation -> {
                        Message message = invocation.getArgument(0);
                        return new MessageResponseDto(
                                message.getId(),
                                message.getCreatedAt(),
                                message.getUpdatedAt(),
                                message.getContent(),
                                message.getChannel().getId(),
                                null,
                                null
                        );
                    });

            // RequestDto 생성
            CreateMessageRequestDto request =
                    new CreateMessageRequestDto(
                            content,
                            channelId,
                            userId
                    );

            List<CreateBinaryContentRequestDto> attachments = new ArrayList<>();

            for(int i = 0; i < 5; i++) {
                attachments.add(new CreateBinaryContentRequestDto(
                        mockFile.getOriginalFilename(),
                        mockFile.getSize(),
                        mockFile.getContentType(),
                        mockFile.getBytes()
                ));
            }

            // when
            MessageResponseDto response = messageService.create(request, attachments);

            // then
            assertThat(response).isNotNull();

            verify(userRepository, times(1)).findById(userId);
            verify(channelRepository, times(1)).findById(channelId);
            verify(readStatusRepository, times(1)).existsByUserIdAndChannelId(userId, channelId);
            verify(binaryContentRepository, times(attachments.size())).save(any(BinaryContent.class));
            verify(binaryContentStorage, times(attachments.size())).put(any(UUID.class), any(byte[].class));
            verify(messageMapper, times(1)).toResponseDto(any(Message.class));
        }

        @Test
        @DisplayName("비공개 채널은 참여하고 있는 사용자만 메시지를 생성할 수 있다")
        void createMessage_NotChannelMember() {
            // given
            String content = "content";
            UUID userId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();
            MockMultipartFile mockFile = mockFile();

            User user = new User(
                    "username",
                    "email@kakao.com",
                    "password",
                    null
            );
            ReflectionTestUtils.setField(user, "id", userId);

            Channel channel = new Channel(
                    "channel",
                    ChannelType.PRIVATE,
                    "description"
            );
            ReflectionTestUtils.setField(channel, "id", channelId);

            when(userRepository.findById(userId))
                    .thenReturn(Optional.of(user));

            when(channelRepository.findById(channelId))
                    .thenReturn(Optional.of(channel));

            when(readStatusRepository.existsByUserIdAndChannelId(userId, channelId))
                    .thenReturn(false);

            // RequestDto 생성
            CreateMessageRequestDto request =
                    new CreateMessageRequestDto(
                            content,
                            channelId,
                            userId
                    );

            List<CreateBinaryContentRequestDto> attachments = new ArrayList<>();

            // when & then
            assertThatThrownBy(() -> messageService.create(request, attachments))
                    .isInstanceOf(NotChannelMemberException.class);

            verify(userRepository, times(1)).findById(userId);
            verify(channelRepository, times(1)).findById(channelId);
            verify(readStatusRepository, times(1)).existsByUserIdAndChannelId(userId, channelId);
            verify(binaryContentRepository, never()).save(any(BinaryContent.class));
            verify(binaryContentStorage, never()).put(any(UUID.class), any(byte[].class));
            verify(messageMapper, never()).toResponseDto(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메시지 수정 테스트")
    class UpdateMessageTest {

        @Test
        @DisplayName("정상적으로 메시지를 수정할 수 있다")
        void updateMessage_Success() {
            // given

            UUID messageId = UUID.randomUUID();
            String newContent = "newContent";

            Channel channel = new Channel(
                    "name",
                    ChannelType.PRIVATE,
                    "description"
            );

            Message message = new Message(
                    "content",
                    channel,
                    null,
                    new ArrayList<>()
            );

            ArgumentCaptor<Message> messageCaptor = ArgumentCaptor.forClass(Message.class);

            when(messageRepository.findById(messageId))
                    .thenReturn(Optional.of(message));

            when(messageRepository.save(any(Message.class)))
                    .thenAnswer(invocation -> invocation.getArgument(0));

            when(messageMapper.toResponseDto(any(Message.class)))
                    .thenAnswer(invocation -> {
                        Message m = invocation.getArgument(0);
                        return new MessageResponseDto(
                                m.getId(),
                                m.getCreatedAt(),
                                m.getUpdatedAt(),
                                m.getContent(),
                                m.getChannel().getId(),
                                null,
                                null
                        );
                    });

            UpdateMessageRequestDto request = new UpdateMessageRequestDto(
                    newContent
            );

            // when
            MessageResponseDto response = messageService.update(messageId, request);

            // then
            assertThat(response).isNotNull();

            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, times(1)).save(messageCaptor.capture());
            verify(messageMapper, times(1)).toResponseDto(any(Message.class));

            Message captorMessage = messageCaptor.getValue();

            assertThat(captorMessage.getContent()).isEqualTo(newContent);
        }

        @Test
        @DisplayName("존재하지 않는 메시지를 수정할 수 없다")
        void updateMessage_MessageNotFound() {
            // given

            UUID messageId = UUID.randomUUID();
            String newContent = "newContent";

            Channel channel = new Channel(
                    "name",
                    ChannelType.PRIVATE,
                    "description"
            );

            Message message = new Message(
                    "content",
                    channel,
                    null,
                    new ArrayList<>()
            );

            when(messageRepository.findById(messageId))
                    .thenThrow(MessageNotFoundException.class);

            UpdateMessageRequestDto request = new UpdateMessageRequestDto(
                    newContent
            );

            // when & then
            assertThatThrownBy(() -> messageService.update(messageId, request))
                    .isInstanceOf(MessageNotFoundException.class);

            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, never()).save(any(Message.class));
            verify(messageMapper, never()).toResponseDto(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메시지 삭제 테스트")
    class DeleteMessageTest {
        @Test
        @DisplayName("정상적으로 메시지를 삭제할 수 있다")
        void deleteMessage_Success() {
            // given

            UUID messageId = UUID.randomUUID();

            Message message = new Message(
                    "content",
                    null,
                    null,
                    new ArrayList<>()
            );

            List<UUID> attachmentIds = message.getAttachments().stream()
                    .map(attachment -> attachment.getId())
                    .toList();

            when(messageRepository.findById(messageId))
                    .thenReturn(Optional.of(message));

            doNothing().when(binaryContentRepository).deleteByIdIn(attachmentIds);
            doNothing().when(messageRepository).deleteById(messageId);

            // when
            messageService.delete(messageId);

            // then
            verify(messageRepository, times(1)).findById(messageId);
            verify(binaryContentRepository, times(1)).deleteByIdIn(anyList());
            verify(messageRepository, times(1)).deleteById(messageId);
        }

        @Test
        @DisplayName("존재하지 않는 메시지는 삭제할 수 없다")
        void deleteMessage_MessageNotFound() {
            // given

            UUID messageId = UUID.randomUUID();

            Message message = new Message(
                    "content",
                    null,
                    null,
                    new ArrayList<>()
            );

            List<UUID> attachmentIds = message.getAttachments().stream()
                    .map(attachment -> attachment.getId())
                    .toList();

            when(messageRepository.findById(messageId))
                    .thenThrow(MessageNotFoundException.class);

            // when & then
            assertThatThrownBy(() -> messageService.delete(messageId))
                    .isInstanceOf(MessageNotFoundException.class);

            verify(messageRepository, times(1)).findById(messageId);
            verify(binaryContentRepository, never()).deleteByIdIn(anyList());
            verify(messageRepository, never()).deleteById(messageId);
        }
    }

    @Nested
    @DisplayName("메시지 조회 테스트")
    class FindMessageTest {
        @Test
        @DisplayName("채널 아이디로 메시지를 조회할 수 있다")
        void findMessage_Success() {
            // given
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);

            List<Message> messages = List.of(
                    new Message("", null, null, null),
                    new Message("", null, null, null)
            );

            Slice<Message> slice = new SliceImpl<>(
                    messages,
                    pageable,
                    true
            );

            when(messageRepository.findAllByChannelId(channelId, pageable))
                    .thenReturn(slice);

            when(messageMapper.toResponseDto(any(Message.class)))
                    .thenAnswer(invocation -> {
                        Message m = invocation.getArgument(0);
                        return new MessageResponseDto(
                                m.getId(),
                                m.getCreatedAt(),
                                m.getUpdatedAt(),
                                m.getContent(),
                                null,
                                null,
                                null
                        );
                    });

            // when
            Slice<MessageResponseDto> response = messageService.findAllByChannelId(channelId, pageable);

            // then
            assertThat(response).isNotNull();

            verify(messageRepository, times(1)).findAllByChannelId(channelId, pageable);
            verify(messageMapper, times(messages.size())).toResponseDto(any(Message.class));
        }
    }
}