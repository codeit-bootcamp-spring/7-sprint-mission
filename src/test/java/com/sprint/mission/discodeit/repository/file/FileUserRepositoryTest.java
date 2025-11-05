package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class FileUserRepositoryTest {
    private UserDto userDtoSample = new UserDto("Sample", "SampleNick","SampleEmail" , true);
    private MessageDto messageDtoSample=new MessageDto("Hello",userDtoSample , false);
    private ChannelDto channelDtoSample=new ChannelDto("Sample","SampleChannel",true,true);
    private FileUserRepository fileUserRepository = new FileUserRepository();


    @BeforeEach
    void setUp() {
        fileUserRepository.saveUser(userDtoSample);
    }

    @Test
    @DisplayName("addChannelToUser Test")
    void addChannelToUser() {
        //when
        fileUserRepository.addChannelToUser(userDtoSample,channelDtoSample);
        //then
        UserDto actualResult = fileUserRepository.getUser(userDtoSample);
        assertTrue(actualResult.getChannelDtoList().contains(channelDtoSample));
    }

    @Test
    @DisplayName("deleteChannelFromUser Test")
    void deleteChannelFromUser() {
        //given
        fileUserRepository.addChannelToUser(userDtoSample,channelDtoSample);
        //when
        fileUserRepository.deleteChannelFromUser(userDtoSample,channelDtoSample);
        //then
        UserDto actualResult = fileUserRepository.getUser(userDtoSample);
        assertFalse(actualResult.getChannelDtoList().contains(channelDtoSample));
    }

    @Test
    @DisplayName("saveUser Test")
    void saveUser() {
        //then
        boolean expectedResult = fileUserRepository.getUser(userDtoSample) != null;
        assertTrue(expectedResult);
    }


    @Test
    @DisplayName("deleteUser Test")
    void deleteUser() {
        //when
        fileUserRepository.deleteUser(userDtoSample);
        //then
        UserDto actualResult = fileUserRepository.getUser(userDtoSample);
        assertNull(actualResult);
    }

    @Test
    @DisplayName("updateUser Test")
    void updateUser() {
        //when
        fileUserRepository.updateUser(userDtoSample, User.userElement.NAME, "Updated");
        UserDto actualResult = fileUserRepository.getUser(userDtoSample);
        //then
        assertEquals("Updated",actualResult.getName());
    }
}