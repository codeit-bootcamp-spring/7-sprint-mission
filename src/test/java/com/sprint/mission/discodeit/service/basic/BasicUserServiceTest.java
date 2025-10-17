package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ValidateService;
import com.sprint.mission.discodeit.service.util.ValidateOperator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserServiceTest {
    private UserRepository userRepository;
    private ValidateService validateService;
    private ChannelRepository channelRepository;
    private MessageRepository messageRepository;
    private BasicUserService basicUserService;
    private UserDto userDtoSample;
    private ChannelDto channelDtoSample;
    private BasicChannelService basicChannelService;
    private BasicMessageService basicMessageService;
    private MessageDto messageDtoSample;
    @BeforeEach
    void setUp() {
        userRepository = new FileUserRepository();
        channelRepository = new FileChannelRepository();
        messageRepository = new FileMessageRepository();
        validateService = new ValidateOperator(channelRepository, userRepository,messageRepository );
        basicUserService = new BasicUserService(userRepository,validateService,channelRepository,messageRepository);
        basicChannelService = new BasicChannelService(channelRepository,validateService,userRepository );
        basicMessageService = new BasicMessageService(messageRepository,validateService);
        userDtoSample = new UserDto("Sample", "SampleNick","SampleEmail" , true);
        channelDtoSample = new ChannelDto("Sample","SampleChannel",true,true);
        messageDtoSample = new MessageDto("SampleMessage",userDtoSample,false);

    }

    @Test
    @DisplayName("createUser test")
    void createUser() {
        //when
        basicUserService.createUser(userDtoSample);
        Boolean expectedResult = userRepository.getUser(userDtoSample) != null;
        //then
        Assertions.assertThat(expectedResult).isTrue();



    }

    @Test
    @DisplayName("deleteUser Test")
    void deleteUser() {
        //given
        basicUserService.createUser(userDtoSample);
        basicChannelService.createChannel(channelDtoSample);
        basicMessageService.createMessage(messageDtoSample);
        //when
        basicUserService.deleteUser(userDtoSample);
        Boolean actualResult = userRepository.getUser(userDtoSample) == null;
        //then
        Assertions.assertThat(actualResult).isTrue();



    }

    @Test
    @DisplayName("deleteUser_with_channel Test")
    void deleteUser_with_channel_and_message() {
        //given
        basicUserService.createUser(userDtoSample);
        basicChannelService.createChannel(channelDtoSample);
        basicMessageService.createMessage(messageDtoSample);
        //when
        basicUserService.deleteUser(userDtoSample);
        MessageDto actualResult = messageRepository.getMessage(messageDtoSample);

        //then
        Assertions.assertThat(actualResult.getSender().getName()).isEqualTo("DeletedUser");



    }


    @Test
    @DisplayName("updateUser Test")
    void updateUser() {
        //given
        basicUserService.createUser(userDtoSample);
        //when
        basicUserService.updateUser(userDtoSample, User.userElement.NAME, "Updated");
        UserDto actualResult = userRepository.getUser(userDtoSample);
        //then
        Assertions.assertThat(actualResult.getName()).isEqualTo("Updated");
    }

    @Test
    @DisplayName("enterChannel Test")
    void enterChannel() {
        //given
        basicUserService.createUser(userDtoSample);
        basicChannelService.createChannel(channelDtoSample);
        //when
        basicUserService.enterChannel(userDtoSample,channelDtoSample);
        ChannelDto actualResult = channelRepository.getChannel(channelDtoSample);
        //then
        Assertions.assertThat(actualResult.getUserDtoList().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("exitChannel Test")
    void exitChannel() {
        //given
        basicUserService.createUser(userDtoSample);
        basicChannelService.createChannel(channelDtoSample);
        basicUserService.enterChannel(userDtoSample,channelDtoSample);
        //when
        basicUserService.exitChannel(userDtoSample,channelDtoSample);
        ChannelDto actualResult = channelRepository.getChannel(channelDtoSample);
        //then
        Assertions.assertThat(actualResult.getUserDtoList().size()).isEqualTo(0);
    }
}