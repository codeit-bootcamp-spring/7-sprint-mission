package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.config.AppConfig;
import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ValidateService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.util.ValidateOperator;

public class JavaApplication {

    public static void main(String[] args) {

        AppConfig appConfig = new AppConfig();

        UserService fileBasicUserService = appConfig.getFileBasicUserService();
        UserService jcfBasicUserService = appConfig.getJcfBasicUserService();

        ChannelService fileBasicChannelService = appConfig.getFileBasicChannelService();
        ChannelService jcfBasicChannelService = appConfig.getJcfBasicChannelService();

        MessageService fileBasicMessageService = appConfig.getFileBasicMessageService();
        MessageService jcfBasicMessageService = appConfig.getJcfBasicMessageService();

        ChannelDto channel1Dto = appConfig.getChannel1Dto();
        ChannelDto channel2Dto = appConfig.getChannel2Dto();
        ChannelDto channel3Dto = appConfig.getChannel3Dto();

        UserDto user1Dto = appConfig.getUser1Dto();
        UserDto user2Dto = appConfig.getUser2Dto();
        UserDto notInUserDBUserDto = appConfig.getNotInUserDBUserDto();

        MessageDto m1Dto = appConfig.getM1Dto();
        MessageDto m2Dto = appConfig.getM2Dto();
        MessageDto m3Dto = appConfig.getM3Dto();




        fileBasicChannelService.createChannel(channel1Dto);
        fileBasicChannelService.createChannel(channel2Dto);
        fileBasicChannelService.createChannel(channel3Dto);

        fileBasicUserService.createUser(user1Dto);
        fileBasicUserService.createUser(user2Dto);
        fileBasicUserService.createUser(notInUserDBUserDto);

        fileBasicMessageService.createMessage(m1Dto);
        fileBasicMessageService.createMessage(m2Dto);
        fileBasicMessageService.createMessage(m3Dto);



        fileBasicChannelService.updateChannel(channel1Dto, Channel.channelElement.NAME, "JavaUpdate");
        fileBasicChannelService.readUpdatedChannel();

        fileBasicChannelService.inviteUserToChannel(user2Dto,channel2Dto);
        fileBasicChannelService.deleteUserFromChannel(user2Dto,channel2Dto);
        fileBasicChannelService.readChannel(channel2Dto);
        fileBasicUserService.deleteUser(user2Dto);
        fileBasicUserService.readDeletedUser();
        fileBasicMessageService.readMessage(m2Dto);
        fileBasicMessageService.readAllMessage();


    }
}
