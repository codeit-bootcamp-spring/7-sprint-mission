package com.sprint.mission.discodeit.slice.jpa;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@DataJpaTest
@Import(JpaAuditingConfig.class)
@DisplayName("MessageRepository JPA Slice Test")
class MessageRepositorySliceTest {

    @Autowired MessageRepository messageRepository;
    @Autowired UserRepository userRepository;
    @Autowired UserStatusRepository userStatusRepository;
    @Autowired ChannelRepository channelRepository;

    private User saveUserWithStatus(String username, String email, String password) {
        User user = User.create(username, email, password, null);
        // JPQL이 join fetch a.userStatus 이므로 반드시 있어야 함
        user.initUserStatus();
        userRepository.saveAndFlush(user);
        return user;
    }

    private Channel savePublicChannel(String name) {
        return channelRepository.saveAndFlush(Channel.createPublicChannel(name, "desc"));
    }

    private Message saveMessage(String content, User author, Channel channel) {
        Message message = Message.builder()
                .content(content)
                .author(author)
                .channel(channel)
                .attachments(List.of())
                .build();
        return messageRepository.saveAndFlush(message);
    }

    @Nested
    @DisplayName("findLatestByChannelId (커스텀 + 페이징/정렬)")
    class FindLatestByChannelId {

        @Test
        @DisplayName("[Success] 최신순 DESC로 size만큼 조회된다")
        void shouldReturnLatestMessages_desc_withLimit() throws Exception {
            // given
            User user = saveUserWithStatus("u1", "u1@a.com", "pw");
            Channel channel = savePublicChannel("pub");

            saveMessage("m1", user, channel);
            Thread.sleep(5);
            saveMessage("m2", user, channel);
            Thread.sleep(5);
            Message latest = saveMessage("m3", user, channel);

            Pageable pageable = PageRequest.of(0, 2);

            // when
            List<Message> result = messageRepository.findLatestByChannelId(channel.getId(), pageable);

            // then
            assertEquals(2, result.size());
            assertEquals(latest.getId(), result.get(0).getId());
            assertTrue(result.get(0).getCreatedAt().isAfter(result.get(1).getCreatedAt()));
        }

        @Test
        @DisplayName("[Fail] 메시지가 없으면 빈 리스트")
        void shouldReturnEmpty_whenNoMessages() {
            // given
            User user = saveUserWithStatus("u2", "u2@a.com", "pw");
            Channel channel = savePublicChannel("pub2");

            Pageable pageable = PageRequest.of(0, 10);

            // when
            List<Message> result = messageRepository.findLatestByChannelId(channel.getId(), pageable);

            // then
            assertTrue(result.isEmpty());
        }
    }

    @Nested
    @DisplayName("findIdsByChannelId (커스텀 쿼리)")
    class FindIdsByChannelId {

        @Test
        @DisplayName("[Success] 채널에 속한 메시지 id만 반환한다")
        void shouldReturnIds_whenExists() {
            // given
            User user = saveUserWithStatus("u3", "ids@a.com", "pw");
            Channel channel = savePublicChannel("pub3");

            Message m1 = saveMessage("m1", user, channel);
            Message m2 = saveMessage("m2", user, channel);

            // when
            List<UUID> ids = messageRepository.findIdsByChannelId(channel.getId());

            // then
            assertTrue(ids.contains(m1.getId()));
            assertTrue(ids.contains(m2.getId()));
        }

        @Test
        @DisplayName("[Fail] 채널에 메시지가 없으면 빈 리스트")
        void shouldReturnEmpty_whenNone() {
            // given
            Channel channel = savePublicChannel("pub4");

            // when
            List<UUID> ids = messageRepository.findIdsByChannelId(channel.getId());

            // then
            assertTrue(ids.isEmpty());
        }
    }

    @Nested
    @DisplayName("findAllByChannelId (Slice + cursor)")
    class FindAllByChannelIdSlice {

        @Test
        @DisplayName("[Success] cursor보다 과거(createdAt < cursor) 메시지만 Slice로 조회된다")
        void shouldReturnSlice_beforeCursor() throws Exception {
            // given
            User user = saveUserWithStatus("u4", "slice@a.com", "pw");
            Channel channel = savePublicChannel("pub5");

            Message m1 = saveMessage("m1", user, channel);
            assertNotNull(m1.getCreatedAt());
            Thread.sleep(5);
            Message m2 = saveMessage("m2", user, channel);
            assertNotNull(m2.getCreatedAt());

            Instant futureCursor = Instant.now().plusSeconds(60);
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<Message> slice = messageRepository.findAllByChannelId(channel.getId(), pageable, futureCursor);

            // then
            assertEquals(2, slice.getContent().size());
            assertTrue(slice.getContent().stream().allMatch(m -> m.getCreatedAt().isBefore(futureCursor)));
        }

        @Test
        @DisplayName("[Fail] cursor가 너무 과거면 아무것도 조회되지 않는다")
        void shouldReturnEmpty_whenCursorTooOld() {
            // given
            User user = saveUserWithStatus("u5", "slice2@a.com", "pw");
            Channel channel = savePublicChannel("pub6");

            saveMessage("m1", user, channel);

            Instant pastCursor = Instant.parse("2000-01-01T00:00:00Z");
            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<Message> slice = messageRepository.findAllByChannelId(channel.getId(), pageable, pastCursor);

            // then
            assertTrue(slice.getContent().isEmpty());
        }
    }
}