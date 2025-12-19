package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.PageResponse;
import com.sprint.mission.discodeit.dto.binaryContent.request.CreateBinaryContentDto;
import com.sprint.mission.discodeit.dto.message.request.CreateMessageDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import com.sprint.mission.discodeit.global.exception.discodietException.channel.ChannelNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.message.MessageNotFoundException;
import com.sprint.mission.discodeit.global.exception.discodietException.user.UserNotFoundException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
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
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("Message Service 테스트")
class BasicMessageServiceTest {

    @Mock
    private MessageRepository messageRepository;
    @Mock
    private BinaryContentRepository binaryContentRepository;
    @Mock
    private UserRepository userRepository;
    @Mock
    private ChannelRepository channelRepository;
    @Mock
    private MessageMapper messageMapper;
    @Mock
    private BinaryContentStorage binaryContentStorage;
    @Mock
    private PageResponseMapper pageResponseMapper;

    @InjectMocks
    BasicMessageService messageService;

    private User author;
    private UserResponseDto userResponseDto;
    private Channel channel;
    private Message message;
    private CreateMessageDto createMessageDto;
    private MessageResponseDto messageResponseDto;
    private UpdateMessageDto updateMessageDto;
    private UpdateMessageDto updateMessageResponseDto;
    private BinaryContent binaryContent1;
    private BinaryContent binaryContent2;
    private CreateBinaryContentDto createBinaryContentDto1;
    private CreateBinaryContentDto createBinaryContentDto2;

    @BeforeEach
    void setUp() {
        author = new User("test", "test@codeit.com", "test_123", null);
        UUID authorId = UUID.randomUUID();
        ReflectionTestUtils.setField(author, "id", authorId);
        userResponseDto = new UserResponseDto(
                author.getId(),
                author.getUsername(),
                author.getEmail(),
                null,
                true
        );
        channel = Channel.builder()
                .type(ChannelType.PUBLIC)
                .name("test_channel")
                .description("test channel description")
                .build();
        UUID channelId = UUID.randomUUID();
        ReflectionTestUtils.setField(channel, "id", channelId);

        createMessageDto = new CreateMessageDto(
                "test message content",
                authorId,
                channelId
        );

        userResponseDto = new UserResponseDto(authorId, author.getUsername(), author.getEmail(), null, true);
    }

    @Nested
    @DisplayName("메세지 생성 테스트")
    class MessageCreate {
        @BeforeEach
        void setUp() {
            binaryContent1 = BinaryContent.builder()
                    .fileName("test.jpg")
                    .size(1024L)
                    .contentType("image/jpeg")
                    .build();
            UUID binaryContentId1 = UUID.randomUUID();
            ReflectionTestUtils.setField(binaryContent1, "id", binaryContentId1);

            createBinaryContentDto1 = new CreateBinaryContentDto(
                    "test.jpg",
                    "image/jpeg",
                    1L,
                    new byte[]{1, 1, 1}
            );

            message = Message.builder()
                    .author(author)
                    .channel(channel)
                    .content(createMessageDto.content())
                    .attachments(List.of(binaryContent1))
                    .build();
            UUID randomId = UUID.randomUUID();
            ReflectionTestUtils.setField(message, "id", randomId);
            ReflectionTestUtils.setField(message, "createdAt", Instant.now());
            ReflectionTestUtils.setField(message, "updatedAt", Instant.now());

            messageResponseDto = new MessageResponseDto(
                    message.getId(),
                    message.getCreatedAt(),
                    message.getUpdatedAt(),
                    message.getContent(),
                    message.getChannel().getId(),
                    userResponseDto,
                    List.of(binaryContent1)
            );
        }

        @Test
        @DisplayName("[정상 케이스] - 메세지 생성 성공")
        void createMessage_withAttachments_success() {
            // given
            given(userRepository.findById(author.getId()))
                    .willReturn(Optional.of(author));
            given(channelRepository.findById(channel.getId()))
                    .willReturn(Optional.of(channel));
            given(binaryContentRepository.save(any(BinaryContent.class)))
                    .willReturn(binaryContent1);


            given(messageRepository.save(any(Message.class)))
                    .willReturn(message);
            given(messageMapper.toResponseDto(any(Message.class)))
                    .willReturn(messageResponseDto);

            // when
            MessageResponseDto result = messageService.createMessage(
                    createMessageDto,
                    List.of(createBinaryContentDto1)
            );

            // then
            assertThat(result).isEqualTo(messageResponseDto);
            assertThat(result.content()).isEqualTo(message.getContent());
            assertThat(result.attachments()).hasSize(1);

            then(userRepository).should().findById(author.getId());
            then(channelRepository).should().findById(channel.getId());
            then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
            then(messageRepository).should().save(any(Message.class));
            then(messageMapper).should().toResponseDto(any(Message.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 유저")
        void createMessage_notFoundUser_fail() {
            // given
            given(userRepository.findById(author.getId()))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> messageService.createMessage(createMessageDto,
                    List.of(createBinaryContentDto1))).isInstanceOf(UserNotFoundException.class);

            then(userRepository).should().findById(author.getId());
            then(channelRepository).should(never()).findById(channel.getId());
            then(binaryContentRepository).should(never()).save(any(BinaryContent.class));
            then(messageRepository).should(never()).save(any(Message.class));
            then(messageMapper).should(never()).toResponseDto(any(Message.class));
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 채널")
        void createMessage_notFoundChannel_fail() {
            // given
            given(userRepository.findById(author.getId()))
                    .willReturn(Optional.of(author));
            given(channelRepository.findById(channel.getId()))
                    .willReturn(Optional.empty());
            // when
            assertThatThrownBy(() -> messageService.createMessage(createMessageDto,
                    List.of(createBinaryContentDto1))).isInstanceOf(ChannelNotFoundException.class);

            then(userRepository).should().findById(author.getId());
            then(channelRepository).should().findById(channel.getId());
            then(binaryContentRepository).should(never()).save(any(BinaryContent.class));
            then(messageRepository).should(never()).save(any(Message.class));
            then(messageMapper).should(never()).toResponseDto(any(Message.class));
        }
    }

    @Nested
    @DisplayName("메세지 조회 테스트")
    class MessageRead {
        private Message message2;
        private MessageResponseDto messageResponseDto2;

        @BeforeEach
        void setUp() {
            message = Message.builder()
                    .author(author)
                    .channel(channel)
                    .content(createMessageDto.content())
                    .attachments(List.of())
                    .build();
            UUID messageId = UUID.randomUUID();
            ReflectionTestUtils.setField(message, "id", messageId);
            ReflectionTestUtils.setField(message, "createdAt", Instant.now());
            ReflectionTestUtils.setField(message, "updatedAt", Instant.now());

            message2 = Message.builder()
                    .author(author)
                    .channel(channel)
                    .content(createMessageDto.content())
                    .attachments(List.of())
                    .build();
            UUID messageId2 = UUID.randomUUID();
            ReflectionTestUtils.setField(message, "id", messageId2);
            ReflectionTestUtils.setField(message, "createdAt", Instant.now());
            ReflectionTestUtils.setField(message, "updatedAt", Instant.now());

            messageResponseDto = new MessageResponseDto(
                    message.getId(),
                    message.getCreatedAt(),
                    message.getUpdatedAt(),
                    message.getContent(),
                    channel.getId(),
                    userResponseDto,
                    List.of()
            );

            messageResponseDto2 = new MessageResponseDto(
                    message2.getId(),
                    message2.getCreatedAt(),
                    message2.getUpdatedAt(),
                    message2.getContent(),
                    channel.getId(),
                    userResponseDto,
                    List.of()
            );
        }

        @Test
        @DisplayName("[정상 케이스] - 메세지 조회 성공")
        void readMessage() {
            // given
            given(messageRepository.findById(message.getId()))
                    .willReturn(Optional.of(message));
            given(messageMapper.toResponseDto(message))
                    .willReturn(messageResponseDto);

            // when
            MessageResponseDto result = messageService.getMessage(message.getId());

            // then
            assertThat(result).isEqualTo(messageResponseDto);
            assertThat(result.content()).isEqualTo(message.getContent());

            then(messageRepository).should().findById(message.getId());
            then(messageMapper).should().toResponseDto(message);
        }

        @Test
        @DisplayName("[예외 케이스] - 존재하지 않는 메세지")
        void readMessage_notFound_fail() {
            // given
            given(messageRepository.findById(message.getId()))
                    .willReturn(Optional.empty());

            // when
            assertThatThrownBy(() -> messageService.getMessage(message.getId()))
                    .isInstanceOf(MessageNotFoundException.class);

            then(messageRepository).should().findById(message.getId());
            then(messageMapper).should(never()).toResponseDto(message);
        }

        @Test
        @DisplayName("[정상 케이스] - 모든 메세지 조회")
        void readMessage_all_success() {
            // given
            given(messageRepository.findAll())
                    .willReturn(List.of(message, message2));
            given(messageMapper.toResponseDto(message)).willReturn(messageResponseDto);
            given(messageMapper.toResponseDto(message2)).willReturn(messageResponseDto2);
            // when
            List<MessageResponseDto> messageDtoList = messageService.getAllMessages();

            // then
            assertThat(messageDtoList).hasSize(2);
            assertThat(messageDtoList).containsExactly(messageResponseDto, messageResponseDto2);
            assertThat(messageDtoList.get(0).id()).isNotEqualTo(messageDtoList.get(1).id());

            then(messageRepository).should().findAll();
            then(messageMapper).should().toResponseDto(message);
            then(messageMapper).should().toResponseDto(message2);
        }

        @Nested
        @DisplayName("메세지 업데이트 테스트")
        class MessageUpdate {
            private UUID messageId;
            private MessageResponseDto updatedMessageResponseDto;

            @BeforeEach
            void setUp() {
                message = Message.builder()
                        .author(author)
                        .channel(channel)
                        .content("test content")
                        .build();
                messageId = UUID.randomUUID();
                ReflectionTestUtils.setField(message, "id", messageId);

                updateMessageDto = new UpdateMessageDto("new content");
                updatedMessageResponseDto = new MessageResponseDto(
                        messageId,
                        message.getCreatedAt(),
                        message.getUpdatedAt(),
                        updateMessageDto.newContent(),
                        channel.getId(), userResponseDto,
                        List.of()
                );
            }

            @Test
            @DisplayName("[정상 케이스] - 메세지 업데이트 성공")
            void updateMessage_success() {
                // given
                given(messageRepository.findById(messageId))
                        .willReturn(Optional.of(message));

                given(messageMapper.toResponseDto(message))
                        .willReturn(updatedMessageResponseDto);

                // when
                MessageResponseDto result = messageService.updateMessage(messageId, updateMessageDto);

                // then
                assertThat(message.getContent()).isEqualTo(updateMessageDto.newContent());
                assertThat(message.getId()).isEqualTo(messageId);
                assertThat(result).isEqualTo(updatedMessageResponseDto);
                assertThat(result.content()).isEqualTo(updateMessageDto.newContent());

                then(messageRepository).should().findById(messageId);
                then(messageMapper).should().toResponseDto(message);
            }

            @Test
            @DisplayName("[예외 케이스] - 존재하지 않는 메세지 수정 시도")
            void updateMessage_notFound_fail() {
                // given
                given(messageRepository.findById(messageId))
                        .willReturn(Optional.empty());

                // when & then
                assertThatThrownBy(() -> messageService.updateMessage(messageId, updateMessageDto))
                        .isInstanceOf(MessageNotFoundException.class);

                then(messageRepository).should().findById(messageId);
                then(messageMapper).should(never()).toResponseDto(message);
            }
        }

        @Nested
        @DisplayName("슬라이스 조회 테스트")
        class MessageSliceRead {
            private Pageable pageable;

            @BeforeEach
            void setUp() {
                // 정렬을 위해 message의 생성 시간은 Instant.now().minusSeconds(10)
                message = Message.builder()
                        .author(author)
                        .channel(channel)
                        .content(createMessageDto.content())
                        .attachments(List.of())
                        .build();
                UUID messageId = UUID.randomUUID();
                ReflectionTestUtils.setField(message, "id", messageId);
                ReflectionTestUtils.setField(message, "createdAt", Instant.now().minusSeconds(10));
                ReflectionTestUtils.setField(message, "updatedAt", Instant.now().minusSeconds(10));


                message2 = Message.builder()
                        .author(author)
                        .channel(channel)
                        .content(createMessageDto.content())
                        .attachments(List.of())
                        .build();
                UUID messageId2 = UUID.randomUUID();
                ReflectionTestUtils.setField(message2, "id", messageId2);
                ReflectionTestUtils.setField(message2, "createdAt", Instant.now());
                ReflectionTestUtils.setField(message2, "updatedAt", Instant.now());

                messageResponseDto = new MessageResponseDto(
                        message.getId(),
                        message.getCreatedAt(),
                        message.getUpdatedAt(),
                        message.getContent(),
                        channel.getId(),
                        userResponseDto,
                        List.of()
                );

                messageResponseDto2 = new MessageResponseDto(
                        message2.getId(),
                        message2.getCreatedAt(),
                        message2.getUpdatedAt(),
                        message2.getContent(),
                        channel.getId(),
                        userResponseDto,
                        List.of()
                );

                pageable = PageRequest.of(0, 2);
            }

            @Test
            @DisplayName("[정상 케이스] - 페이지 조회 성공")
            void slicedMessage_success() {
                // given
                Instant cursor = null;

                Slice<Message> messageSlice = new SliceImpl<>(
                        List.of(message2, message),
                        pageable,
                        true  // hasNext
                );

                given(messageRepository.findAllByChannelId(
                        eq(channel.getId()),
                        any(Instant.class),
                        eq(pageable)
                )).willReturn(messageSlice);

                given(messageMapper.toResponseDto(message2))
                        .willReturn(messageResponseDto2);
                given(messageMapper.toResponseDto(message))
                        .willReturn(messageResponseDto);

                // PageResponseMapper는 Slice<MessageResponseDto>를 받아 PageResponse로 변환
                PageResponse<MessageResponseDto> expectedResponse = new PageResponse<>(
                        List.of(messageResponseDto2, messageResponseDto),
                        message.getCreatedAt(),  // nextCursor
                        2,
                        true,  // hasNext
                        null
                );

                given(pageResponseMapper.toSliceResponseDto(any(Slice.class), eq(message.getCreatedAt())))
                        .willReturn(expectedResponse);

                // when
                PageResponse<MessageResponseDto> result = messageService.getAllMessageByChannelId(
                        channel.getId(),
                        cursor,
                        pageable
                );

                // then
                assertThat(result).isNotNull();
                assertThat(result.content()).hasSize(2);
                assertThat(result.content()).containsExactly(messageResponseDto2, messageResponseDto);
                assertThat(result.hasNext()).isTrue();
                assertThat(result.nextCursor()).isEqualTo(message.getCreatedAt());

                then(messageRepository).should().findAllByChannelId(
                        eq(channel.getId()),
                        any(Instant.class),
                        eq(pageable)
                );
                then(messageMapper).should().toResponseDto(message2);
                then(messageMapper).should().toResponseDto(message);
                then(pageResponseMapper).should().toSliceResponseDto(any(Slice.class), eq(message.getCreatedAt()));
            }
        }

        @Nested
        @DisplayName("메세지 삭제 테스트")
        class MessageDelete {
            private UUID messageId;

            @BeforeEach
            void setUp() {
                messageId = UUID.randomUUID();
            }

            @Test
            @DisplayName("[정상 케이스] - 메세지 삭제 성공")
            void deleteMessage_success() {
                // given
                given(messageRepository.existsById(messageId))
                        .willReturn(true);
                willDoNothing().given(messageRepository).deleteById(messageId);

                // when
                messageService.deleteMessage(messageId);

                // then
                then(messageRepository).should().existsById(messageId);
                then(messageRepository).should().deleteById(messageId);
            }

            @Test
            @DisplayName("[예외 케이스] - 존재하지 않는 메세지 삭제 시도")
            void deleteMessage_notFound_fail() {
                // given
                given(messageRepository.existsById(messageId))
                        .willReturn(false);

                // when & then
                assertThatThrownBy(() -> messageService.deleteMessage(messageId))
                        .isInstanceOf(MessageNotFoundException.class);

                then(messageRepository).should().existsById(messageId);
                then(messageRepository).should(never()).deleteById(any());
            }
        }
    }


}