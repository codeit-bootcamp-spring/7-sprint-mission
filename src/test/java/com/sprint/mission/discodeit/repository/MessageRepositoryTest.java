package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.enum_.ChannelType;
import java.time.Instant;
import java.util.Optional;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

@DataJpaTest
@ActiveProfiles("test")
@Import(JpaAuditingConfig.class)
@DisplayName("메세지 Repository 테스트")
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;
  @Autowired
  private ChannelRepository channelRepository;
  @Autowired
  private UserRepository userRepository;

  private User user;
  private Channel channel;

  @BeforeEach
  void setUp() {

    user = userRepository.save(
        new User("진우", "a@a.com", "1234", null));
    channel = channelRepository.save(
        new Channel(ChannelType.PUBLIC, "공개 채널", "채널 설명"));
  }

  @Nested
  @DisplayName("채널별 메시지 조회")
  class FindChannelMessages {

    @Test
    @DisplayName("채널 ID로 메시지 목록 페이징 할 수 있다")
    void findByChannelId_Success() {
      // given
      messageRepository.save(
          new Message("메세지 테스트", channel, user, null));

      Pageable pageable = PageRequest.of(0, 10);

      // when
      Slice<Message> byChannelId = messageRepository.findByChannelId(channel.getId(), pageable);

      // then
      assertThat(byChannelId.getContent()).hasSize(1);
      assertThat(byChannelId.getContent().get(0).getContent()).isEqualTo("메세지 테스트");
      assertThat(byChannelId.getContent().get(0).getChannel().getId()).isEqualTo(channel.getId());
    }

    @Test
    @DisplayName("메시지 없는 채널 조회 시 빈 페이징 반환")
    void findByChannelId_NotFound() {
      // given
      Pageable pageable = PageRequest.of(0, 10);

      // when
      Slice<Message> byChannelId = messageRepository.findByChannelId(channel.getId(), pageable);

      // then
      assertThat(byChannelId.getContent()).isEmpty();
    }
  }

  @Nested
  @DisplayName("마지막 읽은 시간 조회")
  class FindLastReadTime {

    @Test
    @DisplayName("마지막 메시지 읽은 시간을 조회 할 수 있다")
    void findLastReadTime_Success() {
      // given

      Message message = new Message("메세지 테스트", channel, user, null);
      messageRepository.save(message);

      // when
      Optional<Instant> lastTime = messageRepository.findLastMessageAtByChannelId(
          channel.getId());

      // then
      assertThat(lastTime).isPresent();
      assertThat(lastTime.get()).isNotNull();
    }

    @Test
    @DisplayName("메시지가 없는 채널 조회 시 Optional 반환")
    void findLastReadTime_NotFound() {
      // when
      Optional<Instant> lastTime = messageRepository.findLastMessageAtByChannelId(
          channel.getId());

      // then
      assertThat(lastTime).isEmpty();

    }
  }

}