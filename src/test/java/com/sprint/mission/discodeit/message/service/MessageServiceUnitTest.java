package com.sprint.mission.discodeit.message.service;

import com.sprint.mission.discodeit.TestFixture;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDto;
import com.sprint.mission.discodeit.dto.response.PageResponseDtoBasic;
import com.sprint.mission.discodeit.dto.response.message.MessageDto;
import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.exception.domain.channel.ChannelNotExistException;
import com.sprint.mission.discodeit.exception.domain.message.MessageNotExistException;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseBasicMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.*;
import org.springframework.mock.web.MockMultipartFile;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.BDDMockito.*;
import static org.mockito.Mockito.never;
import static org.mockito.Mockito.times;

@ExtendWith(MockitoExtension.class)
@DisplayName("MessageService Unit Test")
public class MessageServiceUnitTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private MessageAttachmentRepository messageAttachmentRepository;

    @Mock
    private MessageMapper messageMapper;

    @Mock
    private BinaryContentStorage binaryContentStorage;

    @Mock
    private PageResponseMapper<MessageDto> pageResponseMapper;

    @Mock
    private PageResponseBasicMapper<MessageDto> pageResponseBasicMapper;

    @Mock
    private ReadStatusRepository readStatusRepository;

    @InjectMocks
    private BasicMessageService messageService;

    private Channel channel;
    private User user;
    private Message message;
    private MessageDto messageDto;
    private BinaryContent binaryContent;
    private ReadStatus readStatus;
    private MessageAttachment messageAttachment;
    private Page<Message> page;
    private Slice<Message> slice;
    private PageResponseDto<MessageDto> pageResponseDto;


    @BeforeEach
    void setUp() {
        channel = Channel.privateChannelFactory("privateChannel", "random message");
        user = User.createUserFactory("user1","111@user","1234");
        message = Message.createMessageFactory(
                "안녕",
                user,
                channel
        );
        binaryContent = new BinaryContent("siuuFile","text/plain",10L);
        readStatus = ReadStatus.createReadStatusFactory(user,channel);
        messageAttachment = new MessageAttachment(message,binaryContent);
        page = Page.empty();
        pageResponseDto = new PageResponseDto<>(
                null,
                null,
                10,
                false,
                30L
        );
        slice = new PageImpl<>(List.of(message),PageRequest.of(0,10),30L);
        messageDto = new MessageDto(
                message.getId(),
                message.getCreatedAt(),
                message.getUpdatedAt(),
                message.getContent(),
                channel.getId(),
                null,
                null
        );
    }
    @Test
    @DisplayName("[정상 케이스] 메세지 생성 성공")
    void createMessage_Success() {
    given(channelRepository.findById(any(UUID.class))).willReturn(Optional.of(channel));
    given(userRepository.findById(any(UUID.class))).willReturn(Optional.of(user));
    given(readStatusRepository.findReadStatusByChannelAndUser(any(Channel.class),any(User.class)))
            .willReturn(readStatus);

    given(messageRepository.save(any(Message.class))).willReturn(message);
    given(binaryContentRepository.save(any(BinaryContent.class))).willReturn(binaryContent);
    given(binaryContentStorage.put(any(),any(byte[].class))).willReturn(UUID.randomUUID());
    given(messageAttachmentRepository.save(any(MessageAttachment.class))).willReturn(messageAttachment);
    given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

        MessageDto response = messageService.createMessage(TestFixture.messageCreateFactory(UUID.randomUUID(), UUID.randomUUID()),
                List.of(new MockMultipartFile("file",
                        "test.txt", "text/plain", "Hello, World!".getBytes()))
        );

        assertThat(response).isEqualTo(messageDto);

        then(messageRepository).should(times(1)).save(any(Message.class));
        then(binaryContentRepository).should(times(1)).save(any(BinaryContent.class));
        then(binaryContentStorage).should(times(1)).put(any(),any(byte[].class));
        then(messageAttachmentRepository).should(times(1)).save(any(MessageAttachment.class));
        then(messageMapper).should(times(1)).toDto(any(Message.class));
    }

    @Test
    @DisplayName("[예외 케이스] 메세지 생성 실패")
    void createMessage_Fail() {
        given(channelRepository.findById(any(UUID.class)))
                .willThrow(new ChannelNotExistException(UUID.randomUUID()));

        assertThatThrownBy(()->messageService.createMessage(TestFixture.messageCreateFactory(UUID.randomUUID(), UUID.randomUUID()),
                List.of(new MockMultipartFile("file",
                        "test.txt", "text/plain", "Hello, World!".getBytes()))))
                .isInstanceOf(ChannelNotExistException.class);

        then(messageRepository).should(never()).save(any(Message.class));
        then(binaryContentRepository).should(never()).save(any(BinaryContent.class));
        then(binaryContentStorage).should(never()).put(any(),any(byte[].class));
        then(messageAttachmentRepository).should(never()).save(any(MessageAttachment.class));

    }

    @Test
    @DisplayName("[정상 케이스] 메세지 변경 성공")
    void patchMessage_Success() {

        given(messageRepository.findById(any(UUID.class))).willReturn(Optional.of(message));
        given(messageRepository.save(any(Message.class))).willReturn(message);
        given(messageMapper.toDto(any(Message.class))).willReturn(messageDto);

        MessageDto response = messageService.patchMessage(new MessagePatchRequestDto(
                "new Message content"
        ), UUID.randomUUID());

        assertThat(response).isEqualTo(messageDto);

        then(messageRepository).should(times(1)).save(any(Message.class));
    }

    @Test
    @DisplayName("[예외 케이스] 메세지 변경 실패" )
    void patchMessage_Fail() {

        given(messageRepository.findById(any(UUID.class)))
                .willThrow(new MessageNotExistException(UUID.randomUUID()));

        assertThatThrownBy(()->messageService.patchMessage(new MessagePatchRequestDto(
                "new Message content"
        ), UUID.randomUUID()))
                .isInstanceOf(MessageNotExistException.class);

        then(messageRepository).should(never()).save(any(Message.class));

    }

    @Test
    @DisplayName("[정상 케이스] 메세지 삭제 성공")
    void deleteMessage_Success() {
        given(messageRepository.existsById(any(UUID.class))).willReturn(true);
        willDoNothing().given(messageRepository).deleteById(any(UUID.class));
        messageService.deleteMessage(UUID.randomUUID());
        then(messageRepository).should(times(1)).deleteById(any(UUID.class));

    }

    @Test
    @DisplayName("[예외 케이스] 메세지 삭제 실패")
    void deleteMessage_Fail() {
        assertThatThrownBy(()->messageService.deleteMessage(UUID.randomUUID()));
        then(messageRepository).should(never()).deleteById(any(UUID.class));
    }

    @Test
    @DisplayName("[정상 케이스] 채널 id로 메세지 조회 성공")
    void readAllMessageByChannelId_Success() {

        given(channelRepository.existsById(any(UUID.class))).willReturn(true);
        given(messageRepository.findByChannelId(any(UUID.class),any(Pageable.class)))
                .willReturn(page);

        given(pageResponseBasicMapper.fromPage(any(Page.class)))
                .willReturn(null);
        PageResponseDtoBasic<MessageDto> response = messageService.findallByChannelId(
                UUID.randomUUID(),
                Pageable.ofSize(10)
        );

        assertThat(response).isEqualTo(null);

        then(messageRepository).should(times(1))
                .findByChannelId(any(UUID.class),any(Pageable.class));
        then(pageResponseBasicMapper)
                .should(times(1)).fromPage(any(Page.class));

    }

    @Test
    @DisplayName("[정상 케이스] 채널 id로 메세지 조회 성공2")
    void readAllMessageByChannelId_TargetPage_Success() {

        given(channelRepository.existsById(any(UUID.class))).willReturn(true);
        given(messageRepository.findByChannelId(any(UUID.class),any(Pageable.class)))
                .willReturn(new PageImpl<>(List.of(message),PageRequest.of(0,10),8848L));

        given(messageMapper.toDto(any(Message.class)))
        .willReturn(messageDto);

        given(pageResponseBasicMapper.fromPage(any(Page.class)))
        .willReturn(new PageResponseDtoBasic<>(
                List.of(messageDto),
                1557,
                1557,
                false,
                8848L
        ));

        PageResponseDtoBasic<MessageDto> response = messageService.findallByChannelId(
                UUID.randomUUID(),
                Pageable.ofSize(10)
        );

        assertThat(response.totalElements()).isEqualTo(8848L);
        assertThat(response.size()).isEqualTo(1557);
        assertThat(response.hasNext()).isEqualTo(false);

        then(pageResponseBasicMapper)
                .should(times(1)).fromPage(any(Page.class));
        then(messageMapper).should(times(1)).toDto(any(Message.class));

    }

    @Test
    @DisplayName("[예외 케이스] 채널 id로 메세지 조회 실패")
    void readAllMessageByChannelId_Fail() {

        assertThatThrownBy(()->messageService.findallByChannelId(UUID.randomUUID(),Pageable.ofSize(10)))
                .isInstanceOf(ChannelNotExistException.class);

        then(messageRepository).should(never())
                .findByChannelId(any(UUID.class),any(Pageable.class));
    }

    @Test
    @DisplayName("[정상 케이스] 채널 id로 메세지 조회 슬라이스 커서 (커서가 null일때) ")
    void findAllByChannelIdWithCursor_Success() {

        given(messageRepository.findByChannelIdAndCreatedAtAfter(
                any(UUID.class),any( Instant.class),any(Pageable.class)
        ))
                .willReturn(slice);
        given(messageMapper.toDto(any(Message.class)))
                .willReturn(messageDto);
        given(pageResponseMapper.fromSlice(any(),any()))
                .willReturn(pageResponseDto);

        PageResponseDto<MessageDto> response = messageService.findallByChannelIdWithCursor(
                UUID.randomUUID(),
                null,
                Pageable.ofSize(10)
        );

        assertThat(response.size()).isEqualTo(pageResponseDto.size());
        assertThat(response.hasNext()).isEqualTo(pageResponseDto.hasNext());
        assertThat(response.totalElements()).isEqualTo(pageResponseDto.totalElements());

        then(messageRepository).should(times(1))
                .findByChannelIdAndCreatedAtAfter(any(UUID.class),any( Instant.class),any(Pageable.class));
        then(messageMapper).should(times(1)).toDto(any(Message.class));
        then(pageResponseMapper).should(times(1)).fromSlice(any(),any());

    }

    @Test
    @DisplayName("[정상 케이스] 채널 id로 메세지 조회 슬라이스 커서 (커서가 null 아닐때) ")
    void findAllByChannelIdWithCursor_WithCursor_Success() {

        given(messageRepository.findByChannelIdAndCreatedAtAfter(
                any(UUID.class),any( Instant.class),any(Pageable.class)
        ))
                .willReturn(slice);
        given(messageMapper.toDto(any(Message.class)))
                .willReturn(messageDto);
        given(pageResponseMapper.fromSlice(any(),any()))
                .willReturn(pageResponseDto);

        PageResponseDto<MessageDto> response = messageService.findallByChannelIdWithCursor(
                UUID.randomUUID(),
                Instant.now().toString(),
                Pageable.ofSize(10)
        );

        assertThat(response.size()).isEqualTo(pageResponseDto.size());
        assertThat(response.hasNext()).isEqualTo(pageResponseDto.hasNext());
        assertThat(response.totalElements()).isEqualTo(pageResponseDto.totalElements());

        then(messageRepository).should(times(1))
                .findByChannelIdAndCreatedAtAfter(any(UUID.class),any( Instant.class),any(Pageable.class));
        then(messageMapper).should(times(1)).toDto(any(Message.class));
        then(pageResponseMapper).should(times(1)).fromSlice(any(),any());

    }

}
