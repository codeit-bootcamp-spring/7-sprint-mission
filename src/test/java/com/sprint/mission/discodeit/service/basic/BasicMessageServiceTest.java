package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class BasicMessageServiceTest {
    BasicMessageService messageService;
    JCFMessageRepository messageRepository;

    @BeforeEach
    void setUp() {
        messageRepository = JCFMessageRepository.getInstance();
        messageService = BasicMessageService.getInstance();

        messageRepository.findAll().forEach(m -> messageRepository.delete(m.getId()));
    }

    @Test
    @DisplayName("유저별 메세지 조회")
    void findAllByUser() {
        UUID user1 = UUID.randomUUID();
        UUID user2 = UUID.randomUUID();
        UUID channel = UUID.randomUUID();

        messageService.create(new Message(channel, user1, "유저1 메시지1"));
        messageService.create(new Message(channel, user1, "유저1 메시지2"));
        messageService.create(new Message(channel, user2, "유저2 메시지"));

        List<Message> user1Messages = messageService.findAllByUser(user1);

        assertEquals(2, user1Messages.size());
        assertTrue(user1Messages.stream().allMatch(m -> m.getSpeakerId().equals(user1)));
    }

    @Test
    @DisplayName("채널별 메세지 조회")
    void findAllByChannel() {
        UUID ch1 = UUID.randomUUID();
        UUID ch2 = UUID.randomUUID();
        UUID user = UUID.randomUUID();

        messageService.create(new Message(ch1, user, "채널1 메시지1"));
        messageService.create(new Message(ch1, user, "채널1 메시지2"));
        messageService.create(new Message(ch2, user, "채널2 메시지"));

        List<Message> ch1Messages = messageService.findAllByChannel(ch1);

        assertEquals(2, ch1Messages.size());
        assertTrue(ch1Messages.stream().allMatch(m -> m.getChannelId().equals(ch1)));
    }

    @Test
    @DisplayName("내용으로 메세지 검색")
    void searchMessagesByContent() {
        UUID ch = UUID.randomUUID();
        UUID user = UUID.randomUUID();

        messageService.create(new Message(ch, user, "오늘 점심 뭐 먹지?"));
        messageService.create(new Message(ch, user, "점심에 라면 어때?"));
        messageService.create(new Message(ch, user, "저녁엔 운동해야지."));

        List<Message> result = messageService.searchMessagesByContent("점심");

        assertEquals(2, result.size());
        assertTrue(result.stream().allMatch(m -> m.getContent().contains("점심")));
    }

    @Test
    @DisplayName("메세지 생성")
    void create() {
        UUID channelId = UUID.randomUUID();
        UUID speakerId = UUID.randomUUID();

        Message message = new Message(channelId, speakerId, "안녕하세요!");
        messageService.create(message);

        Message stored = messageRepository.findById(message.getId());
        assertNotNull(stored);
        assertEquals("안녕하세요!", stored.getContent());
    }

    @Test
    @DisplayName("메세지 수정")
    void update() {
        UUID channelId = UUID.randomUUID();
        UUID speakerId = UUID.randomUUID();

        Message created = new Message(channelId, speakerId, "수정 전 내용");
        messageService.create(created);

        Message updated = messageService.update(created.getId(), "수정 후 내용");

        assertNotNull(updated);
        assertEquals("수정 후 내용", updated.getContent());

        Message stored = messageRepository.findById(created.getId());
        assertEquals("수정 후 내용", stored.getContent());
    }

    @Test
    @DisplayName("메세지 삭제")
    void delete() {
        UUID channelId = UUID.randomUUID();
        UUID speakerId = UUID.randomUUID();

        Message created = new Message(channelId, speakerId, "삭제할 메세지");
        messageService.create(created);

        Message deleted = messageService.delete(created.getId());
        assertNotNull(deleted);
        assertEquals("삭제할 메세지", deleted.getContent());

        Message found = messageRepository.findById(created.getId());
        assertNull(found);
    }
}