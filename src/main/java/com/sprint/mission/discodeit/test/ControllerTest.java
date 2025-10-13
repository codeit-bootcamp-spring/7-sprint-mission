package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.dto.ChannelDto;
import com.sprint.mission.discodeit.dto.MessageDto;
import com.sprint.mission.discodeit.dto.UserDto;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.presentation.ChannelController;
import com.sprint.mission.discodeit.presentation.MessageController;
import com.sprint.mission.discodeit.presentation.UserController;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

import java.util.List;

public class ControllerTest {
    public static void main(String[] args) {
        JCFDb jcfDb = new JCFDb();
        ChannelController channelController = new ChannelController(new JCFChannel(jcfDb));
        MessageController messageController = new MessageController(new JCFMessage(jcfDb));
        UserController userController = new UserController(new JCFUser(jcfDb));
//        JCFChannel jcfChannel= new JCFChannel(jcfDb);
//        JCFUser jcfUser = new JCFUser(jcfDb);
//        JCFMessage jcfMessage = new JCFMessage(jcfDb);

        //채널 객체

        ChannelDto channel1Dto = new ChannelDto("JAVA","JAVA 안전자산 놀이터",true,true);
        ChannelDto channel2Dto = new ChannelDto("Git","Git fork 선봉자의 모임",false,false);
        ChannelDto channel3Dto = new ChannelDto("던파", "던악귀의 모임", true, false);

        channelController.createChannelController(channel1Dto);
        channelController.createChannelController(channel2Dto);
        channelController.createChannelController(channel3Dto);
        //유저 객체

        UserDto user1Dto = new UserDto("황준영","hwang","genius5375@gmail.com",true);
        UserDto user2Dto = new UserDto("대상혁","Faker" ,"faker@riot.org" , false);
        UserDto notInUserDBUserDto = new UserDto("신창섭", "정상화","maple.org" ,true);

        User user1 = userController.createUserController(user1Dto);
        User user2 = userController.createUserController(user2Dto);
        User notInUserDBUser = userController.createUserController(notInUserDBUserDto);
        // 메세지 객체

        MessageDto m1Dto = new MessageDto("Hello", user1, false);
        MessageDto m2Dto = new MessageDto("Hi I am Faker", user2, false);
        MessageDto m3Dto = new MessageDto("JAVA 를 정상화하네", notInUserDBUser, false);

        Message m1 = messageController.createMessageController(m1Dto);
        Message m2 = messageController.createMessageController(m2Dto);
        Message m3 = messageController.createMessageController(m3Dto);

        channelController.inviteUserToChannelController(user1Dto,channel1Dto);

    }
}
