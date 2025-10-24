package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.message.direct.DirectMessage;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.message.direct.DirectMessageService;
import com.sprint.mission.discodeit.user.UserService;
import com.sprint.mission.discodeit.common.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class DirectMessageView {
    private final DirectMessageService directMessageService;
    private final UserService userService;
    private final Scanner sc;
    private final SharedView sharedView;


    public void showDirectMessageMenu() {
        while (true) {
            System.out.println("\n--- DM(1:1 메시지) 관리 ---");
            System.out.println("1. DM 보내기");
            System.out.println("2. DM 대화 조회");
            System.out.println("3. DM 물리적 삭제");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> DM 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        sendDirectMessage();
                        break;
                    case 2:
                        viewDirectMessageConversation();
                        break;
                    case 3:
                        deleteDirectMessage();
                        break;
                    case 0:
                        return;
                    default:
                        System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println("작업 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private void sendDirectMessage() {
        System.out.println("--- 메시지를 보낼 사용자 선택 ---");
        User sender = sharedView.selectUserFromList(true);
        if (sender == null) return;

        System.out.println("--- 메시지를 받을 사용자 선택 ---");
        User receiver = sharedView.selectUserFromList(true);
        if (receiver == null) return;

        if (sender.getId().equals(receiver.getId())) {
            System.out.println("자기 자신에게는 DM을 보낼 수 없습니다.");
            return;
        }

        System.out.print("보낼 메시지 내용: ");
        String message = sc.nextLine();
        directMessageService.sendMessage(sender.getId(), receiver.getId(), message);
        System.out.println("DM 전송 완료.");
    }

    private void viewDirectMessageConversation() {
        System.out.println("--- 첫 번째 대화 상대 선택 ---");
        User userOne = sharedView.selectUserFromList(true);
        if (userOne == null) return;

        System.out.println("--- 두 번째 대화 상대 선택 ---");
        User userTwo = sharedView.selectUserFromList(true);
        if (userTwo == null) return;

        System.out.println("--- [" + userOne.getUsername() + " ↔ " + userTwo.getUsername() + "] 대화 내용 ---");
        List<DirectMessage> conversation = directMessageService.getConversation(userOne.getId(), userTwo.getId());
        if (conversation.isEmpty()) {
            System.out.println("대화 내용이 없습니다.");
            return;
        }
        conversation.forEach(msg -> {
            try {
                User sender = userService.findById(msg.getSenderId());
                System.out.printf("[%s] %s: %s\n",
                        DateUtils.formatMillis(msg.getCreatedAt()),
                        sender.getNickname(),
                        msg.getMessage());
            } catch (Exception e) {
                System.out.printf("[%s] (알수없음): %s\n",
                        DateUtils.formatMillis(msg.getCreatedAt()),
                        msg.getMessage());
            }
        });
    }

    private void deleteDirectMessage() {
        System.out.println("--- 대화 상대를 선택해주세요 ---");
        User userOne = sharedView.selectUserFromList(true);
        if (userOne == null) return;
        User userTwo = sharedView.selectUserFromList(true);
        if (userTwo == null) return;

        List<DirectMessage> messages = directMessageService.getConversation(userOne.getId(), userTwo.getId());
        if (messages.isEmpty()) {
            System.out.println("삭제할 메시지가 없습니다.");
            return;
        }
        for (int i = 0; i < messages.size(); i++) {
            DirectMessage msg = messages.get(i);
            User sender = userService.findById(msg.getSenderId());
            System.out.printf("%d. [%s] %s: %s\n", i + 1, DateUtils.formatMillis(msg.getCreatedAt()), sender.getNickname(), msg.getMessage());
        }
        System.out.println("0. 취소");
        System.out.print(">> 물리적으로 삭제할 메시지 번호 선택: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= messages.size()) {
            DirectMessage msgToDelete = messages.get(choice - 1);
            directMessageService.deleteById(msgToDelete.getId());
            System.out.println("DM 물리적 삭제 완료.");
        }
    }
}