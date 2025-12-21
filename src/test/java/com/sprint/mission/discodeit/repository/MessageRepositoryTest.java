package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.assertThat;


@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
class MessageRepositoryTest {
    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    private UserRepository userRepository;

    @Autowired
    private ChannelRepository channelRepository;

    @Test
    @DisplayName("findByChannelId 성공: 채널에 속한 메세지만 조회")
    void findByChannelId_success() {
        // given
        User author = userRepository.save(
                new User("user", "password1234", "user@naver.com", null));
        Channel channel1 = channelRepository.save(
                new Channel(ChannelType.PUBLIC, "channel1", false, 0, null));
        Channel channel2 = channelRepository.save(
                new Channel(ChannelType.PUBLIC, "channel2", false, 0, null));
        Message message1 = messageRepository.save(new Message("message1", author, channel1, List.of()));
        Message message2 = messageRepository.save(new Message("message2", author, channel1, List.of()));
        messageRepository.save(new Message("no content", author, channel2, List.of()));


        // when
        List<Message> result = messageRepository.findByChannelId(channel1.getId());

        // then
        assertThat(result).extracting(message -> message.getId())
                .contains(message1.getId(), message2.getId());
        assertThat(result).allMatch(message -> message.getChannel().getId().equals(channel1.getId()));

    }

    @Test
    @DisplayName("findByChannelId 실패: 존재하지 않는 채널이면 empty")
    void findByChannelId_fail() {
        // when
        List<Message> result = messageRepository.findByChannelId(UUID.randomUUID());

        // then
        assertThat(result).isEmpty();

    }

    @Test
    @DisplayName("findByContentContainingIgnoreCase 성공: 대소문자 무시하고 검색")
    void findByContentContainingIgnoreCase_success() {
        // given
        User author = userRepository.save(
                new User("user", "password1234", "user@naver.com", null));
        Channel channel = channelRepository.save(
                new Channel(ChannelType.PUBLIC, "channel", false, 0, null));
        messageRepository.save(new Message("message1", author, channel, List.of()));
        messageRepository.save(new Message("HI message2", author, channel, List.of()));
        messageRepository.save(new Message("hi message3", author, channel, List.of()));

        // when
        List<Message> result = messageRepository.findByContentContainingIgnoreCase("hi");

        // then
        assertThat(result).hasSize(2);
        assertThat(result).allMatch(message -> message.getContent().toLowerCase().contains("hi"));

    }

    @Test
    @DisplayName("findByContentContainingIgnoreCase 실패: 해당하는 키워드가 없으면 empty")
    void findByContentContainingIgnoreCase_fail() {
        // given
        User author = userRepository.save(
                new User("user", "password1234", "user@naver.com", null));
        Channel channel = channelRepository.save(
                new Channel(ChannelType.PUBLIC, "channel", false, 0, null));
        messageRepository.save(new Message("message", author, channel, List.of()));

        // when
        List<Message> result = messageRepository.findByContentContainingIgnoreCase("hi");

        // then
        assertThat(result).isEmpty();

    }

    @Test
    @DisplayName("findByChannelIdOrderByCreatedAtDesc 성공: 페이징 / 정렬 동작 확인")
    void findByChannelIdOrderByCreatedAtDesc_success() {
        // given
        User author = userRepository.save(
                new User("user", "password1234", "user@naver.com", null));
        Channel channel = channelRepository.save(
                new Channel(ChannelType.PUBLIC, "channel", false, 0, null));
        messageRepository.save(new Message("m1", author, channel, List.of()));
        messageRepository.save(new Message("m2", author, channel, List.of()));
        messageRepository.save(new Message("m3", author, channel, List.of()));

        Pageable pageable = PageRequest.of(0, 2);

        // when
        Slice<Message> slice0 = messageRepository.findByChannelIdOrderByCreatedAtDesc(channel.getId(), pageable);
        Slice<Message> slice1 = messageRepository.findByChannelIdOrderByCreatedAtDesc(channel.getId(), PageRequest.of(1, 2));

        // then
        assertThat(slice0.getContent()).hasSize(2);
        assertThat(slice0.hasNext()).isTrue();

        assertThat(slice1.getContent()).hasSize(1);
        assertThat(slice1.hasNext()).isFalse();


    }
}
