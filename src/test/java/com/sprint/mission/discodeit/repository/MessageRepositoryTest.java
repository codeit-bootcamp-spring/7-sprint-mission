package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.role.ChannelType;
import com.sprint.mission.discodeit.entity.role.Role;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

import java.time.Instant;
import java.util.List;
import java.util.Optional;

import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("MessageRepository Test")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;
    @Autowired
    private UserRepository userRepository;
    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("커서 기반 페이징")
    void findAllByChannelId_CursorPaging() {
        // given
        User user =  userRepository.save(new User("user@codeit.com", "Password1!","user", Role.USER));
        User user2 = userRepository.save(new User("user2@codeit.com", "Password1!","user2", Role.USER));
        Channel channel = channelRepository.save(new Channel("test", "test", ChannelType.PUBLIC));

        // createdAt 문제
        messageRepository.save(new Message("1번", channel, user, null));
        for(int i = 0; i < 1000000000; i++);
        messageRepository.save(new Message("2번", channel, user, null));
        for(int i = 0; i < 1000000000; i++);
        messageRepository.save(new Message("3번", channel, user2, null));

        // when
        Pageable pageable = PageRequest.of(0, 5);
        Instant now = Instant.now();
        List<Message> found
                = messageRepository.findAllByChannelId(channel.getId(), now, pageable);

        // then
        assertThat(found.get(0).getAuthor().getUsername()).isEqualTo("user2");
        assertThat(found.get(2).getContent()).isEqualTo("1번");
    }

    @Test
    @DisplayName("가장 최근 메시지를 조회한다.")
    void findTopByChannelIdOrderByCreatedAtDesc() {
        // given
        User user =  userRepository.save(new User("user@codeit.com", "Password1!","user", Role.USER));
        User user2 = userRepository.save(new User("user2@codeit.com", "Password1!","user2", Role.USER));
        Channel channel = channelRepository.save(new Channel("test", "test", ChannelType.PUBLIC));

        messageRepository.save(new Message("1번", channel, user, null));
        for(int i = 0; i < 1000000000; i++);
        messageRepository.save(new Message("2번", channel, user, null));
        for(int i = 0; i < 1000000000; i++);
        messageRepository.save(new Message("3번", channel, user2, null));

        // when
        Optional<Message> found =  messageRepository.findTopByChannelIdOrderByCreatedAtDesc(channel.getId());

        // then
        assertThat(found).isPresent();
        assertThat(found.get().getAuthor().getUsername()).isEqualTo("user2");
        assertThat(found.get().getContent()).isEqualTo("3번");

    }
}