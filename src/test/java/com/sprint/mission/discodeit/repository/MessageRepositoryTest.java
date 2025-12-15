package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.QuerydslConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test") // active 설정이 따로 있으면 넣어줘야 함
@Import(QuerydslConfig.class)
@DataJpaTest
@DisplayName("messageRepository 테스트")
class MessageRepositoryTest {

    @Autowired
    MessageRepository messageRepository;
    @Autowired
    EntityManager em;

    private Channel channel;
    private User user;


    @BeforeEach
    void setUp() {
        user = new User("test@test.com", "1234", "tester");
        channel = new Channel("ch1", "desc", ChannelType.PUBLIC);
        em.persist(user);
        em.persist(channel);
    }

    @Test
    @DisplayName("메시지 삭제 성공")
    void deleteById_success() {
        // given
        Message message = new Message(user, channel, "hello");

        Message saved = messageRepository.save(message);
        UUID messageId = saved.getId();

        // when
        messageRepository.deleteById(messageId);
        em.flush();

        // then
        Optional<Message> result = messageRepository.findById(messageId);
        assertThat(result).isEmpty();
    }

    @Test
    @DisplayName("cursor 없이 최신순 조회")
    void findAll_desc_withoutCursor() {
        // given
        Message m1 = new Message(user, channel, "m1");
        Message m2 = new Message(user, channel, "m2");
        Message m3 = new Message(user, channel, "m3");

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.flush();

        // when
        List<Message> result =
                messageRepository.findAllByChannelId(
                        channel.getId(),
                        10,
                        "createdAt,desc",
                        null
                );

        // then
        assertThat(result).hasSize(3);
        assertThat(result)
                .extracting(Message::getContent)
                .containsExactly("m3", "m2", "m1");
    }

    @Test
    @DisplayName("cursor 기준 이전 메시지만 최신순으로 조회된다")
    void findAll_desc_withCursor() {
        // given
        Message m1 = new Message(user, channel, "m1");
        Message m2 = new Message(user, channel, "m2");
        Message m3 = new Message(user, channel, "m3");

        em.persist(m1);
        em.persist(m2);
        em.persist(m3);
        em.flush();

        // 🔑 커서를 m2의 createdAt으로 설정
        Instant cursor = m2.getCreatedAt();

        // when
        List<Message> result =
                messageRepository.findAllByChannelId(
                        channel.getId(),
                        10,
                        "createdAt,desc",
                        cursor
                );

        // then
        assertThat(result).hasSize(1);
        assertThat(result)
                .extracting(Message::getContent)
                .containsExactly("m1");
    }
}

