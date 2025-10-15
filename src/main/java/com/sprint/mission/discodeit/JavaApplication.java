package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.jcf.*;

import java.util.*;

/**
 * ✅ JavaApplication (통합 실행 클래스)
 * - UserService, ChannelService, MessageService를 통합 테스트한다.
 * - 순서:
 *   1. 사용자 생성
 *   2. 채널 생성
 *   3. 메시지 생성 (연관관계 검증 포함)
 *   4. 수정 / 조회 / 삭제 테스트
 */
public class JavaApplication {

    public static void main(String[] args) {

        System.out.println("=== 📡 Discord Mini-Service Integration Test Start ===\n");

        //  서비스 생성 (검증 가능한 버전)
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(); // 인수 없이 호출

        //  사용자 생성
        User user1 = userService.create("우유", "milk@example.com");
        User user2 = userService.create("초코우유", "choco@example.com");

        System.out.println("[USER] 생성 완료:");
        System.out.println(" - " + user1.getId() + " / " + user1.getUsername());
        System.out.println(" - " + user2.getId() + " / " + user2.getUsername());

        // 3️⃣ 채널 생성
        Channel channel1 = channelService.create("공지사항");
        Channel channel2 = channelService.create("잡담방");

        System.out.println("\n[CHANNEL] 생성 완료:");
        System.out.println(" - " + channel1.getId() + " / " + channel1.getChannelName());
        System.out.println(" - " + channel2.getId() + " / " + channel2.getChannelName());

        // 4️⃣ 메시지 생성 (연관관계 검증 포함)
        Message msg1 = messageService.create(user1.getId(), channel1.getId(), "안녕하세요! 우유입니다 🥛");
        Message msg2 = messageService.create(user2.getId(), channel2.getId(), "초코우유 왔다 🍫");

        System.out.println("\n[MESSAGE] 생성 완료:");
        System.out.println(" - " + msg1.getId() + " / " + msg1.getContent());
        System.out.println(" - " + msg2.getId() + " / " + msg2.getContent());

        // 5️⃣ 메시지 수정
        messageService.updateContent(msg1.getId(), "수정된 메시지 내용!");
        System.out.println("\n[MESSAGE] 수정 결과:");
        System.out.println(" - " + msg1.getId() + " / " + msg1.getContent());

        // 6️⃣ 전체 메시지 조회
        System.out.println("\n[MESSAGE] 전체 조회:");
        for (Message m : messageService.readAll()) {
            System.out.println(" - " + m.getId() + " / content=" + m.getContent());
        }

        // 7️⃣ 사용자 비활성화 테스트
        userService.deactivate(user2.getId());
        System.out.println("\n[USER] 비활성화 확인:");
        System.out.println(" - " + user2.getUsername() + " → active=" + user2.isActive());

        // 8️⃣ 삭제 테스트
        boolean deleted = messageService.delete(msg2.getId());
        System.out.println("\n[MESSAGE] 삭제 결과: " + deleted);

        System.out.println("\n=== ✅ 테스트 완료! Discord Mini-Service 정상 작동 ===");
    }
}