package com.sprint.mission.discodeit.repository;

import org.junit.jupiter.api.DisplayName;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;

@DataJpaTest
@EnableJpaAuditing
@DisplayName("ChannelRepository Test")
class ChannelRepositoryTest {

    @Autowired
    private ChannelRepository channelRepository;
}