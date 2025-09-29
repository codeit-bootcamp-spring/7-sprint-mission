package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.UserService;
import com.sprint.mission.discodeit.entity.service.jcf.*;

import java.awt.print.Printable;
import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        //싱글톤 길어서 변수에 담았다
        JCFUserService USER_SERVICE = JCFUserService.getInstance(); //유저기능
        JCFMessageService MESSAGE_SERVICE = JCFMessageService.getInstance(); //메시지 기능
        JCFChannelService CHANNEL_SERVICE = JCFChannelService.getInstance(); //채널 기능

      //유저
        System.out.println("==========================유저=============================");
       //구현
        USER_SERVICE.create("jam@ewew", "123123", "신제원", "바보");
        USER_SERVICE.create("kim@test.com", "abc123", "김철수", "철수짱");
        USER_SERVICE.create("lee@sample.com", "pass456", "이영희", "히히걸");
        USER_SERVICE.create("park@hello.com", "qwerty", "박민수", "민수킹");
        USER_SERVICE.create("choi@demo.com", "pw7890", "최지우", "지우스타");


    //채널
        System.out.println("===========================채널=============================");

        //메시지
        System.out.println("===========================메시지=============================");


    }
}
