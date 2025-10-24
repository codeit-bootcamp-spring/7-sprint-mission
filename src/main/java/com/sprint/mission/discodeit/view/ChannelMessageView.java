package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelMessageService;
import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.utils.AppConfigRegacy;
import com.sprint.mission.discodeit.utils.DateUtils;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ChannelMessageView {
    private final ChannelMessageService channelMessageService;
    private final UserService userService;
    private final Scanner sc;
    private final SharedView sharedView;

    public void showChannelMessageMenu() {
        while (true) {
            System.out.println("\n--- 채널 메시지 관리 ---");
            System.out.println("1. 채널 메시지 보내기");
            System.out.println("2. 채널 메시지 조회");
            System.out.println("3. 채널 메시지 논리적 삭제");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 채널 메시지 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1:
                        sendChannelMessage();
                        break;
                    case 2:
                        viewChannelMessages();
                        break;
                    case 3:
                        softDeleteChannelMessage();
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

    private void sendChannelMessage() {
        System.out.println("--- 메시지를 보낼 사용자 선택 ---");
        User sender = sharedView.selectUserFromList(true);
        if (sender == null) return;

        System.out.println("--- 메시지를 보낼 채널 선택 ---");
        Participation participation = sharedView.selectParticipationFromUser(sender);
        if (participation == null) {
            return;
        }

        System.out.print("보낼 메시지 내용: ");
        String message = sc.nextLine();
        channelMessageService.sendMessage(participation.getChannelId(), sender.getId(), message);
        System.out.println("메시지 전송 완료.");
    }

    private void viewChannelMessages() {
        System.out.println("--- 메시지를 조회할 채널 선택 ---");
        Channel channel = sharedView.selectChannelFromList(true);
        if (channel == null) return;

        System.out.println("--- [" + channel.getChannelName() + "] 채널 메시지 목록 ---");
        List<ChannelMessage> messages = channelMessageService.getMessagesByChannel(channel.getId());
        if (messages.isEmpty()) {
            System.out.println("메시지가 없습니다.");
            return;
        }
        messages.forEach(msg -> {
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

    private void softDeleteChannelMessage() {
        System.out.println("--- 메시지를 삭제할 채널 선택 ---");
        Channel channel = sharedView.selectChannelFromList(true);
        if (channel == null) return;

        List<ChannelMessage> messages = channelMessageService.getMessagesByChannel(channel.getId());
        if (messages.isEmpty()) {
            System.out.println("삭제할 메시지가 없습니다.");
            return;
        }
        for (int i = 0; i < messages.size(); i++) {
            ChannelMessage msg = messages.get(i);
            User sender = userService.findById(msg.getSenderId());
            System.out.printf("%d. [%s] %s: %s\n", i + 1, DateUtils.formatMillis(msg.getCreatedAt()), sender.getNickname(), msg.getMessage());
        }
        System.out.println("0. 취소");
        System.out.print(">> 삭제할 메시지 번호 선택: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= messages.size()) {
            ChannelMessage msgToDelete = messages.get(choice - 1);
            channelMessageService.softDeleteById(msgToDelete.getId());
            System.out.println("채널 메시지 논리적 삭제 완료.");
        }
    }
}