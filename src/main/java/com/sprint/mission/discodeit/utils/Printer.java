package com.sprint.mission.discodeit.utils;

import com.sprint.mission.discodeit.dto.channel.response.ChannelResponseDto;
import com.sprint.mission.discodeit.dto.channel.response.PrivateChannelResponseDto;
import com.sprint.mission.discodeit.dto.user.response.UserResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.ReceiveType;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.file.FileUserRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;

import java.time.Instant;
import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class Printer {

    public static void printInfo(UserResponseDto user){
        System.out.printf("%s님의 정보\n", user.getNickName());
        System.out.printf("이름: %s\n", user.getUserName());
        System.out.printf("닉네임: %s\n", user.getNickName());
        System.out.printf("이메일: %s\n", user.getEmail());
        System.out.printf("전화번호: %s\n", user.getPhoneNum());
        System.out.printf("아이디: %s\n", user.getUserId());
    }

    public static void printChatHistory(UserService userService, UserResponseDto user, List<Message> msgs) {
        Instant unixTime;
        String KST, date = null, time;

        for(Message msg : msgs) {
            KST = TimeConvert.time(msg.getCreatedAt()); // Instant 값을 "yyyy-MM-dd HH:mm:ss" 형태로 변환
            time = KST.split(" ")[1]; // 시간만 저장

            if (!KST.split(" ")[0].equals(date)) { //날짜가 다르다면
                date = KST.split(" ")[0]; // 날짜만 저장
                System.out.printf("====================%s====================\n", date);
            }

            if (msg.getSenderId().equals(user.getId())) {
                System.out.printf("%s 나: %s\n", time, msg.getContent());
            } else if(!msg.getReceiverId().equals(user.getId()) && msg.getReceiverId().equals(msg.getSenderId())) { // 채널이 보낸 메시지의 경우
                System.out.printf("%s\n", msg.getContent());
            } else {
                System.out.printf("%s %s: %s\n", time, userService.getUserNickName(msg.getSenderId()), msg.getContent());
            }
        }
    }

    public static void printChatLatest(MessageService messageService, UserResponseDto user1, UserResponseDto user2){
        Optional.ofNullable(messageService.getLastestMessage(user1.getId(), user2.getId(), ReceiveType.USER))
                .ifPresentOrElse(
                        message -> {
                            String KST = TimeConvert.time(message.getCreatedAt());
                            String time = KST.split(" ")[1];
                            System.out.printf("%s: %s(%s)\n", user2.getNickName(), message.getContent(), time);
                        },
                        () -> System.out.printf("%s\n", user2.getNickName()) //이전 메시지가 없다면 닉네임만 출력
                );
    }

    public static void printChannelInfo(ChannelResponseDto channel, int num){
        System.out.printf("%d. %s(%s, %s)\n", num + 1, channel.getChannelName(), channel.getChannelType(), channel.getVisibility());
        System.out.printf("   └ 최근 대화: %s\n", TimeConvert.time(channel.getLastedMessageAt()));
    }

    public static void printChannelMember(ChannelService channelService, UUID channelId){
        PrivateChannelResponseDto channel = (PrivateChannelResponseDto) channelService.getChannel(channelId);
        UserRepository userRepository = FileUserRepository.getInstance();

        System.out.println("채널 멤버 조회");
        List<User> channelMember = channel.getMemberIds().stream()
                .map(id -> userRepository.findById(id))
                .toList();

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
