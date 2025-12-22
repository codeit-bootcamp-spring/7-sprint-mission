package com.sprint.mission.discodeit.message.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.time.Duration;
import java.time.Instant;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("Message Repository Slice Test")
@EnableJpaAuditing
public class MessageRepositorySliceTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private UserRepository userRepository;

    private Message message;
    private Channel channel;
    private User user;

    @BeforeEach
    void setUp() {
        channel = Channel.publicChannelFactory("publicChannel","publicChannelDesc");
        user = User.createUserFactory(
                "user1","111@user","1234"
        );
        message = Message.createMessageFactory(
                "siuuu",user,channel
        );
    }
    @Test
    @DisplayName("[정상 케이스] 페이지네이션 메세지 페이지 생성 성공")
    void createMessagePage_Success() {

        //given
        userRepository.save(user);
        channelRepository.save(channel);
        messageRepository.save(message);

        Pageable page = Pageable.ofSize(5);

        Page<Message> actualResult = messageRepository.findByChannelId(channel.getId(), page);

        assertThat(actualResult.getContent().size()).isEqualTo(1);
        assertThat(actualResult.getContent()).contains(message);


    }

    @Test
    @DisplayName("[예외 케이스] 페이지네이션 메세지 페이지 생성 실패")
    void createMessagePage_Fail() {

        //given
        userRepository.save(user);
        channelRepository.save(channel);
        messageRepository.save(message);

        Pageable page = Pageable.ofSize(5);

        Page<Message> actualResult = messageRepository.findByChannelId(null, null);

        assertThat(actualResult).isEqualTo(Page.empty());

    }


    @Test
    @DisplayName("[정상 케이스] 커서 메세지 페이지 생성 성공")
    void createCursorMessagePage_Success() {
        //given
        userRepository.save(user);
        channelRepository.save(channel);
        messageRepository.save(message);

        Pageable page = Pageable.ofSize(5);
        Instant temp = Instant.now().minus(Duration.ofDays(1));

        Slice<Message> actualResult = messageRepository.
                findByChannelIdAndCreatedAtAfter(channel.getId(), temp, page);

        assertThat(actualResult).isNotEmpty();
        assertThat(actualResult.getContent()).contains(message);
    }

    @Test
    @DisplayName("[예외 케이스] 커서 메세지 페이지 생성 실패")
    void createCursorMessagePage_Fail() {

        userRepository.save(user);
        channelRepository.save(channel);
        messageRepository.save(message);

        Slice<Message> actualResult = messageRepository.
                findByChannelIdAndCreatedAtAfter(channel.getId(), null, null);

        assertThat(actualResult).isEmpty();

    }


}
