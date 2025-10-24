package com.sprint.mission.discodeit.view;

import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class MessageView {
    private final ChannelMessageView channelMessageView;
    private final DirectMessageView directMessageView;
    private final SharedView sharedView;
    private final Scanner sc;


    public void showMessageMenu() {
        while (true) {
            System.out.println("\n--- 메시지 관리 ---");
            System.out.println("1. 채널 메시지 관리");
            System.out.println("2. DM(1:1 메시지) 관리");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 메시지 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            switch (choice) {
                case 1:
                    channelMessageView.showChannelMessageMenu();
                    break;
                case 2:
                    directMessageView.showDirectMessageMenu();
                    break;
                case 0:
                    return;
                default:
                    System.out.println("잘못된 입력입니다.");
            }
        }
    }
}