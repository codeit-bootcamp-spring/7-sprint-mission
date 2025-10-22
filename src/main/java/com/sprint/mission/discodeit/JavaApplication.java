package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.file.FileUserService;
import com.sprint.mission.discodeit.service.file.FileChannelService;
import com.sprint.mission.discodeit.service.file.FileMessageService;
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

        System.out.println("=== 📡 Discord Mini-Service Integration Test (File I/O) ===\n");

        // 1) 파일 I/O 영속화 버전 서비스
        UserService userService = new FileUserService();
        ChannelService channelService = new FileChannelService();
        MessageService messageService = new FileMessageService();

        // 2) 사용자 생성
        User user1 = userService.create("우유", "milk@example.com");
        User user2 = userService.create("초코우유", "choco@example.com");

        System.out.println("[USER] 생성 완료:");
        System.out.println(" - " + user1.getId() + " / " + user1.getUsername());
        System.out.println(" - " + user2.getId() + " / " + user2.getUsername());

        // 3) 채널 생성
        Channel channel1 = channelService.create("공지사항");
        Channel channel2 = channelService.create("잡담방");

        System.out.println("\n[CHANNEL] 생성 완료:");
        System.out.println(" - " + channel1.getId() + " / " + channel1.getChannelName());
        System.out.println(" - " + channel2.getId() + " / " + channel2.getChannelName());

        // 4) 메시지 생성
        Message msg1 = messageService.create(user1.getId(), channel1.getId(), "안녕하세요! 우유입니다 🥛");
        Message msg2 = messageService.create(user2.getId(), channel2.getId(), "초코우유 왔다 🍫");

        System.out.println("\n[MESSAGE] 생성 완료:");
        System.out.println(" - " + msg1.getId() + " / " + msg1.getContent());
        System.out.println(" - " + msg2.getId() + " / " + msg2.getContent());

        // 5) 메시지 수정
        messageService.updateContent(msg1.getId(), "수정된 메시지 내용!");
        // ⚠️ 수정 후에는 서비스에서 다시 읽어서 출력하는 습관이 안전함
        Message refreshed = messageService.read(msg1.getId());
        System.out.println("\n[MESSAGE] 수정 결과:");
        System.out.println(" - " + refreshed.getId() + " / " + refreshed.getContent());

        // 6) 전체 메시지 조회
        System.out.println("\n[MESSAGE] 전체 조회:");
        for (Message m : messageService.readAll()) {
            System.out.println(" - " + m.getId() + " / content=" + m.getContent());
        }

        // 7) 사용자 비활성화 테스트
        userService.deactivate(user2.getId());
        System.out.println("\n[USER] 비활성화 확인:");
        System.out.println(" - " + user2.getUsername() + " → active=" + userService.read(user2.getId()).isActive());

        // 8) 삭제 테스트
        boolean deleted = messageService.delete(msg2.getId());
        System.out.println("\n[MESSAGE] 삭제 결과: " + deleted);

        // 9) 현재 개수 확인
        System.out.println("\n[COUNT] now:");
        System.out.println(" - users=" + userService.readAll().size());
        System.out.println(" - channels=" + channelService.readAll().size());
        System.out.println(" - messages=" + messageService.readAll().size());

        // 10) === 재시작 시나리오 모의 ===
        // 새 서비스 인스턴스를 만들면, 내부 File*Repository가 파일에서 자동 로드함.
        userService = new FileUserService();
        channelService = new FileChannelService();
        messageService = new FileMessageService();

        // 11) 재시작 후 데이터 유지 확인
        List<User> usersAfterRestart = userService.readAll();
        List<Channel> channelsAfterRestart = channelService.readAll();
        List<Message> messagesAfterRestart = messageService.readAll();

        System.out.println("\n[RESTART] After re-instantiation (auto-load from files):");
        System.out.println(" - users=" + usersAfterRestart.size());
        System.out.println(" - channels=" + channelsAfterRestart.size());
        System.out.println(" - messages=" + messagesAfterRestart.size());

        System.out.println("\n=== ✅ File I/O Persistence Test Completed ===");
    }
}