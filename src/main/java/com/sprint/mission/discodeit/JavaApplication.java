package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.file.*;

import java.util.List;
import java.util.UUID;

public class JavaApplication {
    public static void main(String[] args) {
        // 파일 경로는 프로젝트 루트 기준으로 data 디렉토리에 저장됩니다.
        String userFile = "data/users.db";
        String channelFile = "data/channels.db";
        String messageFile = "data/messages.db";

        UserService userService = new FileUserService(userFile);
        ChannelService channelService = new FileChannelService(channelFile);
        MessageService messageService = new FileMessageService(messageFile);

        // --- 등록 ---
        User u1 = userService.create("Alice", "alice@example.com");
        User u2 = userService.create("Bob", "bob@example.com");

        Channel c1 = channelService.create("general");
        Channel c2 = channelService.create("random");

        Message m1 = messageService.create(u1.getId(), c1.getId(), "Hello everyone!");
        Message m2 = messageService.create(u2.getId(), c1.getId(), "Hi Alice!");
        Message m3 = messageService.create(u1.getId(), c2.getId(), "Random post");

        // --- 단건 조회 ---
        System.out.println("=== 단건 조회 ===");
        System.out.println(userService.findById(u1.getId()));
        System.out.println(channelService.findById(c1.getId()));
        System.out.println(messageService.findById(m1.getId()));

        // --- 전체 조회 ---
        System.out.println("\n=== 전체 조회 ===");
        List<User> users = userService.findAll();
        users.forEach(System.out::println);
        List<Channel> channels = channelService.findAll();
        channels.forEach(System.out::println);
        List<Message> messages = messageService.findAll();
        messages.forEach(System.out::println);

        // --- 수정 ---
        System.out.println("\n=== 수정 ===");
        userService.update(u1.getId(), "AliceUpdated", "alice_new@example.com");
        channelService.update(c1.getId(), "general-updated");
        messageService.update(m1.getId(), "Hello world, updated!");

        System.out.println("\n=== 수정된 데이터 조회 ===");
        System.out.println(userService.findById(u1.getId()));
        System.out.println(channelService.findById(c1.getId()));
        System.out.println(messageService.findById(m1.getId()));

        // --- 삭제 ---
        System.out.println("\n=== 삭제 ===");
        messageService.delete(m2.getId());
        userService.delete(u2.getId());
        channelService.delete(c2.getId());

        // --- 삭제 확인 ---
        System.out.println("\n=== 삭제 확인 ===");
        System.out.println("Deleted message m2: " + messageService.findById(m2.getId())); // null expected
        System.out.println("Deleted user u2: " + userService.findById(u2.getId())); // null expected
        System.out.println("Deleted channel c2: " + channelService.findById(c2.getId())); // null expected

        // --- 최종 상태 출력 ---
        System.out.println("\n=== 최종 전체 상태 ===");
        userService.findAll().forEach(System.out::println);
        channelService.findAll().forEach(System.out::println);
        messageService.findAll().forEach(System.out::println);
    }
}