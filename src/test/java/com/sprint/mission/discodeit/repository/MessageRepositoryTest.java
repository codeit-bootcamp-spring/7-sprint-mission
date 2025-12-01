package com.sprint.mission.discodeit.repository;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertThrows;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.jdbc.AutoConfigureTestDatabase;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.dao.InvalidDataAccessApiUsageException;

@DataJpaTest // JPA TEST, Transactional 내장
@AutoConfigureTestDatabase(replace = AutoConfigureTestDatabase.Replace.NONE) // H2로 교체하지 않고 postgresql로 테스트
class MessageRepositoryTest {

  @Autowired
  private MessageRepository messageRepository;

  @Autowired
  private UserRepository userRepository;

  @Autowired
  private BinaryContentRepository binaryContentRepository;

  @Autowired
  private ChannelRepository channelRepository;

  @Test
  @DisplayName("Message 생성 성공 테스트")
  void givenUserAndChannel_whenMessageCreate_thenSuccess() {
    // given
    byte[] bytes = {0, 0};
    BinaryContent binaryContent = new BinaryContent("testFile", 10L, "*/*", bytes);
    binaryContentRepository.save(binaryContent);

    User user = new User("Test1", "test1@codeit.com", "test1234", binaryContent);
    userRepository.save(user);

    Channel channel = new Channel(ChannelType.PUBLIC, "public channel", "public description");
    channelRepository.save(channel);

    // when
    Message message1 = new Message("Message Content1", channel, user);
    Message message2 = new Message("Message Content2", channel, user);
    Message message3 = new Message("Message Content3", channel, user);

    messageRepository.save(message1);
    messageRepository.save(message2);
    messageRepository.save(message3);

    // then
    int count = messageRepository.findAll().size();
    assertEquals(3, count);
  }

  @Test
  @DisplayName("Message 생성 실패 테스트 - 존재하지 않는 유저")
  void givenUserAndChannel_whenMessageCreate_thenFail() {
    // given
    // 영속화 되지 않는 유저
    User user = new User("Test1", "test1@codeit.com", "test1234", null);
    Channel channel = new Channel(ChannelType.PUBLIC, "public channel", "public description");
    channelRepository.save(channel);

    // when

    Message message = new Message("Message Content1", channel, user);
    messageRepository.save(message);

    // then
    assertThrows(InvalidDataAccessApiUsageException.class, () -> {
      messageRepository.flush();
    });
  }

  @Test
  @DisplayName("채널 삭제 시 해당 메세지 상태 확인 테스트 - 고아 객체이므로 삭제")
  void givenMessageAndChannel_whenChannelDelete_thenMessageDelete() {
    // given
    byte[] bytes = {0, 0};
    BinaryContent binaryContent = new BinaryContent("testFile", 10L, "*/*", bytes);
    binaryContentRepository.save(binaryContent);

    User user = new User("Test1", "test1@codeit.com", "test1234", binaryContent);
    userRepository.save(user);

    Channel channel = new Channel(ChannelType.PUBLIC, "public channel", "public description");
    channelRepository.save(channel);

    Message message1 = new Message("Message Content1", channel, user);
    Message message2 = new Message("Message Content2", channel, user);
    Message message3 = new Message("Message Content3", channel, user);

    // when
    channelRepository.delete(channel);
    messageRepository.flush();
    channelRepository.flush();
    // then
    System.out.println("개수 = " + messageRepository.findAll().size());
  }

}