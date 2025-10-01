package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.MessageService;
import com.sprint.mission.discodeit.entity.service.UserService;
import com.sprint.mission.discodeit.entity.service.jcf.*;

import java.awt.print.Printable;
import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Collections;
import java.util.LinkedList;
import java.util.List;

public class JavaApplication {

    public static void main(String[] args) {
        //싱글톤 길어서 변수에 담았다
        JCFUserService USER_SERVICE = JCFUserService.getInstance(); //유저기능
        JCFMessageService MESSAGE_SERVICE = JCFMessageService.getInstance(); //메시지 기능
        JCFChannelService CHANNEL_SERVICE = JCFChannelService.getInstance(); //채널 기능
        //등록 조회 수정 조회 삭제
      //유저
        System.out.println("==========================유저=============================");
       //등록
     User u1 = USER_SERVICE.create("jam@ewew", "123123", "신제원", "바보");
     User u2 =   USER_SERVICE.create("kim@test.com", "abc123", "김철수", "철수짱");
     User u3 =  USER_SERVICE.create("lee@sample.com", "pass456", "이영희", "히히걸");
     User u4 =   USER_SERVICE.create("park@hello.com", "qwerty", "박민수", "민수킹");
     User u5 =  USER_SERVICE.create("choi@demo.com", "pw7890", "최지우", "지우스타");

      //조회
        USER_SERVICE.read(u1.getId());
        USER_SERVICE.readAll().forEach(System.out::println);
       //수정
       USER_SERVICE.update(u1.getId(), u-> {
            u.setUserId("dd");
            u.setUserNickname("d");
       });
       //조회
        System.out.println(USER_SERVICE.read(u1.getId()));
       //삭제
       USER_SERVICE.delete(u1.getId());
       //조회
       USER_SERVICE.read(u1.getId());
        USER_SERVICE.readAll().forEach(System.out::println);


        System.out.println("===========================메시지=============================");
        //등록
        Message m1 =  MESSAGE_SERVICE.create(u2,u3,"바보에게 바보가1");
        Message m2 =  MESSAGE_SERVICE.create(u2,u3,"바보에게 바보가2");
        Message m3 =  MESSAGE_SERVICE.create(u2,u3,"바보에게 바보가3");
        // 조회
        MESSAGE_SERVICE.read(m1.getId());
        MESSAGE_SERVICE.readAll();//.forEach(System.out::println);
        // 수정
        MESSAGE_SERVICE.update(m1.getId(),"미안해");
        // 조회
        MESSAGE_SERVICE.read(m1.getId());
        // 삭제
        MESSAGE_SERVICE.delete(m1.getId());
        // 조회
        MESSAGE_SERVICE.read(m1.getId());
        MESSAGE_SERVICE.readAll();//.forEach(System.out::println);


        System.out.println("===========================채널=============================");

        //등록
        Channel channel1 = CHANNEL_SERVICE.create(u2, "채널1");
        Channel channel2 = CHANNEL_SERVICE.create(u3, "채널2");
        Channel channel3 = CHANNEL_SERVICE.create(u4, "채널3");
        Channel channel4 = CHANNEL_SERVICE.create(u5, "채널4");

        // 조회
         CHANNEL_SERVICE.read(channel1.getId());
         CHANNEL_SERVICE.readAll();
        // 수정 유저 추가 추가유저 보스설정
        CHANNEL_SERVICE.update(channel1.getId(),d->{

            channel1.getUsers().add(0,u3);
            d.setBose(u3);
        });
        // 조회
        CHANNEL_SERVICE.read(channel2.getId());
        CHANNEL_SERVICE.readAll();
        // 삭제
        CHANNEL_SERVICE.delete(channel2.getId());
        //조회
        CHANNEL_SERVICE.read(channel2.getId());
        CHANNEL_SERVICE.readAll();


      String str ="테스트";
      StringBuffer s = new StringBuffer("테스트");

      Integer ad =1;
      int ac =1;



    }
}
