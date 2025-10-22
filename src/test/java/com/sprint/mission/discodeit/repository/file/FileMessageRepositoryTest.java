package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class FileMessageRepositoryTest {
    private UserDto userDtoSample = new UserDto("Sample", "SampleNick","SampleEmail" , true);
    private MessageDto messageDtoSample=new MessageDto("Hello",userDtoSample , false);
    private FileMessageRepository fileMessageRepository = new FileMessageRepository();
    private User DEFAULT_SENDER = new User(UUID.randomUUID(), "DeletedUser", "DeletedUser", "codeit.org", true);
    @BeforeEach
    void setUp() {
        fileMessageRepository.saveMessage(messageDtoSample);
    }

    @Test
    @DisplayName("saveMessage Test")
    void saveMessage() {

        boolean expectedResult=fileMessageRepository.getMessage(messageDtoSample)!=null;
        //then
        assertTrue(expectedResult);
    }

    @Test
    @DisplayName("deleMessage Test")
    void deleteMessage() {
        //when
        fileMessageRepository.deleteMessage(messageDtoSample);
        //then
        boolean expectedResult=fileMessageRepository.getMessage(messageDtoSample)==null;
        assertTrue(expectedResult);

    }

    @Test
    @DisplayName("updateMessage Test")
    void updateMessage() {
        //when
        fileMessageRepository.updateMessage(messageDtoSample, Message.messageElement.CONTENT, "Updated");
        //then
        MessageDto actualResult = fileMessageRepository.getMessage(messageDtoSample);
        assertEquals("Updated",actualResult.getContent());
    }

    @Test
    @DisplayName("setDefaultSender Test")
    void setDefaultSender() {
        //when
        fileMessageRepository.setDefaultSender(messageDtoSample);
        //then
        UserDto actualResult = fileMessageRepository.getMessage(messageDtoSample).getSender();
        assertEquals(DEFAULT_SENDER.getName(),actualResult.getName());

    }
}