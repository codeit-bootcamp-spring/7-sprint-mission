package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.domain.Sort;

import java.util.ArrayList;
import java.util.Comparator;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@DisplayName("메시지 레포지토리 슬라이스 테스트")
class MessageRepositoryTest {

    @Autowired
    private MessageRepository messageRepository;

    @Autowired
    TestEntityManager em;

    @Test
    @DisplayName("정상적으로 페이지네이션을 적용하여 메시지를 조회할 수 있다")
    void findMessage_Success() {
        // given
        int pageSize = 50;


        Channel channel = new Channel("channel", ChannelType.PUBLIC, "des");
        em.persist(channel);

        for (int i = 0; i < 60; i++) {
            Message message =
                    new Message(
                            "CONTENT" + i,
                            channel,
                            null,
                            new ArrayList<>()
                    );
            em.persist(message);
        }
        em.flush();

        Pageable pageable = PageRequest.of(
                0,
                pageSize,
                Sort.by(Sort.Direction.DESC, "createdAt")
        );

        // when
        Slice<Message> response = messageRepository.findAllByChannelId(channel.getId(), pageable);

        // then
        assertThat(response.getContent()).hasSize(pageSize);
        assertThat(response.hasNext()).isTrue();
        assertThat(response.getContent())
                .isSortedAccordingTo(
                        Comparator.comparing(Message::getCreatedAt).reversed()
                );
    }
}