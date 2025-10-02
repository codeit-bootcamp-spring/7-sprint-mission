package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.UUID;

public class ChannelTest {

    public static void main(String[] args) {

        JCFUserService userService = new JCFUserService();
        JCFChannelService channelService = new JCFChannelService(userService); // 의존성주입

        System.out.println("🚀 채널 멤버 관리 기능 테스트 시작");

        // --- 유저 및 채널 생성 ---
        User User1 = userService.createUser("test@codeit.com", "QWERty1!", "admin");
        User User2 = userService.createUser("newbie@codeit.com", "NewbiePass1!", "newbie");
        Channel channel1 = channelService.create(User1, Channel.ChannelType.TEXT);

        UUID user1Id = User1.getId();
        UUID newUserId = User2.getId();
        UUID channelId = channel1.getId();

        System.out.println("--- 채널 조회 ---");
        channelService.findById(channelId).ifPresent(System.out::println);

        // --- 채널 수정 ---
        System.out.println("--- 채널 이름 수정 ---");
        channelService.updateChannelName(channelId, "UpdatedChannel");
        channelService.findById(channelId).ifPresent(System.out::println);

        // --- 채널2 생성 ---
        Channel channel2 = channelService.create(User2, "뉴비의채널", Channel.ChannelType.VOICE);
        UUID channel2Id = channel2.getId();

        // --- 채널 전체 조회 ---
        System.out.println("--- 채널 전체 조회 ---");
        channelService.findAll().forEach(System.out::println);

        // --- 멤버 추가 ---
        System.out.println("--- 멤버 추가 ---");
        channelService.addMember(channelId, newUserId);
        channelService.findById(channelId).ifPresent(System.out::println);

        // --- 중복 추가 ---
        System.out.println("--- 중복 추가 ---");
        channelService.addMember(channelId, newUserId);

        // --- 멤버 삭제 ---
        System.out.println("---멤버 삭제 ---");
        channelService.removeMember(channelId, newUserId);
        channelService.findById(channelId).ifPresent(System.out::println);

        // --- 없는 멤버 삭제 ---
        System.out.println("---없는 멤버 삭제 ---");
        channelService.removeMember(channelId, newUserId);

        // --- 채널 삭제 ---
        System.out.println("--- 채널 삭제 후 전체 조회 ---");
        channelService.deleteChannel(channelId);
        channelService.findAll().forEach(System.out::println);

        System.out.println("종료");
    }
}
