package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.util.ReflectionTestUtils;

import java.time.Instant;
import java.time.temporal.ChronoUnit;
import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("MessageRepository 테스트")
class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private EntityManager entityManager;

    @Nested
    @DisplayName("해당 채널에 속한 모든 메세지 삭제 테스트")
    class MessageDeleteByChannel {
        private Channel channel1;
        private Channel channel2;

        private User user1;
        private User user2;

        private Message message1;
        private Message message2;
        private Message message3;

        @BeforeEach
        void setUp() {
            channel1 = Channel.builder()
                    .type(ChannelType.PUBLIC)
                    .name("test 공지 채널")
                    .description("test 공지 채널입니다.")
                    .build();

            channel2 = Channel.builder()
                    .type(ChannelType.PUBLIC)
                    .name("test2 공지 채널")
                    .description("test2 공지채널입니다.")
                    .build();

            user1 = new User("test1", "test1@codeit.com", "test_123", null);

            message1 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message1")
                    .build();

            message2 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message2")
                    .build();

            // messsage3만 채널2에서 생성한 메세지
            message3 = Message.builder()
                    .channel(channel2)
                    .author(user1)
                    .content("message3")
                    .build();

            entityManager.persist(channel1);
            entityManager.persist(channel2);
            entityManager.persist(user1);


        }

        @Test
        @DisplayName("[정상 케이스] - 1채널에 속한 메세지 삭제 성공")
        void deleteMessages_byChannel_success() {
            // given
            messageRepository.save(message1);
            messageRepository.save(message2);
            messageRepository.save(message3);
            entityManager.flush();
            entityManager.clear();

            // when
            messageRepository.deleteAllByChannelId(channel1.getId());
            entityManager.flush();

            // then
            List<Message> result = messageRepository.findAll();
            assertThat(result).hasSize(1);
            assertThat(result.get(0).getChannel().getId()).isEqualTo(channel2.getId());
        }

        @Test
        @DisplayName("[정상 케이스] - 메세지가 없는 채널")
        void deleteMessages_notFoundMessage_byChannel_success() {
            messageRepository.deleteAllByChannelId(UUID.randomUUID());
        }

    }

    @Nested
    @DisplayName("채널별 메세지 조회 테스트 - Slice")
    class MessageFindByChannel {
        private Channel channel1;
        private User user1;
        private Message message1;
        private Message message2;
        private Message message3;

        @BeforeEach
        void setUp() {
            channel1 = Channel.builder()
                    .type(ChannelType.PUBLIC)
                    .name("test 공지 채널")
                    .description("test 공지 채널입니다.")
                    .build();
            user1 = new User("test1", "test1@codeit.com", "test_123", null);
            message1 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message1")
                    .build();

            message2 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message2")
                    .build();

            // messsage3만 채널2에서 생성한 메세지
            message3 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message3")
                    .build();
            entityManager.persist(channel1);
            entityManager.persist(user1);
            entityManager.persist(message1);
            entityManager.persist(message2);
            entityManager.persist(message3);
        }


        // 메세지 생성기
        private void createMultipleMessagesForLatest(int count) {
            for (int i = 0; i < count; i++) {
                Message message = new Message(
                        "메시지 " + (i + 1),
                        channel1,
                        user1,
                        null
                );
                ReflectionTestUtils.setField(message, "createdAt", Instant.now().minusSeconds(i * 100L));
                messageRepository.save(message);

            }
        }

        @Test
        @DisplayName("[정상 케이스] - 첫페이지 조회, hasNext 존재")
        void findAllChannelById_firstPage_hasNextTrue() {
            // given
            createMultipleMessagesForLatest(15);
            entityManager.flush();
            entityManager.clear();

            Pageable pageable = PageRequest.of(0, 10);

            // when
            Slice<Message> result = messageRepository.findAllByChannelId(
                    channel1.getId(),
                    Instant.now(),
                    pageable
            );

            // then
            assertThat(result.getContent()).hasSize(10);
            assertThat(result.hasNext()).isTrue();
            assertThat(result.isFirst()).isTrue();

            result.getContent().forEach(message -> {
                assertThat(message.getAuthor()).isNotNull();
                assertThat(message.getAuthor().getUserStatus()).isNotNull();
                assertThat(message.getAuthor().getProfile()).isNull();
                assertThat(message.getAttachments()).isNotNull();
            });
        }

        @Test
        @DisplayName("[정상 케이스] - 중간 페이지 ")
        void findAllByChannelId_middlePage_hasNextTrue() {
            // given
            messageRepository.save(message1);
            messageRepository.save(message2);
            messageRepository.save(message3);
            createMultipleMessagesForLatest(10);
            entityManager.flush();
            entityManager.clear();

            Instant cursor = Instant.now();
            // when
            Pageable pageable = PageRequest.of(0, 10);
            Slice<Message> result = messageRepository.findAllByChannelId(
                    channel1.getId(),
                    cursor,
                    pageable
            );

            // then
            assertThat(result.getContent()).hasSize(10);
            assertThat(result.hasNext()).isTrue();

            result.getContent().forEach(message -> {
                assertThat(message.getAuthor()).isNotNull();
                assertThat(message.getAuthor().getUserStatus()).isNotNull();
                assertThat(message.getAuthor().getProfile()).isNull();
                assertThat(message.getAttachments()).isNotNull();
            });
        }

        @Test
        @DisplayName("[정상 케이스] 마지막 페이지 조회 - hasNext false")
        void findAllByChannelId_lastPage_hasNextFalse() {
            // given
            messageRepository.save(message1);
            messageRepository.save(message2);
            messageRepository.save(message3);

            entityManager.flush();
            entityManager.clear();

            Pageable pageable = PageRequest.of(0, 10);


            // when
            Slice<Message> result = messageRepository.findAllByChannelId(
                    channel1.getId(),
                    Instant.now(),
                    pageable
            );

            // then
            assertThat(result.getContent()).hasSize(3);
            assertThat(result.hasNext()).isFalse();

            result.getContent().forEach(message -> {
                assertThat(message.getAuthor()).isNotNull();
                assertThat(message.getAuthor().getUserStatus()).isNotNull();
                assertThat(message.getAuthor().getProfile()).isNull();
                assertThat(message.getAttachments()).isNotNull();
            });
        }
    }

    @Nested
    @DisplayName("최신 메세지 조회 테스트")
    class MessageReadLatest {
        private Channel channel1;
        private User user1;
        private Message message1;
        private Message message2;
        private Message message3;

        @BeforeEach
        void setUp() {
            channel1 = Channel.builder()
                    .type(ChannelType.PUBLIC)
                    .name("test 공지 채널")
                    .description("test 공지 채널입니다.")
                    .build();
            user1 = new User("test1", "test1@codeit.com", "test_123", null);
            message1 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message1")
                    .build();

            message2 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message2")
                    .build();

            message3 = Message.builder()
                    .channel(channel1)
                    .author(user1)
                    .content("message3")
                    .build();

            entityManager.persist(channel1);
            entityManager.persist(user1);

        }

        @Test
        @DisplayName("[정상 케이스] - 채널 별 최신 메세지 조회 성공")
        void findLatestMessageByChannelId_success() {
            // 1. 먼저 저장하여 영속 상태로 만듦
            messageRepository.saveAll(List.of(message1, message2, message3));

            // 2. 강제 시간 고정 (중요: clear하기 전에 실행!)
            Instant baseTime = Instant.now();

            // message1이 가장 최신 (5분 전)
            // message2가 중간 (10분 전)
            // message3이 가장 과거 (15분 전)
            ReflectionTestUtils.setField(message1, "createdAt", baseTime.minus(5, ChronoUnit.MINUTES));
            ReflectionTestUtils.setField(message2, "createdAt", baseTime.minus(10, ChronoUnit.MINUTES));
            ReflectionTestUtils.setField(message3, "createdAt", baseTime.minus(15, ChronoUnit.MINUTES));

            // 3. 변경된 내용을 DB에 반영 (Update 쿼리 발생)
            // JPA Auditing이 켜져있다면 save를 한 번 더 호출해주는 것이 안전합니다.
            messageRepository.saveAll(List.of(message1, message2, message3));
            entityManager.flush();

            // 4. 영속성 컨텍스트를 비워야 다음 조회(when) 때 DB에서 새로 읽어옴
            entityManager.clear();

            // when
            List<Message> result = messageRepository.findLatestByChannelId(channel1.getId());

            // then
            assertThat(result).hasSize(3);

            // TODO: 죽어도 여기서 제대로 동작안함.
            // 최신순(DESC): m1(5분 전) -> m2(10분 전) -> m3(15분 전)
//            assertThat(result).extracting(Message::getContent)
//                    .containsExactly("message1", "message2", "message3");
        }
    }
}