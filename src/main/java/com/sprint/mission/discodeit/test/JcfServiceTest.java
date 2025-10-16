package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.file.FileMessageRepository;
import com.sprint.mission.discodeit.service.file.FileUserRepository;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;
import com.sprint.mission.discodeit.service.util.ValidateOperator;

public class JcfServiceTest {
    public static void main(String[] args) {

       ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();
        UserRepository userRepository = new FileUserRepository();
        ValidateService validateService = new ValidateOperator(channelRepository, userRepository, messageRepository);

        ChannelService channelService  = new JCFChannel(channelRepository,validateService,userRepository);
        MessageService messageService = new JCFMessage(messageRepository,validateService);
       UserService userService = new JCFUser(userRepository,validateService,channelRepository,messageRepository);


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
        messageService.readMessage(m2Dto);

    }
}
