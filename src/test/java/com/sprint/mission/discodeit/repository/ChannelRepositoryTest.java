package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.config.JpaAuditingConfig;
import com.sprint.mission.discodeit.config.QuerydslConfig;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@ActiveProfiles("test") // active 설정이 따로 있으면 넣어줘야 함
@Import({ QuerydslConfig.class, JpaAuditingConfig.class })
@DataJpaTest
@DisplayName("channelRepository 테스트")
class ChannelRepositoryTest {

    @Autowired
    ChannelRepository channelRepository;

    @Test
    @DisplayName("채널 타입 조회 성공")
    void findAllByType_public() {
        // given
        Channel publicChannel1 = new Channel("public-1", "desc", ChannelType.PUBLIC);
        Channel publicChannel2 = new Channel("public-2", "desc", ChannelType.PUBLIC);
        Channel privateChannel = new Channel("private-1", "desc", ChannelType.PRIVATE);

        channelRepository.save(publicChannel1);
        channelRepository.save(publicChannel2);
        channelRepository.save(privateChannel);

        // when
        List<Channel> result = channelRepository.findAllByType(ChannelType.PUBLIC);

        // then
        assertThat(result).hasSize(2);
        assertThat(result)
                .extracting(Channel::getType)
                .containsOnly(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("타입 조회 시 데이터 없음")
    void findAllByType_public_empty() {
        // given
        Channel privateChannel1 = new Channel("private-1", "desc", ChannelType.PRIVATE);
        Channel privateChannel2 = new Channel("private-2", "desc", ChannelType.PRIVATE);

        channelRepository.save(privateChannel1);
        channelRepository.save(privateChannel2);

        // when
        List<Channel> result = channelRepository.findAllByType(ChannelType.PUBLIC);

        // then
        assertThat(result).isEmpty();
    }
}
