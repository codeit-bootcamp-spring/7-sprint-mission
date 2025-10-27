package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.util.List;
import java.util.Optional;

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

            if(msg.getSenderId().equals(user.getId())){
                System.out.printf("%s 나: %s\n", time, msg.getContent());
            } else {
                System.out.printf("%s %s: %s\n", time, userService.getUserNickName(msg.getSenderId()), msg.getContent());
            }
        }
    }

    public static void printChatLatest(MessageService messageService, User user1, User user2){
        Optional.ofNullable(messageService.getLastestMessage(user1, user2))
                .ifPresentOrElse(
                        message -> {
                            long unixTime = message.getCreatedAt();
                            String KST = TimeConvert.time(unixTime);
                            String time = KST.split(" ")[1];
                            System.out.printf("%s: %s(%s)\n", user2.getNickName(), message.getContent(), time);
                        },
                        () -> System.out.printf("%s\n", user2.getNickName()) //이전 메시지가 없다면 닉네임만 출력
                );
    }

    public static void printChannelMember(Channel channel){
        System.out.println("채널 멤버 조회");
        List<User> channelMember = channel.getMembers();
        for (int i = 0; i < channelMember.size(); i++) {
            System.out.printf("%d. %s\n", i + 1, channelMember.get(i).getNickName());
        }
    }

    public static void printLine(){
        System.out.println("================================================================");
    }

    public static void printHalfLine(){
        System.out.println("================================");
    }
}
