package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfo;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfoDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.List;
import java.util.UUID;

public class MessageTest {

    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService(userService);
        JCFMessageService messageService = new JCFMessageService(userService, channelService);

        // --- 유저 및 채널 생성 ---
        UserInfoDto user1 = userService.createUser("test@codeit.com", "QWERty1!", "admin");
        UserInfoDto user2 = userService.createUser("newbie@codeit.com", "NewbiePass1!", "newbie");
        UserInfoDto user3 = userService.createUser("asd@codeit.com", "aaaaPass1!", "Arang");
        UUID user1Id = user1.getId();
        UUID user2Id = user2.getId();
        UUID user3Id = user3.getId();

        ChannelInfo channel = channelService.createChannel(user1Id, ChannelType.TEXT);
        UUID channelId = channel.getId();

        // --- 채널에 유저 추가 ---
        channelService.addMemberToChannel(channelId, user2Id);
        channelService.addMemberToChannel(channelId, user3Id);

        // --- 개인 메시지 테스트 ---
        System.out.println("\n--- 개인 메시지 전송 및 조회 ---");
        messageService.createDirectMessage(user1Id, user2Id, "안녕하세요!");
        messageService.createDirectMessage(user2Id, user1Id, "네 좋은 아침이예요");

        List<MessageInfoDto> dm = messageService.findMessageBetweenUsers(user1Id, user2Id);
        System.out.println("==========================");
        System.out.printf("%s <-> %s\n", user1.userName(), user2.userName());
        dm.forEach(System.out::println);
        System.out.println("==========================");

        // --- 채널 메시지 테스트 ---
        System.out.println("\n--- 채널 메시지 전송 및 조회 ---");
        messageService.createChannelMessage(user1Id, channelId, "여러분 안녕하세요");
        messageService.createChannelMessage(user2Id, channelId, "안녕하세요");
        MessageInfoDto cm = messageService.createChannelMessage(user3Id, channelId, "반가워요");

        System.out.println("===========================");
        System.out.println(channel.getChannelName());
        messageService.findChannelMessage(channelId).forEach(System.out::println);
        System.out.println("===========================");

        // --- 메시지 수정 ---
        System.out.println("\n--- 메시지 수정 ---");
        UUID cmId = cm.id();
        messageService.updateMessage(cmId, "반갑습니다!!!!");
        System.out.println("=========수정 후 출력=========");
        System.out.println(channel.getChannelName());
        messageService.findChannelMessage(channelId).forEach(System.out::println);
        System.out.println("===========================");

        // --- 메시지 삭제 ---
        System.out.println("\n--- 메시지 삭제 ---");
        boolean isDeleted = messageService.deleteMessage(cmId);
        System.out.println(" 메시지 삭제 " + (isDeleted ? "성공" : "실패"));

        System.out.println("===========================");
        System.out.println(channel.getChannelName());
        messageService.findChannelMessage(channelId).forEach(System.out::println);
        System.out.println("===========================");

        System.out.println("\n종료");
    }
}
