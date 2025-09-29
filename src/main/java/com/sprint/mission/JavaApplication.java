package com.sprint.mission;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFUserService;

public class JavaApplication {

    public static void main(String[] args) {

        UserService userService = new JCFUserService();

        System.out.println("User CRUD");

        // 유저1, 2 생성
        User user1 = new User("admin@codeit.com", "00000000", "admin");
        User user2 = new User("asd@codeit.com", "qwer1234", "codeitSB7");
        userService.create(user1);
        System.out.println("유저1 생성");
        userService.create(user2);
        System.out.println("유저2 생성");

        // 읽기
        System.out.println("조회: " + userService.read(user1.getId()));

        // 수정
        user1.updateUserName("AdminUpdated");
        userService.update(user1.getId(), user1);       // 뭘 해야하나

        // 수정 후 읽기
        System.out.println("유저 이름 업데이트: " + userService.read(user1.getId()));

        // 유저3 생성
        User user3 = new User("asd@codeit.com", "12345678", "gnara0719");
        userService.create(user3);
        System.out.println("유저3 생성");

        // 전체 읽기
        System.out.println("전체 유저: " + userService.readAll());

        // 유저 삭제 후 전체 읽기
        userService.delete(user2.getId());
        System.out.println("유저2 삭제");
        System.out.println("전체 유저: " + userService.readAll());

        System.out.println("======================================");
        System.out.println("Channel CRUD");

        ChannelService channelService = new JCFChannelService();
        Channel ch1 = new Channel(user1, Channel.ChannelType.TEXT);
        channelService.create(ch1);
        Channel ch2 = new Channel(user3, "나의 채널", Channel.ChannelType.VOICE);
        channelService.create(ch2);
        System.out.println("채널1, 2 생성");
        System.out.println("채널 전체 조회"); // 생성시간이 똑같아서 순서가 섞임
        System.out.println(channelService.readAll());

        // 채널2 수정
        ch2.updateMembers(user1);
        ch1.updateMembers(user2);       // 삭제된 유저가 들어가짐 ㄷㄷ
        user3.updateUserName("UpdatedGnara0719");
        ch1.updateChannelName("수정된채널");
        channelService.update(ch2.getId(), ch2);

        System.out.println("채널 전체 조회");
        System.out.println(channelService.readAll());

        System.out.println("채널2 삭제");
        channelService.delete(ch2.getId());

        System.out.println("채널 전체 조회");
        System.out.println(channelService.readAll());
    }
}