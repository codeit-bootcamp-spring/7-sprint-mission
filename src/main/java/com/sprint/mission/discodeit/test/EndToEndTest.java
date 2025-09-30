package com.sprint.mission.discodeit.test;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;
import com.sprint.mission.discodeit.service.jcf.JCFUser;

public class EndToEndTest {
    public static void main(String[] args) {

        //무조건 해야함 DB 생성, 의존성 주입
        JCFDb jcfDb = new JCFDb();
        JCFChannel jcfChannel= new JCFChannel(jcfDb);
        JCFUser jcfUser = new JCFUser(jcfDb);
        JCFMessage jcfMessage = new JCFMessage(jcfDb);

        //채널 객체

        Channel channel1 = new Channel("JAVA","JAVA 안전자산 놀이터",true,true);
        Channel channel2 = new Channel("Git","Git fork 선봉자의 모임",false,false);
        Channel channel3 = new Channel("던파", "던악귀의 모임", true, false);

        //유저 객체

        User user1 = new User("황준영","hwang","genius5375@gmail.com",true);
        User user2 = new User("대상혁","Faker" ,"faker@riot.org" , false);
        User notInUserDBUser = new User("신창섭", "정상화","maple.org" ,true);

        // 메세지 객체

        Message m1 = new Message("Hello", user1, false);
        Message m2 = new Message("Hi I am Faker", user2, false);
        Message m3 = new Message("JAVA 를 정상화하네", notInUserDBUser, false);

        // 채널 생성

        jcfChannel.createChannel(channel1);
        jcfChannel.createChannel(channel2);
        jcfChannel.createChannel(channel3);

        //유저 생성 user 1, user2 만

        jcfUser.createUser(user1);
        jcfUser.createUser(user2);


        //메세지 생성

        jcfMessage.createMessage(m1);
        jcfMessage.createMessage(m2);
        jcfMessage.createMessage(m3);

        //메세지 삭제
        jcfMessage.deleteMessage(m1);
        jcfMessage.deleteMessage(m3);

        //유저 채널 입장 및 유저 강퇴 , 유저 나오기
        jcfMessage.readDeletedMessage();
        jcfUser.enterChannel(user1,channel1);
        jcfChannel.deleteUserFromChannel(user2,channel1);
        jcfChannel.deleteUserFromChannel(user1,channel1);
        jcfChannel.inviteUserToChannel(user1,channel2);
        jcfChannel.inviteUserToChannel(user1 ,channel1);


        // 유저 정보

        jcfUser.readUser(user1);
        // 채널 삭제

        jcfChannel.deleteChannel(channel1);
        jcfUser.readUser(user1);
        jcfChannel.readChannel(channel1);
        jcfChannel.readDeletedChannel();
        jcfUser.readDeletedUser();
        //채널 업데이트
        jcfChannel.updateChannel(channel2, Channel.channelElement.NAME,"Rust" );
        jcfChannel.updateChannel(channel3, Channel.channelElement.IS_PUBLIC,false);
        jcfChannel.updateChannel(channel3, Channel.channelElement.IS_PUBLIC,"hello");

        jcfChannel.readAllChannel();

        //메세지 업데이트
        jcfMessage.updateMessage(m2, Message.messageElement.CONTENT,"This is edited message ");

        jcfMessage.createMessage(m1);
//        jcfChannel.readChannel(channel2);
//        jcfChannel.readChannel(channel3);
//        jcfChannel.readUpdatedChannel();





    }
}
