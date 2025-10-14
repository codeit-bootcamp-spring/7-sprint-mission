package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;

public class Printer {
    public static void printInfo(User user){
        System.out.printf("%s님의 정보\n", user.getNickName());
        System.out.printf("이름: %s\n", user.getUserName());
        System.out.printf("닉네임: %s\n", user.getNickName());
        System.out.printf("이메일: %s\n", user.getEmail());
        System.out.printf("전화번호: %s\n", user.getPhoneNum());
        System.out.printf("아이디: %s\n", user.getUserId());
    }

    public static void printChatHistory(UserService userService, User user, List<Message> msgs) {
        long unixTime;
        String KST, date = null, time;

        for(Message msg : msgs){
            unixTime = msg.getCreatedAt();
            KST = TimeConvert.time(unixTime);
            time = KST.split(" ")[1]; // 시간만 저장

            if(!KST.split(" ")[0].equals(date)){ //날짜가 다르다면
                KST = TimeConvert.time(unixTime);
                date = KST.split(" ")[0]; // 날짜만 저장
                System.out.printf("====================%s====================\n", date);
            }

            if(msg.senderId == user.getId()){
                System.out.printf("%s 나: %s\n", time, msg.getContents());
            } else {
                System.out.printf("%s %s: %s\n", time, userService.getUserNickName(msg.getSenderId()), msg.getContents());
            }
        }
    }

    public static void printChatLatest(MessageService<User> messageService, User user1, User user2){
        Message message = messageService.getLastestMessage(user1, user2);
        long unixTime;
        String KST, time;

        if(message == null){ //이전 메시지가 없다면 닉네임만 출력
            System.out.printf("%s\n", user2.getNickName());
        } else {
            unixTime = message.getCreatedAt();
            KST = TimeConvert.time(unixTime);
            time = KST.split(" ")[1];
            System.out.printf("%s: %s(%s)\n", user2.getNickName(), message.getContents(), time);
        }
    }

    public static void printLine(){
        System.out.println("================================================================");
    }

    public static void printHalfLine(){
        System.out.println("================================");
    }
}
