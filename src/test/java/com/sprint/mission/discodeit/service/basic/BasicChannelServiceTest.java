package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ValidateService;
import com.sprint.mission.discodeit.service.util.ValidateOperator;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;
@DisplayName("BasicChannelService Test")
class BasicChannelServiceTest {
    private ChannelRepository channelRepository;
    private ValidateService validateService;
    private  UserRepository userRepository;
    private MessageRepository messageRepository;
    private ChannelDto channelDtoSample;
    private UserDto userDtoSample;
    private BasicChannelService basicChannelService;

    @BeforeEach
    void setUp() {
        channelRepository = new FileChannelRepository();
        messageRepository = new FileMessageRepository();
        userRepository = new FileUserRepository();
        validateService = new ValidateOperator(channelRepository,userRepository,messageRepository);
        channelDtoSample= new ChannelDto("JAVA","JAVA 안전자산 놀이터",true,true);
        userDtoSample = new UserDto("황준영","hwang","genius5375@gmail.com",true);
        basicChannelService = new BasicChannelService(channelRepository,validateService,userRepository);

    }

    @Test
    @DisplayName("createChannel Test")
    void createChannel_test() {





        //when
        basicChannelService.createChannel(channelDtoSample);
        Boolean actualResult = channelRepository.getChannel(channelDtoSample) != null;
        //then
        Assertions.assertThat(actualResult).isTrue();

    }



    @Test
    @DisplayName("deleteChannel Test")
    void deleteChannel() {
        //given
        basicChannelService.createChannel(channelDtoSample);
        //when

        basicChannelService.deleteChannel(channelDtoSample);
        Boolean actualResult = channelRepository.getChannel(channelDtoSample) == null;
        //then
        Assertions.assertThat(actualResult).isTrue();
    }

    @Test
    @DisplayName("updateChannel Test")
    void updateChannel() {
        //given
        basicChannelService.createChannel(channelDtoSample);
        //when
        basicChannelService.updateChannel(channelDtoSample,Channel.channelElement.NAME, "Updated");
        ChannelDto actualResult = channelRepository.getChannel(channelDtoSample);
        //then
        Assertions.assertThat(actualResult.getName()).isEqualTo("Updated");


    }


    @Test
    @DisplayName("inviteUserToChannel Test")
    void inviteUserToChannel() {
        // given
        basicChannelService.createChannel(channelDtoSample);
        userRepository.saveUser(userDtoSample);

        //when
        basicChannelService.inviteUserToChannel(userDtoSample,channelDtoSample);
        ChannelDto actualResult = channelRepository.getChannel(channelDtoSample);
        //then
        Assertions.assertThat(actualResult.getUserDtoList().size()).isEqualTo(1);

    }

    @Test
    @DisplayName("deleteUserFromChannel Test")
    void deleteUserFromChannel() {
        //given
        basicChannelService.createChannel(channelDtoSample);
        userRepository.saveUser(userDtoSample);
        basicChannelService.inviteUserToChannel(userDtoSample,channelDtoSample);
        //when
        basicChannelService.deleteUserFromChannel(userDtoSample,channelDtoSample);
        ChannelDto actualResult = channelRepository.getChannel(channelDtoSample);
        //then
        Assertions.assertThat(actualResult.getUserDtoList().size()).isEqualTo(0);

    }
}