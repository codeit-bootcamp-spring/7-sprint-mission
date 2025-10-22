package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.deletedCash.DeletedChannel;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.*;

import java.util.ArrayList;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JCFChannelRepositoryTest {


    private JCFChannelRepository jcfChannelRepository;
    private ChannelDto channelDtoSample;
    private UserDto userDtoSample;
    @BeforeEach
    void setUp() {
        jcfChannelRepository = new JCFChannelRepository();
        channelDtoSample = new ChannelDto("Sample","SampleChannel",true,true);
        userDtoSample = new UserDto("SampleUser", "SampleNick","SampleEmail" , true );
    }
    @Test
    @DisplayName("getChannelById Test")
    void getChannelById() {
        //given
        jcfChannelRepository.saveChannel(channelDtoSample);
        //when
        ChannelDto actualResult = jcfChannelRepository.getChannelById(channelDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getId()).isEqualTo(channelDtoSample.getId());
    }

    @Test
    @DisplayName("saveChannel Test")
    void saveChannel() {
        //when
        jcfChannelRepository.saveChannel(channelDtoSample);
        //then
        Assertions.assertThat(jcfChannelRepository.getChannelById(channelDtoSample.getId())).isNotNull();
    }

    @Test
    @DisplayName("deleteChannel Test")
    void deleteChannel() {
        //given
        jcfChannelRepository.saveChannel(channelDtoSample);
        //when
        jcfChannelRepository.deleteChannel(channelDtoSample);
       boolean actualResult = jcfChannelRepository.getChannelById(channelDtoSample.getId())== null;
        //then
        Assertions.assertThat(actualResult).isTrue();
    }

    @Test
    @DisplayName("updateChannel Test")
    void updateChannel() {
        //given
        jcfChannelRepository.saveChannel(channelDtoSample);
        //when
        jcfChannelRepository.updateChannel(channelDtoSample, Channel.channelElement.NAME,"Updated");
        ChannelDto actualResult = jcfChannelRepository.getChannelById(channelDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getName()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("addUserToChannel Test")
    void addUserToChannel() {
        //given
        jcfChannelRepository.saveChannel(channelDtoSample);
        //when
        jcfChannelRepository.addUserToChannel(userDtoSample,channelDtoSample);
        ChannelDto actualResult = jcfChannelRepository.getChannelById(channelDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getUserDtoList().size()).isEqualTo(1);
    }

    @Test
    @DisplayName("deleteUserFromChannel Test")
    void deleteUserFromChannel() {
        //given
        jcfChannelRepository.saveChannel(channelDtoSample);
        jcfChannelRepository.addUserToChannel(userDtoSample,channelDtoSample);
        //when
        jcfChannelRepository.deleteUserFromChannel(userDtoSample,channelDtoSample);
        ChannelDto actualResult = jcfChannelRepository.getChannelById(channelDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getUserDtoList().size()).isEqualTo(0);
    }
}