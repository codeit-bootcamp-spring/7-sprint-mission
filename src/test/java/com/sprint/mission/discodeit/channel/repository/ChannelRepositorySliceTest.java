package com.sprint.mission.discodeit.channel.repository;

import com.sprint.mission.discodeit.config.TestConfiguration;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.exception.domain.channel.ChannelNotExistException;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.context.annotation.Import;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.data.jpa.repository.config.EnableJpaAuditing;
import org.springframework.test.context.ActiveProfiles;

import java.util.Optional;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;
import static org.assertj.core.api.AssertionsForClassTypes.assertThat;

@DataJpaTest
@DisplayName("Channel Repository slice test")
@ActiveProfiles("test")
public class ChannelRepositorySliceTest {

    @Autowired
    private ChannelRepository channelRepository;

    private User user;
    private Channel channel;

    @BeforeEach
    void setUp() {
    user = User.createUserFactory("user1","111@user","1234");
    channel = Channel.publicChannelFactory("publicChannel","publicChannelDesc");

    }


    @Test
    @DisplayName("[정상 케이스] 채널 저장 성공")
    void saveChannel_Success() {

        Channel targetChannel = channelRepository.save(channel);

        assertThat(channelRepository.findById(targetChannel.getId())).isPresent();

    }

    @Test
    @DisplayName("[예외 케이스] 채널 저장 실패")
    void saveChannel_Fail(){
        assertThatThrownBy(()->channelRepository.save(null))
                .isInstanceOf(InvalidDataAccessApiUsageException.class);
    }

    @Test
    @DisplayName("[정상 케이스] id로 조회 성공")
    void findById_Success(){
        Channel targetChannel = channelRepository.save(channel);
        Optional<Channel> byId = channelRepository.findById(targetChannel.getId());

        assertThat(byId.isPresent()).isTrue();
        assertThat(byId.get()).isEqualTo(targetChannel);

    }

    @Test
    @DisplayName("[예외 케이스] id로 조회 실패")
    void findById_Fail(){

        channelRepository.save(channel);

        Optional<Channel> byId = channelRepository.findById(UUID.randomUUID());

        assertThat(byId).isNotPresent();
    }



}
