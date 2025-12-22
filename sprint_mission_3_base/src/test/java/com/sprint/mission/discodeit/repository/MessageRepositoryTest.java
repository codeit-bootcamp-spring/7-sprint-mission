package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;

import com.sprint.mission.discodeit.entity.Message;
import jakarta.persistence.EntityManager;
import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private EntityManager em;

    @Test
    @DisplayName("findAllByChannelIdWithAuthor: cursor 이전 메시지를 createdAt 내림차순으로 Slice 조회한다")
    void findAllByChannelIdWithAuthor_success_pagingByCursor() {
        UUID userId = UUID.randomUUID();
        UUID channelAId = UUID.randomUUID();
        UUID channelBId = UUID.randomUUID();

        Instant base = Instant.parse("2025-01-01T00:00:00Z");
        insertUser(userId, "user1", "user1@test.com", "password1234", base);
        insertChannel(channelAId, "PUBLIC", "channel-a", "desc-a", base);
        insertChannel(channelBId, "PUBLIC", "channel-b", "desc-b", base);

        Instant t1 = base.plusSeconds(10);
        Instant t2 = base.plusSeconds(20);
        Instant t3 = base.plusSeconds(30);
        Instant cursor = base.plusSeconds(100);

        UUID m1 = UUID.randomUUID();
        UUID m2 = UUID.randomUUID();
        UUID m3 = UUID.randomUUID();
        UUID mOther = UUID.randomUUID();

        insertMessage(m1, channelAId, userId, "m1", t1);
        insertMessage(m2, channelAId, userId, "m2", t2);
        insertMessage(m3, channelAId, userId, "m3", t3);
        insertMessage(mOther, channelBId, userId, "other", base.plusSeconds(40));

        em.flush();
        em.clear();

        Slice<Message> first = messageRepository.findAllByChannelIdWithAuthor(
                channelAId,
                cursor,
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        assertThat(first.getContent()).hasSize(2);
        assertThat(first.hasNext()).isTrue();

        List<Message> firstContent = first.getContent();
        assertThat(firstContent.get(0).getCreatedAt()).isEqualTo(t3);
        assertThat(firstContent.get(1).getCreatedAt()).isEqualTo(t2);

        assertThat(firstContent.get(0).getAuthor()).isNotNull();
        assertThat(firstContent.get(0).getAuthor().getUsername()).isEqualTo("user1");
        assertThat(firstContent.get(1).getAuthor()).isNotNull();
        assertThat(firstContent.get(1).getAuthor().getUsername()).isEqualTo("user1");

        Instant nextCursor = firstContent.get(1).getCreatedAt();

        em.clear();

        Slice<Message> second = messageRepository.findAllByChannelIdWithAuthor(
                channelAId,
                nextCursor,
                PageRequest.of(0, 2, Sort.by(Sort.Direction.DESC, "createdAt"))
        );

        assertThat(second.getContent()).hasSize(1);
        assertThat(second.hasNext()).isFalse();
        assertThat(second.getContent().get(0).getCreatedAt()).isEqualTo(t1);
        assertThat(second.getContent().get(0).getAuthor()).isNotNull();
        assertThat(second.getContent().get(0).getAuthor().getUsername()).isEqualTo("user1");
    }

    @Test
    @DisplayName("findLastMessageAtByChannelId: 채널의 마지막 메시지 createdAt(MAX)을 Optional로 반환한다")
    void findLastMessageAtByChannelId_success() {
        UUID userId = UUID.randomUUID();
        UUID channelAId = UUID.randomUUID();
        UUID channelNoMessageId = UUID.randomUUID();

        Instant base = Instant.parse("2025-01-02T00:00:00Z");
        insertUser(userId, "user2", "user2@test.com", "password1234", base);
        insertChannel(channelAId, "PUBLIC", "channel-a", "desc-a", base);
        insertChannel(channelNoMessageId, "PUBLIC", "channel-empty", "desc-empty", base);

        Instant t1 = base.plusSeconds(10);
        Instant t2 = base.plusSeconds(20);

        insertMessage(UUID.randomUUID(), channelAId, userId, "old", t1);
        insertMessage(UUID.randomUUID(), channelAId, userId, "new", t2);

        em.flush();
        em.clear();

        Optional<Instant> last = messageRepository.findLastMessageAtByChannelId(channelAId);
        assertThat(last).isPresent();
        assertThat(last.get()).isEqualTo(t2);

        Optional<Instant> empty = messageRepository.findLastMessageAtByChannelId(channelNoMessageId);
        assertThat(empty).isEmpty();
    }

    private void insertUser(UUID id, String username, String email, String password, Instant createdAt) {
        em.createNativeQuery("""
        INSERT INTO users (id, username, email, password, created_at, updated_at, profile_id)
        VALUES (:id, :username, :email, :password, :createdAt, NULL, NULL)
        """)
                .setParameter("id", id)
                .setParameter("username", username)
                .setParameter("email", email)
                .setParameter("password", password)
                .setParameter("createdAt", createdAt)
                .executeUpdate();

        em.createNativeQuery("""
        INSERT INTO user_statuses (id, user_id, created_at, last_active_at, last_seen_at, updated_at)
        VALUES (:id, :userId, :createdAt, :lastActiveAt, NULL, NULL)
        """)
                .setParameter("id", UUID.randomUUID())
                .setParameter("userId", id)
                .setParameter("createdAt", createdAt)
                .setParameter("lastActiveAt", createdAt)
                .executeUpdate();
    }

    private void insertChannel(UUID id, String type, String name, String description, Instant createdAt) {
        em.createNativeQuery("""
        INSERT INTO channels (id, type, name, description, created_at, updated_at)
        VALUES (:id, :type, :name, :description, :createdAt, NULL)
        """)
                .setParameter("id", id)
                .setParameter("type", type)
                .setParameter("name", name)
                .setParameter("description", description)
                .setParameter("createdAt", createdAt)
                .executeUpdate();
    }

    private void insertMessage(UUID id, UUID channelId, UUID authorId, String content, Instant createdAt) {
        em.createNativeQuery("""
        INSERT INTO messages (id, channel_id, author_id, content, created_at, updated_at)
        VALUES (:id, :channelId, :authorId, :content, :createdAt, NULL)
        """)
                .setParameter("id", id)
                .setParameter("channelId", channelId)
                .setParameter("authorId", authorId)
                .setParameter("content", content)
                .setParameter("createdAt", createdAt)
                .executeUpdate();
    }
}
