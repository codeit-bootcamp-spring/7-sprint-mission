package com2.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com2.sprint.mission.discodeit.entity.Channel;
import com2.sprint.mission.discodeit.entity.Message;
import com2.sprint.mission.discodeit.entity.User;
import com2.sprint.mission.discodeit.service.ChannelService;
import com2.sprint.mission.discodeit.service.MessageService;
import com2.sprint.mission.discodeit.service.UserService;
import com2.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com2.sprint.mission.discodeit.service.jcf.JCFMessageService;
import com2.sprint.mission.discodeit.service.jcf.JCFUserService;





/**
 * 메인 애플리케이션 클래스
 * 각 도메인(User, Channel, Message)에 대해 CRUD 및 검증 기능을 테스트한다.
 */
public class JavaApplication {
    public static void main(String[] args) {
        // ===== 서비스 초기화 =====
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(userService, channelService);

        // ===== User CRUD =====
        System.out.println("\n===== User CRUD =====");
        User user = new User("woou", "woou@example.com");
        userService.create(user); // 등록
        System.out.println("등록: " + user.getUsername());

        user.update("woou-updated", "updated@example.com");
        userService.update(user.getId(), user); // 수정
        System.out.println("수정: " + userService.read(user.getId()).getUsername());

        userService.delete(user.getId()); // 삭제
        System.out.println("삭제 확인: " + userService.read(user.getId()));

        // ===== Channel CRUD =====
        System.out.println("\n===== Channel CRUD =====");
        Channel channel = new Channel("general", "General chat");
        channelService.create(channel); // 등록
        System.out.println("등록: " + channel.getName());

        channel.update("general-updated", "Updated desc");
        channelService.update(channel.getId(), channel); // 수정
        System.out.println("수정: " + channelService.read(channel.getId()).getName());

        channelService.delete(channel.getId()); // 삭제
        System.out.println("삭제 확인: " + channelService.read(channel.getId()));

        // ===== Message CRUD (정상 케이스) =====
        System.out.println("\n===== Message CRUD (정상) =====");
        User msgUser = new User("msgUser", "msg@example.com");
        Channel msgChannel = new Channel("notice", "Notice channel");
        userService.create(msgUser);
        channelService.create(msgChannel);

        Message message = new Message("Hello, World!", msgUser, msgChannel);
        messageService.create(message); // 등록
        System.out.println("등록된 메시지: " + message.getContent());

        message.update("Hello Updated World!");
        messageService.update(message.getId(), message); // 수정
        System.out.println("수정된 메시지: " + messageService.read(message.getId()).getContent());

        messageService.delete(message.getId()); // 삭제
        System.out.println("삭제 확인: " + messageService.read(message.getId()));

        // ===== Message 검증 테스트 (비정상 케이스) =====
        System.out.println("\n===== Message 검증 테스트 (비정상) =====");
        try {
            User ghostUser = new User("ghost", "ghost@example.com");
            Channel ghostChannel = new Channel("ghost", "Ghost channel");
            Message invalidMsg = new Message("Invalid", ghostUser, ghostChannel);

            messageService.create(invalidMsg); // ❌ 예외 발생
        } catch (IllegalArgumentException e) {
            System.out.println("예외 발생: " + e.getMessage());
        }
    }
}