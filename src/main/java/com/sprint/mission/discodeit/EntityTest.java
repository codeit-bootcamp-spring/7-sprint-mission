package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import java.util.UUID;

public class EntityTest {
    public static void main(String[] args) {
        // 1️⃣ User 생성
        User user = new User("우유", "milk@example.com");
        System.out.println("User ID: " + user.getId());
        System.out.println("User Name: " + user.getUsername());
        System.out.println("User Email: " + user.getEmail());
        System.out.println("User Active: " + user.isActive());
        System.out.println("CreatedAt: " + user.getCreatedAt());

        // User 이름 수정
        user.updateUsername("초코우유");
        System.out.println("Updated Name: " + user.getUsername());
        System.out.println("UpdatedAt: " + user.getUpdatedAt());
        System.out.println("------");

        // 2️⃣ Channel 생성
        Channel channel = new Channel("공지사항");
        System.out.println("Channel ID: " + channel.getId());
        System.out.println("Channel Name: " + channel.getChannelName());
        System.out.println("Channel Topic: " + channel.getChannelTopic());

        // Channel 주제 업데이트
        channel.updateChannelTopic("팀 공지와 업데이트 공유");
        System.out.println("Updated Topic: " + channel.getChannelTopic());
        System.out.println("------");

        // 3️⃣ Message 생성
        Message message = new Message(user.getId(), channel.getId(), "안녕하세요!");
        System.out.println("Message ID: " + message.getId());
        System.out.println("Sender ID: " + message.getSenderId());
        System.out.println("Channel ID: " + message.getChannelId());
        System.out.println("Content: " + message.getContent());

        // Message 수정
        message.updateContent("수정된 메시지");
        System.out.println("Updated Content: " + message.getContent());
        System.out.println("UpdatedAt: " + message.getUpdatedAt());
    }
}
