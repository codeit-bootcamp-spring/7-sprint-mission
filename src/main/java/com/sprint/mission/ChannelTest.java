package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.*;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.UUID;

public class ChannelTest {

    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService(userService);
        userService.setChannelService(channelService);

        // --- 유저 및 채널 생성 ---
        UserInfo User1 = userService.createUser("test@codeit.com", "QWERty1!", "admin");
        UserInfo User2 = userService.createUser("newbie@codeit.com", "NewbiePass1!", "newbie");
        UUID user1Id = User1.getId();
        UUID user2Id = User2.getId();

        ChannelInfo channel1 = channelService.createChannel(user1Id, Channel.ChannelType.TEXT);
        UUID channelId = channel1.getId();

        System.out.println("--- 채널 조회 ---");
        channelService.findChannelInfoById(channelId).ifPresent(System.out::println);

        // --- 채널 수정 ---
        System.out.println("--- 채널 이름 수정 ---");
        try {
            channelService.updateChannelName(channelId, "UpdatedChannel");
        } catch (InvalidInputException e) {
            System.out.println("오류: "  + e.getMessage());
        }
        channelService.findChannelInfoById(channelId).ifPresent(System.out::println);

        // --- 채널2 생성 ---
        ChannelInfo channel2 = channelService.createChannel(user2Id, "채널2", Channel.ChannelType.VOICE);
        UUID channel2Id = channel2.getId();

        // --- 채널 전체 조회 ---
        System.out.println("--- 채널 전체 조회 ---");
        channelService.findAll().forEach(System.out::println);

        // --- 멤버 추가 ---
        System.out.println("--- 멤버 각각 추가 ---");
        channelService.addMemberToChannel(channelId, user2Id);
        channelService.addMemberToChannel(channel2Id, user1Id);
        channelService.findAll().forEach(System.out::println);

        // --- 중복 추가 ---
        System.out.println("--- 중복 추가 ---");
        channelService.addMemberToChannel(channelId, user2Id);
        channelService.addMemberToChannel(channel2Id, user1Id);

        // --- 멤버 삭제 ---
        System.out.println("--- 채널1 멤버 삭제 ---");
        channelService.removeMemberFromChannel(channelId, user2Id);
        channelService.findChannelInfoById(channelId).ifPresent(System.out::println);

        // --- 없는 멤버 삭제 ---
        System.out.println("--- 중복 삭제 ---");
        channelService.removeMemberFromChannel(channelId, user2Id);

        // --- 채널 삭제 ---
        System.out.println("--- 채널 삭제 후 전체 조회 ---");
        channelService.deleteChannel(channelId);
        channelService.findAll().forEach(System.out::println);

        // --- 유저1 삭제 후 채널 조회 ---
        System.out.println("--- 유저1 삭제 후 채널 조회 ---");
        userService.deleteUser(user1Id);
        channelService.findAll().forEach(System.out::println);

        System.out.println("========================");
        System.out.println("종료");
    }
}
