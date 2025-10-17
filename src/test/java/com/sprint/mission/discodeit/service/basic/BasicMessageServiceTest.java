package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.entity.Message;

import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ValidateService;
import com.sprint.mission.discodeit.service.util.ValidateOperator;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.assertj.core.api.Assertions.*;
import static org.junit.jupiter.api.Assertions.*;

class BasicMessageServiceTest {
    private MessageRepository repository;
    private ValidateService validateService;
    private UserRepository userRepository;
    private ChannelRepository channelRepository;
    private BasicMessageService basicMessageService;
    private MessageDto messageDtoSample;
    private UserDto userDtoSample;


    @BeforeEach
    void setUp() {
        userDtoSample = new UserDto("황준영","hwang","genius5375@gmail.com",true);
       messageDtoSample= new MessageDto("Hello", userDtoSample, false);
        userRepository = new FileUserRepository();
         channelRepository = new FileChannelRepository();
        repository = new FileMessageRepository();
         validateService = new ValidateOperator(channelRepository,userRepository,repository);

         basicMessageService = new BasicMessageService(repository,validateService);

    }

    @Test
    @DisplayName("createMessage Test")
    void createMessage() {
        //given
        userRepository.saveUser(userDtoSample);
        //when
        basicMessageService.createMessage(messageDtoSample);
        Boolean expectedResult= repository.getMessage(messageDtoSample) != null;
        //then
        assertThat(expectedResult).isTrue();
    }

    @Test
    @DisplayName("deleteMessage Test")
    void deleteMessage() {
        //given
        userRepository.saveUser(userDtoSample);
        basicMessageService.createMessage(messageDtoSample);
        //when
        basicMessageService.deleteMessage(messageDtoSample);
        Boolean expectedResult= repository.getMessage(messageDtoSample) == null;
        //then
        assertThat(expectedResult).isTrue();
    }

    @Test
    @DisplayName("updateMessage Test")
    void updateMessage() {
        //given
        userRepository.saveUser(userDtoSample);
        basicMessageService.createMessage(messageDtoSample);
        //when
        basicMessageService.updateMessage(messageDtoSample, Message.messageElement.CONTENT,"Updated");
        MessageDto actualResult = repository.getMessage(messageDtoSample);
        //then
        assertThat(actualResult.getContent()).isEqualTo("Updated");
    }
}