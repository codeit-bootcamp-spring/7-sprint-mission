package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.env.Environment;

import static org.junit.jupiter.api.Assertions.*;

class FileChannelRepositoryTest {
//    private FileChannelRepository fileChannelRepository = new FileChannelRepository();
//    private ChannelDto channelDtoSample = new ChannelDto("Sample","SampleChannel",true,true);
//    private UserDto userDtoSample = new UserDto("SampleUser", "SampleNick","SampleEmail" , true );
//    @BeforeEach
//    void setUp() {
//        fileChannelRepository.saveChannel(channelDtoSample);
//    }
//
//    @Test
//    @DisplayName("getChannelById Test")
//    void getChannelById() {
//        //when
//        ChannelDto actualResult = fileChannelRepository.getChannelById(channelDtoSample.getId());
//
//        //then
//        Assertions.assertThat(actualResult.getId()).isEqualTo(channelDtoSample.getId());
//    }
//
//    @Test
//    @DisplayName("deleteChannel Test")
//    void deleteChannel() {
//        //when
//        fileChannelRepository.deleteChannel(channelDtoSample);
//        //then
//        boolean expectedResult = fileChannelRepository.getChannelById(channelDtoSample.getId()) == null;
//        Assertions.assertThat(expectedResult).isTrue();
//    }
//
//    @Test
//    @DisplayName("updateChannel Test")
//    void updateChannel() {
//        //when
//        fileChannelRepository.updateChannel(channelDtoSample, Channel.channelElement.NAME,"Updated");
//        ChannelDto actualResult = fileChannelRepository.getChannelById(channelDtoSample.getId());
//        //then
//        Assertions.assertThat(actualResult.getName()).isEqualTo("Updated");
//    }
//
//    @Test
//    @DisplayName("addUserToChannel Test")
//    void addUserToChannel() {
//        //when
//        fileChannelRepository.addUserToChannel(userDtoSample,channelDtoSample);
//        //then
//        boolean expectedResult = fileChannelRepository.
//                getChannelById(channelDtoSample.getId()).
//                getUserDtoList().
//                contains(userDtoSample);
//        Assertions.assertThat(expectedResult).isTrue();
//    }
//
//    @Test
//    @DisplayName("deleteUserFromChannel Test")
//    void deleteUserFromChannel() {
//        //given
//        fileChannelRepository.addUserToChannel(userDtoSample,channelDtoSample);
//        //when
//        fileChannelRepository.deleteUserFromChannel(userDtoSample,channelDtoSample);
//        ChannelDto actualResult = fileChannelRepository.getChannelById(channelDtoSample.getId());
//        //then
//        Assertions.assertThat(actualResult.getUserDtoList().contains(userDtoSample)).isFalse();
//    }
//
//    @Test
//    @DisplayName("saveChannel Test")
//    void saveChannel() {
//        boolean actualResult = fileChannelRepository.getChannelById(channelDtoSample.getId()) != null;
//        Assertions.assertThat(actualResult).isTrue();
//    }
}