package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class JCFUserRepositoryTest {
    private JCFUserRepository jcfUserRepository;
    private UserDto userDtoSample = new UserDto("Sample", "SampleNick","SampleEmail" , true);
    private MessageDto messageDtoSample=new MessageDto("Hello",userDtoSample , false);
    private ChannelDto channelDtoSample=new ChannelDto("Sample","SampleChannel",true,true);

    @BeforeEach
    void setUp() {
        jcfUserRepository = new JCFUserRepository();
        jcfUserRepository.saveUser(userDtoSample);

    }

    @Test
    @DisplayName("getUserById Test")
    void getUserById() {
    }

    @Test
    @DisplayName("saveUser Test")
    void saveUser() {
    }

    @Test
    @DisplayName("deleteUser Test")
    void deleteUser() {
    }

    @Test
    @DisplayName("updateUser Test")
    void updateUser() {
    }

    @Test
    @DisplayName("addChannelToUser Test")
    void addChannelToUser() {
        //when
        jcfUserRepository.addChannelToUser(userDtoSample,channelDtoSample);
        //then
        UserDto actualResult = jcfUserRepository.getUser(userDtoSample);
        assertTrue(actualResult.getChannelDtoList().contains(channelDtoSample));
    }

    @Test
    @DisplayName("deleteChannelFromUser Test")
    void deleteChannelFromUser() {
        //given
        jcfUserRepository.addChannelToUser(userDtoSample,channelDtoSample);
        //when
        jcfUserRepository.deleteChannelFromUser(userDtoSample,channelDtoSample);
        //then
        UserDto actualResult = jcfUserRepository.getUser(userDtoSample);
        assertFalse(actualResult.getChannelDtoList().contains(channelDtoSample));
    }
}