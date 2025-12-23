package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.enums.ChannelType;
import jakarta.persistence.EntityManager;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@EnableJpaAuditing
@DisplayName("Channel Repository 테스트")
class ChannelRepositoryTest {
    @Autowired
    private ChannelRepository channelRepository;

    @Autowired
    private EntityManager entityManager;

    private Channel channel1;
    private Channel channel2;
    private Channel channel3;
    private Channel channel4;
    private Channel channel5;


    @Nested
    @DisplayName("channelIds(참여 채널들) + PUBLIC 채널 조회 테스트")
    class ChannelReadPublicORInIds {
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

            channel3 = Channel.builder()
                    .type(ChannelType.PRIVATE)
                    .build();

            channel4 = Channel.builder()
                    .type(ChannelType.PRIVATE)
                    .build();

            channel5 = Channel.builder()
                    .type(ChannelType.PRIVATE)
                    .build();
        }

        @Test
        @DisplayName("[정상 케이스] - Public Channel과 참여중인 채널(Private) 조회 성공")
        void findAllChannel_publicOrChannelIds_success() {
            // given
            channelRepository.saveAll(List.of(channel1, channel2, channel3, channel4, channel5));
            entityManager.flush();
            entityManager.clear();

            // when
            List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of(channel3.getId(), channel4.getId()));

            // then
            assertThat(result.size()).isEqualTo(4);

        }

        @Test
        @DisplayName("[정상 케이스] - 참여중인 Private Channel 없는 경우")
        void findAllChannel_public_notFoundPrivateChannel_success() {
            // given
            channelRepository.saveAll(List.of(channel1, channel2, channel3, channel4, channel5));
            entityManager.flush();
            entityManager.clear();

            // when
            List<Channel> result = channelRepository.findAllByTypeOrIdIn(ChannelType.PUBLIC, List.of());

            // then
            assertThat(result.size()).isEqualTo(2);
            result.forEach(channel -> assertThat(channel.getType()).isEqualTo(ChannelType.PUBLIC));

        }
    }
}