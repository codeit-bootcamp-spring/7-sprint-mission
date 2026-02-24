package com.sprint.mission.discodeit.service.basic;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.Dto_MessageUpdate;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.mapper.MessageMapper;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.dto.dto_Neo.MessageDto;
import com.sprint.mission.discodeit.page.PageResponseDto;
import com.sprint.mission.discodeit.repository.jpa.*;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.Optional;
import java.util.UUID;
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
class MessageServiceTest {

    @InjectMocks
    private MessageService messageService;

    @Mock private MessagesRepository messageRepository;
    @Mock private ChannelsRepository channelRepository;
    @Mock private UsersRepository userRepository;
    @Mock private MessageAttachmentsRepository messageAttachmentsRepository;
    @Mock private BinaryContentStorage binaryContentStorage;
    @Mock private BinaryContentsRepository binaryContentRepository;
    @Mock private MessageMapper messageMapper;
    @Mock private PageResponseMapper pageResponseMapper;

    /* =========================
       create()
       ========================= */

//    @Test
//    void create_success_without_files() {
//        UUID channelId = UUID.randomUUID();
//        UUID userId = UUID.randomUUID();
//
//        Channel channel = new Channel(null, "channel", "desc");
//        User user = new User("user", "email@test.com", "pw", null);
//        Message message = new Message("hello", channel, user);
//
//        given(channelRepository.findById(channelId))
//            .willReturn(Optional.of(channel));
//        given(userRepository.findById(userId))
//            .willReturn(Optional.of(user));
//        given(messageRepository.save(any(Message.class)))
//            .willReturn(message);
//        given(messageMapper.toDto(message))
//            .willReturn(mock(MessageDto.class));
//
//        MessageCreateRequest request =
//            new MessageCreateRequest("hello", channelId, userId);
//
//        MessageDto result =
//            messageService.create(request, List.of());
//
//        assertThat(result).isNotNull();
//    }

//    @Test
//    void create_fail_channelId_null() {
//        MessageCreateRequest request =
//            new MessageCreateRequest("hi", null, UUID.randomUUID());
//
//        assertThatThrownBy(() ->
//            messageService.create(request, List.of())
//        ).isInstanceOf(channelNotFoundException.class);
//    }

//    @Test
//    void create_fail_authorId_null() {
//        MessageCreateRequest request =
//            new MessageCreateRequest("hi", UUID.randomUUID(), UUID.randomUUID());
//
//        assertThatThrownBy(() ->
//            messageService.create(request, null)
//        ).isInstanceOf(UserNotFoundException.class);
//    }

    /* =========================
       find()
       ========================= */

    @Test
    void find_success() {
        UUID messageId = UUID.randomUUID();
        Message message = mock(Message.class);

        given(messageRepository.findById(messageId))
            .willReturn(Optional.of(message));
        given(messageMapper.toDto(message))
            .willReturn(mock(MessageDto.class));

        MessageDto result = messageService.find(messageId);

        assertThat(result).isNotNull();
    }

    @Test
    void find_fail_not_found() {
        UUID messageId = UUID.randomUUID();

        given(messageRepository.findById(messageId))
            .willReturn(Optional.empty());

        assertThatThrownBy(() ->
            messageService.find(messageId)
        ).isInstanceOf(IllegalArgumentException.class);
    }

    /* =========================
       deleteMessage()
       ========================= */

    @Test
    void deleteMessage_success() {
        UUID messageId = UUID.randomUUID();
        Message message = mock(Message.class);

        given(messageRepository.findById(messageId))
            .willReturn(Optional.of(message));

        messageService.deleteMessage(messageId);

        then(messageRepository).should().deleteById(messageId);
    }

    @Test
    void deleteMessage_fail_not_found() {
        UUID messageId = UUID.randomUUID();

        given(messageRepository.findById(messageId))
            .willReturn(Optional.empty());

        assertThatThrownBy(() ->
            messageService.deleteMessage(messageId)
        ).isInstanceOf(NoSuchElementException.class);
    }

    /* =========================
       findAllByChannelId()
       ========================= */

    @Test
    void findAllByChannelId_success() {
        UUID channelId = UUID.randomUUID();
        Pageable pageable = PageRequest.of(0, 10);

        Message message = mock(Message.class);
        MessageDto dto = mock(MessageDto.class);

        Slice<Message> slice =
            new SliceImpl<>(List.of(message), pageable, false);

        given(messageRepository.findByChannelId(channelId, pageable))
            .willReturn(slice);
        given(messageMapper.toDto(message))
            .willReturn(dto);
        given(pageResponseMapper.fromSlice(any()))
            .willReturn(mock(PageResponseDto.class));

        PageResponseDto<MessageDto> result =
            messageService.findAllByChannelId(channelId, pageable);

        assertThat(result).isNotNull();
    }

    /* =========================
       updateMessage()
       ========================= */

    @Test
    void updateMessage_success() {
        UUID messageId = UUID.randomUUID();
        Message message = new Message("old", null, null);

        given(messageRepository.findById(messageId))
            .willReturn(Optional.of(message));
        given(messageMapper.toDto(message))
            .willReturn(mock(MessageDto.class));

        Dto_MessageUpdate update =
            new Dto_MessageUpdate("new");

        MessageDto result =
            messageService.updateMessage(messageId, update);

        assertThat(message.getContent()).isEqualTo("new");
        assertThat(result).isNotNull();
    }

    @Test
    void updateMessage_fail_not_found() {
        UUID messageId = UUID.randomUUID();

        given(messageRepository.findById(messageId))
            .willReturn(Optional.empty());

        assertThatThrownBy(() ->
            messageService.updateMessage(messageId, new Dto_MessageUpdate("x"))
        ).isInstanceOf(NoSuchElementException.class);
    }
}
