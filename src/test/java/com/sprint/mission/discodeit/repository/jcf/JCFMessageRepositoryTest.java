package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JCFMessageRepositoryTest {
    private User DEFAULT_SENDER = new User(UUID.randomUUID(), "DeletedUser", "DeletedUser", "codeit.org", true);
    private JCFMessageRepository jcfMessageRepository;
    private UserDto userDtoSample = new UserDto("Sample", "SampleNick","SampleEmail" , true);
    private MessageDto messageDtoSample=new MessageDto("Hello",userDtoSample , false);
    @BeforeEach
    void setUp() {

        jcfMessageRepository = new JCFMessageRepository();
        jcfMessageRepository.saveMessage(messageDtoSample);
    }

    @Test
    @DisplayName("getMessageById Test")
    void getMessageById() {
        //when
        MessageDto actualResult = jcfMessageRepository.getMessageById(messageDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getId()).isEqualTo(messageDtoSample.getId());
    }

    @Test
    @DisplayName("saveMessage Test")
    void saveMessage() {
        //
        MessageDto actualResult = jcfMessageRepository.getMessageById(messageDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getId()).isEqualTo(messageDtoSample.getId());

    }

    @Test
    @DisplayName("deleteMessage Test")
    void deleteMessage() {

        //when
        jcfMessageRepository.deleteMessage(messageDtoSample);
        Boolean expectedResult= jcfMessageRepository.getMessageById(messageDtoSample.getId()) == null;
        //then
        Assertions.assertThat(expectedResult).isTrue();
    }

    @Test
    @DisplayName("updateMessage Test")
    void updateMessage() {
        //given
        jcfMessageRepository.saveMessage(messageDtoSample);
        //when
        jcfMessageRepository.updateMessage(messageDtoSample, Message.messageElement.CONTENT, "Updated");
        MessageDto actualResult = jcfMessageRepository.getMessageById(messageDtoSample.getId());
        //then
        Assertions.assertThat(actualResult.getContent()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("setDefaultSender Test")
    void setDefaultSender() {
        //when
        jcfMessageRepository.setDefaultSender(messageDtoSample);

        UserDto actualResult = jcfMessageRepository.getMessage(messageDtoSample).getSender();
        //then
        Assertions.assertThat(actualResult.getName()).isEqualTo(DEFAULT_SENDER.getName());
    }
}