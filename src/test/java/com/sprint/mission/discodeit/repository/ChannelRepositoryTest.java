package com.sprint.mission.discodeit.repository;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.*;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.enum_.ChannelType;
import java.util.Optional;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.test.context.ActiveProfiles;


@DataJpaTest
@ActiveProfiles("test")
@DisplayName("채널 Repository 테스트")
class ChannelRepositoryTest {

  @Autowired
  private ChannelRepository channelRepository;

  @Nested
  @DisplayName("공개 채널 조회 관련")
  class Search {

    @Test
    @DisplayName("채널명과 타입으로 공개 채널을 찾을 수 있다")
    void findByChannelNameAndType_Success() {
      // given
      Channel saved = channelRepository.save(new Channel(ChannelType.PUBLIC, "공개 채널", "채널 설명"));

      // when
      Optional<Channel> foundChannel = channelRepository.findByNameAndType(saved.getName(),
          ChannelType.PUBLIC);

      // then
      assertThat(foundChannel).isPresent();
      assertThat(foundChannel.get().getName()).isEqualTo("공개 채널");
      assertThat(foundChannel.get().getType()).isEqualTo(ChannelType.PUBLIC);
    }

    @Test
    @DisplayName("존재하지 않은 채널 조회 시 Optional 반환")
    void findByChannelNameAndType_NotFound() {
      // when
      Optional<Channel> foundChannel = channelRepository.findByNameAndType("없는 채널",
          ChannelType.PUBLIC);

      // then
      assertThat(foundChannel).isEmpty();

    }
  }
}