package com.sprint.mission.discodeit.service;

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
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;
import com.sprint.mission.discodeit.service.basic.BasicMessageService;
import com.sprint.mission.discodeit.service.basic.BasicUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.util.ValidateOperator;

import java.io.File;

public class JavaApplication {
    public static void main(String[] args) {

        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();
        UserRepository userRepository = new FileUserRepository();
        ValidateService validateService = new ValidateOperator(channelRepository, userRepository, messageRepository);

        ChannelService channelService  = new BasicChannelService(channelRepository,validateService,userRepository);
        MessageService messageService = new BasicMessageService(messageRepository,validateService);
        UserService userService = new BasicUserService(userRepository,validateService,channelRepository,messageRepository);


        ChannelDto channel1Dto = new ChannelDto("JAVA","JAVA 안전자산 놀이터",true,true);
        ChannelDto channel2Dto = new ChannelDto("Git","Git fork 선봉자의 모임",false,false);
        ChannelDto channel3Dto = new ChannelDto("던파", "던악귀의 모임", true, false);

        UserDto user1Dto = new UserDto("황준영","hwang","genius5375@gmail.com",true);
        UserDto user2Dto = new UserDto("대상혁","Faker" ,"faker@riot.org" , false);
        UserDto notInUserDBUserDto = new UserDto("신창섭", "정상화","maple.org" ,true);

        MessageDto m1Dto = new MessageDto("Hello", user1Dto, false);
        MessageDto m2Dto = new MessageDto("Hi I am Faker", user2Dto, false);
        MessageDto m3Dto = new MessageDto("JAVA 를 정상화하네", notInUserDBUserDto, false);

        channelService.createChannel(channel1Dto);
        channelService.createChannel(channel2Dto);
        channelService.createChannel(channel3Dto);

        userService.createUser(user1Dto);
        userService.createUser(user2Dto);
        userService.createUser(notInUserDBUserDto);

        messageService.createMessage(m1Dto);
        messageService.createMessage(m2Dto);
        messageService.createMessage(m3Dto);



        channelService.updateChannel(channel1Dto, Channel.channelElement.NAME, "JavaUpdate");
        channelService.readUpdatedChannel();

        channelService.inviteUserToChannel(user2Dto,channel2Dto);
        channelService.readChannel(channel2Dto);
        userService.deleteUser(user2Dto);
        userService.readDeletedUser();
        messageService.readMessage(m2Dto);
        messageService.readAllMessage();


    }
}
