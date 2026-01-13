package com.sprint.mission.discodeit.unit.service;

import com.sprint.mission.discodeit.dto.message.MessageResponseDto;
import com.sprint.mission.discodeit.dto.message.MessageSendCommand;
import com.sprint.mission.discodeit.dto.message.MessageUpdateCommand;
import com.sprint.mission.discodeit.dto.message.MessageUpdateResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponse;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.type.ChannelType;
import com.sprint.mission.discodeit.exception.DiscodeitException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.exception.message.MessageSendNotAllowed;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.ReadStatusRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.reader.ChannelReader;
import com.sprint.mission.discodeit.service.reader.MessageReader;
import com.sprint.mission.discodeit.service.reader.UserReader;
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

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.eq;
import static org.mockito.BDDMockito.given;
import static org.mockito.BDDMockito.then;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.never;

@ExtendWith(MockitoExtension.class)
public class MessageServiceUnitTest {
    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserReader userReader;

    @Mock
    private ChannelReader channelReader;

    @Mock
    private MessageReader messageReader;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentService binaryContentService;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @Mock
    private MessageMapper messageMapper;

    @InjectMocks
    private BasicMessageService messageService;

    @Nested
    @DisplayName("sendMessage")
    class sendMessage {

        @Test
        @DisplayName("[Behavior + Branch][Positive] 메세지 전송 - 메세지 전송 성공")
        void sendMessage_shouldDelegateToBinaryContentRepositoryAndMessageRepository() {

            // given
            UUID authorId = UUID.randomUUID();
            UUID channelId = UUID.randomUUID();
            MessageSendCommand command = mock(MessageSendCommand.class);
            User user = mock(User.class);
            Channel channel = mock(Channel.class);
            Message message = mock(Message.class);
            MessageResponseDto responseDto = mock(MessageResponseDto.class);

            given(command.senderId()).willReturn(authorId);
            given(command.channelId()).willReturn(channelId);
            given(command.content()).willReturn("content");
            given(command.profiles()).willReturn(List.of());

            given(userReader.findUserOrThrow(command.senderId())).willReturn(user);
            given(channelReader.findChannelOrThrow(channelId)).willReturn(channel);
            given(channel.getType()).willReturn(ChannelType.PUBLIC);
            given(binaryContentRepository.findAllById(any())).willReturn(List.of());
            given(messageRepository.save(any())).willReturn(message);
            given(messageMapper.toDto(message)).willReturn(responseDto);

            // when
            MessageResponseDto result = messageService.sendMessageToChannel(command);

            // then
            assertSame(responseDto, result);
            then(userReader).should().findUserOrThrow(authorId);
            then(channelReader).should().findChannelOrThrow(channelId);
            then(binaryContentService).shouldHaveNoInteractions();
            then(messageRepository).should().save(any());
            then(messageMapper).should().toDto(message);
        }

        @Test
        @DisplayName("[Negative] 메세지 전송 - 채널 맴버가 아닐때 MessageSendNotAllowed 전파")
        void sendMessage_shouldThrowMessageSendNotAllowedException() {

            // given
            Channel channel = mock(Channel.class);
            User user = mock(User.class);
            MessageSendCommand command = mock(MessageSendCommand.class);
            given(userReader.findUserOrThrow(any())).willReturn(user);
            given(channelReader.findChannelOrThrow(any())).willReturn(channel);
            given(channel.getType()).willReturn(ChannelType.PRIVATE);
            given(channel.getId()).willReturn(UUID.randomUUID());
            given(user.getId()).willReturn(UUID.randomUUID());
            given(command.content()).willReturn("content");
            given(readStatusRepository.existsByUserIdAndChannelId(
                    user.getId(),
                    channel.getId()
            )).willReturn(false);

            // when & then
            MessageSendNotAllowed ex = assertThrows(MessageSendNotAllowed.class, () -> messageService.sendMessageToChannel(command));

            assertEquals(ErrorCode.MESSAGE_SEND_NOT_ALLOWED, ex.getErrorCode());
            then(userReader).should().findUserOrThrow(any());
            then(channelReader).should().findChannelOrThrow(any());


        }
    }

    @Nested
    @DisplayName("updateMessage")
    class UpdateMessage {
        @Test
        @DisplayName("[Behavior + Branch][Positive] 메세지 수정 - 메세지 수정(위임) 성공 ")
        void updateMessage_shouldDelegateUpdateContent() {
            // given
            Message message = mock(Message.class);
            UUID messageId = UUID.randomUUID();
            MessageUpdateCommand command = new MessageUpdateCommand(messageId, "content");
            MessageUpdateResponseDto responseDto = mock(MessageUpdateResponseDto.class);

            given(messageReader.findMessageOrThrow(command.messageId())).willReturn(message);
            given(message.updateContent(command.content())).willReturn(true);
            given(messageMapper.toUpdateDto(message)).willReturn(responseDto);

            // when
            MessageUpdateResponseDto result = messageService.updateMessage(command);

            // then
            assertSame(responseDto, result);
            then(messageReader).should().findMessageOrThrow(command.messageId());
            then(message).should().updateContent(command.content());
            then(messageMapper).should().toUpdateDto(message);
        }

        @Test
        @DisplayName("[Branch][Positive] 메시지 수정 - 같은 내용이면 updateContent=false여도 DTO 반환")
        void updateMessage_shouldReturnDto_evenWhenNoChange() {
            // given
            UUID messageId = UUID.randomUUID();
            MessageUpdateCommand command = new MessageUpdateCommand(messageId, "content");

            Message message = mock(Message.class);
            MessageUpdateResponseDto responseDto = mock(MessageUpdateResponseDto.class);

            given(messageReader.findMessageOrThrow(messageId)).willReturn(message);
            given(message.updateContent("content")).willReturn(false);
            given(messageMapper.toUpdateDto(message)).willReturn(responseDto);

            // when
            MessageUpdateResponseDto result = messageService.updateMessage(command);

            // then
            assertSame(responseDto, result);
            then(messageReader).should().findMessageOrThrow(messageId);
            then(message).should().updateContent("content");
            then(messageMapper).should().toUpdateDto(message);
        }

        @Test
        @DisplayName("[Exception][Negative] 메세지 수정 - messageId가 null일때 INVALID_INPUT 예외전파")
        void updateMessage_shouldThrows_whenMessageIdIsNull() {
            // given
            MessageUpdateCommand command = new MessageUpdateCommand(null, "content");

            // when & then
            DiscodeitException ex = assertThrows(DiscodeitException.class, () -> messageService.updateMessage(command));

            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());

            then(messageReader).shouldHaveNoInteractions();
            then(messageMapper).shouldHaveNoInteractions();

        }

        @Test
        @DisplayName("[Exception][Negative] 메세지 수정 - content가 null일때 INVALID_INPUT 예외전파")
        void shouldThrow_whenContentNull() {
            // given
            UUID messageId = UUID.randomUUID();
            MessageUpdateCommand command = new MessageUpdateCommand(messageId, null);

            // when & then
            DiscodeitException ex = assertThrows(DiscodeitException.class, () -> messageService.updateMessage(command));

            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());

            then(messageReader).shouldHaveNoInteractions();
            then(messageMapper).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("[Exception][Negative] 메세지 수정 - content가 Blank일때 INVALID_INPUT 예외전파")
        void shouldThrow_whenContentBlank() {
            // given
            UUID messageId = UUID.randomUUID();
            MessageUpdateCommand command = new MessageUpdateCommand(messageId, " ");

            // when & then
            DiscodeitException ex = assertThrows(DiscodeitException.class, () -> messageService.updateMessage(command));

            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());

            then(messageReader).shouldHaveNoInteractions();
            then(messageMapper).shouldHaveNoInteractions();
        }
    }

    @Nested
    @DisplayName("deleteMessage")
    class DeleteMessage {
        @Test
        @DisplayName("[Behavior][Positive] 메세지 삭제 - messageRepository.deleteById 위임")
        void deleteMessage_shouldDeleteMessage_whenValidMessageId(){
            // given
            UUID messageId = UUID.randomUUID();
            Message message = mock(Message.class);
            given(messageReader.findMessageOrThrow(messageId)).willReturn(message);
            given(message.getId()).willReturn(messageId);

            // when
            messageService.deleteMessage(messageId);

            // then
            then(messageReader).should().findMessageOrThrow(messageId);
            then(messageRepository).should().deleteById(message.getId());
        }

        @Test
        @DisplayName("[Exception][Negative] 메세지 삭제 - messageId 가 null이면 INVALID_INPUT 예외 전파")
        void deleteMessage_shouldThrows_WhenMessageIdIsNull(){
            // given
            UUID messageId = null;

            // when & then
            DiscodeitException ex = assertThrows(DiscodeitException.class, () -> messageService.deleteMessage(messageId));

            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());
        }
    }

    @Nested
    @DisplayName("getAllMessagesByChannelId")
    class getAllMessagesByChannelId {
        @Test
        @DisplayName("[Exception][Negative] 메시지 목록 조회 - channelId null이면 INVALID_INPUT 예외")
        void getAllMessagesByChannelId_shouldThrow_whenChannelIdNull() {
            // given
            Pageable pageable = PageRequest.of(0, 20);
            Instant cursor = Instant.now();

            // when & then
            DiscodeitException ex = assertThrows(
                    DiscodeitException.class,
                    () -> messageService.getAllMessagesByChannelId(null, pageable, cursor)
            );

            assertEquals(ErrorCode.INVALID_INPUT, ex.getErrorCode());

            then(channelReader).shouldHaveNoInteractions();
            then(messageRepository).shouldHaveNoInteractions();
            then(messageMapper).shouldHaveNoInteractions();
        }

        @Test
        @DisplayName("[Behavior][Positive] 메시지 목록 조회 - repository 조회 + DTO 변환 + nextCursor 계산")
        void getAllMessagesByChannelId_shouldReturnPageResponse_andComputeNextCursor() {
            // given
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 2);
            Instant cursor = Instant.parse("2025-01-01T00:00:00Z");

            Channel channel = mock(Channel.class);
            given(channelReader.findChannelOrThrow(channelId)).willReturn(channel);
            given(channel.getId()).willReturn(channelId);

            // repository가 반환할 Message Slice (엔티티)
            Message m1 = mock(Message.class);
            Message m2 = mock(Message.class);
            Slice<Message> messageSlice = new SliceImpl<>(List.of(m1, m2), pageable, true);

            given(messageRepository.findAllByChannelId(channelId, pageable, cursor))
                    .willReturn(messageSlice);

            // mapper 결과 DTO createdAt 세팅
            MessageResponseDto d1 = mock(MessageResponseDto.class);
            MessageResponseDto d2 = mock(MessageResponseDto.class);

            Instant t2 = Instant.parse("2025-01-01T11:00:00Z");

            given(messageMapper.toDto(m1)).willReturn(d1);
            given(messageMapper.toDto(m2)).willReturn(d2);
            given(d2.createdAt()).willReturn(t2);

            // when
            PageResponse<MessageResponseDto> result =
                    messageService.getAllMessagesByChannelId(channelId, pageable, cursor);

            // then: 위임 검증
            then(channelReader).should().findChannelOrThrow(channelId);
            then(messageRepository).should().findAllByChannelId(channelId, pageable, cursor);
            then(messageMapper).should().toDto(m1);
            then(messageMapper).should().toDto(m2);
        }

        @Test
        @DisplayName("[Branch][Positive] cursor가 null이면 Instant.now()로 대체해서 repo에 전달")
        void getAllMessagesByChannelId_shouldUseNow_whenCursorNull() {
            // given
            UUID channelId = UUID.randomUUID();
            Pageable pageable = PageRequest.of(0, 10);

            Channel channel = mock(Channel.class);
            given(channelReader.findChannelOrThrow(channelId)).willReturn(channel);
            given(channel.getId()).willReturn(channelId);

            Slice<Message> emptySlice = new SliceImpl<>(List.of(), pageable, false);

            // ArgumentCaptor로 cursor가 null이 아닌 값으로 전달되는지 확인(Optional.ofNullable(cursor).orElse(Instant.now())이 코드)
            ArgumentCaptor<Instant> cursorCaptor = ArgumentCaptor.forClass(Instant.class);

            given(messageRepository.findAllByChannelId(eq(channelId), eq(pageable), any(Instant.class)))
                    .willReturn(emptySlice);

            // when
            messageService.getAllMessagesByChannelId(channelId, pageable, null);

            // then
            then(messageRepository).should().findAllByChannelId(eq(channelId), eq(pageable), cursorCaptor.capture());
            assertNotNull(cursorCaptor.getValue()); // now로 대체됐는지 최소 검증
        }
    }

}
