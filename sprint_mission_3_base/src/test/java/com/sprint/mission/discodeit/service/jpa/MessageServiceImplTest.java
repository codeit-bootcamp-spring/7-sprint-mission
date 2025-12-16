package com.sprint.mission.discodeit.service.jpa;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.BDDMockito.*;

import com.sprint.mission.discodeit.dto.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.request.MessageUpdateRequest;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.PageResponseMapper;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

@ExtendWith(MockitoExtension.class)
class MessageServiceImplTest {

    @Mock
    private MessageRepository messageRepository;

    @Mock
    private UserRepository userRepository;

    @Mock
    private ChannelRepository channelRepository;

    @Mock
    private BinaryContentRepository binaryContentRepository;

    @Mock
    private BinaryContentStorage storage;

    @Mock
    private PageResponseMapper pageResponseMapper;

    @InjectMocks
    private MessageServiceImpl messageService;

    @Test
    @DisplayName("create 성공: 유저/채널 존재 + 첨부 2개 -> message 저장 + binary 저장 + storage.put 호출")
    void create_success_withAttachments() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        User user = new User("u", "u@test.com", "pw", null);
        Channel channel = new Channel(ChannelType.PUBLIC, "c", null);

        ReflectionTestUtils.setField(user, "id", authorId);
        ReflectionTestUtils.setField(channel, "id", channelId);

        given(userRepository.findById(authorId)).willReturn(Optional.of(user));
        given(channelRepository.findById(channelId)).willReturn(Optional.of(channel));

        given(messageRepository.save(any(Message.class))).willAnswer(invocation -> {
            Message m = invocation.getArgument(0);
            ReflectionTestUtils.setField(m, "id", UUID.randomUUID());
            return m;
        });

        given(binaryContentRepository.save(any(BinaryContent.class))).willAnswer(invocation -> {
            BinaryContent b = invocation.getArgument(0);
            ReflectionTestUtils.setField(b, "id", UUID.randomUUID());
            return b;
        });

        MessageCreateRequest req = new MessageCreateRequest("hello", channelId, authorId);

        BinaryContentCreateRequest a1 = new BinaryContentCreateRequest("a.txt", "text/plain", "a".getBytes());
        BinaryContentCreateRequest a2 = new BinaryContentCreateRequest("b.txt", "text/plain", "bb".getBytes());

        var result = messageService.create(req, List.of(a1, a2));

        assertThat(result).isNotNull();

        then(messageRepository).should().save(any(Message.class));
        then(binaryContentRepository).should(times(2)).save(any(BinaryContent.class));
        then(storage).should(times(2)).put(any(UUID.class), any(byte[].class));
    }

    @Test
    @DisplayName("create 실패: author 없음 -> IllegalArgumentException(User not found) + message 저장 안함")
    void create_fail_userNotFound() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        given(userRepository.findById(authorId)).willReturn(Optional.empty());

        MessageCreateRequest req = new MessageCreateRequest("hello", channelId, authorId);

        assertThatThrownBy(() -> messageService.create(req, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("User not found");

        then(messageRepository).should(never()).save(any());
        then(channelRepository).should(never()).findById(any());
        then(binaryContentRepository).should(never()).save(any());
        then(storage).should(never()).put(any(), any());
    }

    @Test
    @DisplayName("create 실패: channel 없음 -> IllegalArgumentException(Channel not found) + message 저장 안함")
    void create_fail_channelNotFound() {
        UUID authorId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        User user = new User("u", "u@test.com", "pw", null);
        ReflectionTestUtils.setField(user, "id", authorId);

        given(userRepository.findById(authorId)).willReturn(Optional.of(user));
        given(channelRepository.findById(channelId)).willReturn(Optional.empty());

        MessageCreateRequest req = new MessageCreateRequest("hello", channelId, authorId);

        assertThatThrownBy(() -> messageService.create(req, List.of()))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Channel not found");

        then(messageRepository).should(never()).save(any());
        then(binaryContentRepository).should(never()).save(any());
        then(storage).should(never()).put(any(), any());
    }

    @Test
    @DisplayName("update 성공: 메시지 존재 -> content 변경")
    void update_success() {
        UUID messageId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();
        UUID authorId = UUID.randomUUID();

        Channel channel = new Channel(ChannelType.PUBLIC, "c", null);
        User user = new User("u", "u@test.com", "pw", null);

        ReflectionTestUtils.setField(channel, "id", channelId);
        ReflectionTestUtils.setField(user, "id", authorId);

        Message message = new Message("old", channel, user, List.of());
        ReflectionTestUtils.setField(message, "id", messageId);

        given(messageRepository.findById(messageId)).willReturn(Optional.of(message));

        MessageUpdateRequest req = new MessageUpdateRequest("new");
        var result = messageService.update(messageId, req);

        assertThat(result).isNotNull();
        then(messageRepository).should().findById(messageId);
    }

    @Test
    @DisplayName("update 실패: 메시지 없음 -> IllegalArgumentException(Message not found)")
    void update_fail_messageNotFound() {
        UUID messageId = UUID.randomUUID();
        given(messageRepository.findById(messageId)).willReturn(Optional.empty());

        MessageUpdateRequest req = new MessageUpdateRequest("new");

        assertThatThrownBy(() -> messageService.update(messageId, req))
                .isInstanceOf(IllegalArgumentException.class)
                .hasMessageContaining("Message not found");

        then(messageRepository).should().findById(messageId);
    }

    @Test
    @DisplayName("delete 성공: deleteById 호출")
    void delete_success() {
        UUID messageId = UUID.randomUUID();

        messageService.delete(messageId);

        then(messageRepository).should().deleteById(messageId);
    }

    @Test
    @DisplayName("delete 실패: repository가 예외 던지면 그대로 전파")
    void delete_fail_repositoryThrows() {
        UUID messageId = UUID.randomUUID();

        willThrow(new RuntimeException("db error"))
                .given(messageRepository).deleteById(messageId);

        assertThatThrownBy(() -> messageService.delete(messageId))
                .isInstanceOf(RuntimeException.class)
                .hasMessageContaining("db error");

        then(messageRepository).should().deleteById(messageId);
    }
}
