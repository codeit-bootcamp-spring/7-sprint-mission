package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContentDto.BinaryContentDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.messageDto.MessageDto;
import com.sprint.mission.discodeit.dto.messageDto.MessageUpdateRequest;
import com.sprint.mission.discodeit.dto.userDto.UserDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.binaryContent.FileOperationFailedException;
import com.sprint.mission.discodeit.exception.binaryContent.FileUploadLimitExceedException;
import com.sprint.mission.discodeit.exception.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.exception.message.MessageNotEmptyException;
import com.sprint.mission.discodeit.exception.message.MessageNotFoundException;
import com.sprint.mission.discodeit.exception.user.UserNotFoundException;
import com.sprint.mission.discodeit.fixture.ChannelFixture;
import com.sprint.mission.discodeit.fixture.MessageFixture;
import com.sprint.mission.discodeit.fixture.UserFixture;
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
import org.junit.jupiter.params.ParameterizedTest;
import org.junit.jupiter.params.provider.Arguments;
import org.junit.jupiter.params.provider.CsvSource;
import org.junit.jupiter.params.provider.MethodSource;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.springframework.test.context.junit.jupiter.SpringExtension;
import org.springframework.test.util.ReflectionTestUtils;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

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
    @DisplayName("메시지 생성")
    class CreateMessage {

        static Stream<List<MultipartFile>> attachments() {
            return Stream.of(
                    null,
                    List.of()
            );
        }

        @ParameterizedTest
        @MethodSource("attachments")
        @DisplayName("""
                메시지를 생성할 수 있다.
                내용은 있지만, 첨부파일은 없거나 null이다.
                """)
        void messageCreate_Success_WithContent(List<MultipartFile> files) {
            // given
            MessageCreateRequest request = MessageFixture.getMessageRequest(1);
            MessageDto dto = MessageFixture.getMessageDto(1);

            User user = UserFixture.getUser(1);
            Channel channel = ChannelFixture.getPublicChannel(1);

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(channelRepository.findById(any())).thenReturn(Optional.of(channel));
            when(messageMapper.toDto(any(Message.class))).thenReturn(dto);

            // when
            MessageDto response = messageService.createMessage(request, files);

            // then
            verify(messageRepository, times(1)).save(any(Message.class));
            verify(messageMapper, times(1)).toDto(any(Message.class));
            verify(binaryContentRepository, never()).save(any());
            assertThat(response).isEqualTo(dto);
        }

        @ParameterizedTest
        @CsvSource(value = {
                "''",
                "' '",
                "null"
        }, nullValues = "null")
        @DisplayName("""
                메시지를 생성할 수 있다.
                첨부파일은 있지만, 내용이 비어있거나 null이다.
                """)
        void createMessage_Success_WithAttachment(String content) {
            // given
            User user = UserFixture.getUser(1);
            UUID userId = user.getId();
            UserDto userDto = UserDto.builder().id(userId).build();

            UUID channelId = UUID.randomUUID();

            MessageCreateRequest request = MessageCreateRequest.builder()
                    .content(content)
                    .authorId(userId)
                    .channelId(channelId)
                    .build();

            MultipartFile multipartFile = mock(MultipartFile.class);
            List<MultipartFile> files = List.of(multipartFile);

            BinaryContent attachment = BinaryContent.builder().build();
            List<BinaryContentDto> attachments
                    = List.of(new BinaryContentDto(null, null, null, null));
            when(binaryContentRepository.save(any())).thenReturn(attachment);

            MessageDto dto = MessageDto.builder()
                    .content(content)
                    .author(userDto)
                    .channelId(channelId)
                    .attachments(attachments)
                    .build();

            when(userRepository.findById(userId)).thenReturn(Optional.of(user));
            when(channelRepository.findById(channelId)).thenReturn(Optional.of(ChannelFixture.getPublicChannel(1)));
            when(messageMapper.toDto(any(Message.class))).thenReturn(dto);

            // when
            MessageDto response = messageService.createMessage(request, files);

            // then
            verify(messageRepository, times(1)).save(any(Message.class));
            verify(messageMapper, times(1)).toDto(any(Message.class));
            verify(binaryContentRepository, times(1)).save(any(BinaryContent.class));

            assertThat(response).isEqualTo(dto);
        }

        static Stream<Arguments> emptyContentAndAttachment() {
            return Stream.of(
                    Arguments.of("", List.of()),
                    Arguments.of("", null),

                    Arguments.of(" ", List.of()),
                    Arguments.of(" ", null),

                    Arguments.of(null, List.of()),
                    Arguments.of(null, null)
            );
        }

        @ParameterizedTest
        @MethodSource("emptyContentAndAttachment")
        @DisplayName("""
                메시지 생성 시,
                내용이 비어있거나 null이고,
                첨부파일이 비어있거나 null이라면
                예외를 반환한다.
                """)
        void messageCreate_Fail_EmptyContentAndAttachment(String content, List<MultipartFile> files) {
            // given
            User user = UserFixture.getUser(1);
            Channel channel = ChannelFixture.getPublicChannel(1);
            MessageCreateRequest request = MessageCreateRequest.builder()
                    .content(content)
                    .authorId(user.getId())
                    .channelId(channel.getId())
                    .build();
            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(channelRepository.findById(any())).thenReturn(Optional.of(channel));

            // when & then
            assertThatThrownBy(() -> messageService.createMessage(request, files))
                    .isInstanceOf(MessageNotEmptyException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MESSAGE_NOT_EMPTY);

            verify(messageRepository, never()).save(any(Message.class));
            verify(binaryContentRepository, never()).save(any());
            verify(messageMapper, never()).toDto(any(Message.class));
        }

        @Test
        @DisplayName("존재하지 않는 유저Id면 예외를 반환한다.")
        void messageCreate_Fail_NotFoundAuthor() {
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
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.USER_NOT_FOUND);

            verify(userRepository, times(1)).findById(any());
            verify(messageRepository, never()).save(any(Message.class));
        }

        @Test
        @DisplayName("존재하지 않는 채널Id면 예외를 반환한다.")
        void messageCreate_Fail_NotFoundChannel() {
            // given
            UUID userId = UUID.randomUUID();
            User user = UserFixture.getUser(1);
            when(userRepository.findById(userId)).thenReturn(Optional.of(user));

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
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.CHANNEL_NOT_FOUND);

            verify(userRepository, times(1)).findById(any());
            verify(channelRepository, times(1)).findById(any());
            verify(messageRepository, never()).save(any(Message.class));
        }

        @Test
        @DisplayName("""
                첨부파일은 한번에 10개까지 보낼 수 있다.
                10개를 넘으면 예외를 반환한다.
                """)
        void messageCreate_Fail_TooManyAttachment() {
            // given
            User user = UserFixture.getUser(1);
            Channel channel = ChannelFixture.getPublicChannel(1);
            MessageCreateRequest request = MessageFixture.getMessageRequest(1);

            List<MultipartFile> files = new ArrayList<>();
            for (int i = 0; i < 11; i++) {
                files.add(mock(MultipartFile.class));
            }

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(channelRepository.findById(any())).thenReturn(Optional.of(channel));

            // when & then
            assertThatThrownBy(() -> messageService.createMessage(request, files))
                    .isInstanceOf(FileUploadLimitExceedException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FILE_UPLOAD_LIMIT_EXCEED);

            verify(binaryContentRepository, never()).save(any());
            verify(messageRepository, never()).save(any(Message.class));
            verify(messageMapper, never()).toDto(any(Message.class));
        }

        @Test
        @DisplayName("첨부파일 데이터 읽기 실패 시 예외를 반환한다.")
        void messageCreate_Fail_BinaryContentReadError() throws IOException {
            // given
            User user = UserFixture.getUser(1);
            Channel channel = ChannelFixture.getPublicChannel(1);
            MessageCreateRequest request = MessageFixture.getMessageRequest(1);

            when(userRepository.findById(any())).thenReturn(Optional.of(user));
            when(channelRepository.findById(any())).thenReturn(Optional.of(channel));

            MultipartFile multipartFile = mock(MultipartFile.class);
            BinaryContent binaryContent = BinaryContent.builder().build();
            ReflectionTestUtils.setField(binaryContent, "id", UUID.randomUUID());

            when(binaryContentRepository.save(any())).thenReturn(binaryContent);
            when(multipartFile.getBytes()).thenThrow(new IOException("예외 강제 발생"));

            // when & then
            assertThatThrownBy(() -> messageService.createMessage(request, List.of(multipartFile)))
                    .isInstanceOf(FileOperationFailedException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.FILE_OPERATION_FAILED);
        }
    }

    @Nested
    @DisplayName("메시지 수정")
    class UpdateMessage {

        @Test
        @DisplayName("메시지를 수정할 수 있다.")
        void messageUpdate_Success() {
            // given
            Message message = MessageFixture.getMessage(1);
            MessageUpdateRequest request = new MessageUpdateRequest("new");
            MessageDto dto = MessageDto.builder()
                    .content("new")
                    .build();

            when(messageRepository.findById(any())).thenReturn(Optional.of(message));
            when(messageMapper.toDto(any(Message.class))).thenReturn(dto);

            // when
            MessageDto response = messageService.updateMessage(message.getId(), request);

            // then
            assertThat(response.content()).isEqualTo(dto.content()).isEqualTo("new");

            verify(messageRepository, times(1)).findById(any());
            verify(messageRepository, times(1)).save(any(Message.class));
            verify(messageMapper, times(1)).toDto(any(Message.class));
        }

        @Test
        @DisplayName("존재하지 않는 메시지Id면 예외를 반환한다.")
        void messageUpdate_Fail_NotFound() {
            // given
            UUID messageId = UUID.randomUUID();
            MessageUpdateRequest request = new MessageUpdateRequest("new");
            when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.updateMessage(messageId, request))
                    .isInstanceOf(MessageNotFoundException.class)
                    .hasMessage("메시지를 찾을 수 없습니다.");
            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, never()).save(any(Message.class));
        }

        @ParameterizedTest
        @CsvSource(value = {
                "''",
                "' '",
                "null"
        }, nullValues = "null")
        @DisplayName("메시지 수정 내용에 공백이나 null이 들어가면 예외를 반환한다.")
        void messageUpdate_Fail_NotEmptyOrNull(String content) {
            // given
            Message message = MessageFixture.getMessage(1);
            UUID messageId = message.getId();
            MessageUpdateRequest request = new MessageUpdateRequest(content);

            when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

            // when & then
            assertThatThrownBy(() -> messageService.updateMessage(messageId, request))
                    .isInstanceOf(MessageNotEmptyException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MESSAGE_NOT_EMPTY);

            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, never()).save(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메시지를 삭제할 수 있다.")
    class DeleteMessage {

        @Test
        @DisplayName("메시지 삭제 성공")
        void messageDelete_Success() {
            // given
            Message message = MessageFixture.getMessage(1);
            UUID messageId = message.getId();
            when(messageRepository.findById(messageId)).thenReturn(Optional.of(message));

            // when
            messageService.deleteMessage(messageId);

            // then
            verify(messageRepository, times(1)).findById(messageId);
            verify(messageRepository, times(1)).deleteById(messageId);
        }

        @Test
        @DisplayName("존재하지 않는 메시지Id면 예외를 반환한다.")
        void deleteMessage_NotFound_ThrowException() {
            // given
            UUID messageId = UUID.randomUUID();
            when(messageRepository.findById(messageId)).thenReturn(Optional.empty());

            // when & then
            assertThatThrownBy(() -> messageService.deleteMessage(messageId))
                    .isInstanceOf(MessageNotFoundException.class)
                    .extracting("errorCode")
                    .isEqualTo(ErrorCode.MESSAGE_NOT_FOUND);

            verify(messageRepository, times(1)).findById(messageId);
        }
    }
}