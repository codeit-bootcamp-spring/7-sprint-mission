package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.config.enums.ChannelType;
import com.sprint.mission.discodeit.channel.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ChannelView {
    private final ChannelService channelService;
    private final Scanner sc;
    private final SharedView sharedView;

    public void showChannelMenu() {
        while (true) {
            System.out.println("\n--- 채널 관리 ---");
            System.out.println("1. 채널 생성");
            System.out.println("2. 채널 설정 변경");
            System.out.println("3. 채널 논리적 삭제");
            System.out.println("4. 채널 물리적 삭제");
            System.out.println("5. 모든 채널 조회");
            System.out.println("6. 활성 채널 조회");
            System.out.println("7. 채널 이름으로 검색"); // 기능 추가
            System.out.println("8. 조건부 채널 검색"); // 기능 추가
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 채널 관리 메뉴: ");

            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1: createChannel(); break;
                    case 2: changeChannelSettings(); break;
                    case 3: softDeleteChannel(); break;
                    case 4: deleteChannel(); break;
                    case 5: channelService.findAll().forEach(System.out::println); break;
                    case 6: channelService.findAllNonDel().forEach(System.out::println); break;
                    case 7: findChannelByName(); break; // 기능 연결
                    case 8: findChannelsBySettings(); break; // 기능 연결
                    case 0: return;
                    default: System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println("작업 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private void createChannel() {
        System.out.print("채널 이름: ");
        String name = sc.nextLine();
        Channel newChannel = channelService.create(name, null, null, false);
        System.out.println("채널 생성 완료: " + newChannel);
    }

    private void findChannelByName() {
        System.out.print("검색할 채널 이름: ");
        String name = sc.nextLine();
        Channel channel = channelService.findByChannelNameNonDel(name);
        System.out.println("검색 결과: " + channel);
    }

    private void findChannelsBySettings() {
        ChannelType type = null;
        System.out.print("채널 이름 (없으면 Enter): ");
        String name = sc.nextLine();
        System.out.print("채널 주제 (없으면 Enter): ");
        String topic = sc.nextLine();
        System.out.print("채널 타입 1.CHAT, 2.VIDEO, 3.TEST, 모두 검색. 4");
        switch (sc.nextInt()){
            case 1:
                type = ChannelType.CHAT;
                break;
            case 2:
                type = ChannelType.VIDEO;
                break;
            case 3:
                type = ChannelType.TEST;
                break;
            case 4:
                break;
        }

        List<Channel> result = channelService.findAllChannelsBySettingsNonDel(
                name.isBlank() ? null : name,
                type,
                topic.isBlank() ? null : topic
        );

        System.out.println("--- 검색 결과 ---");
        if (result.isEmpty()) {
            System.out.println("조건에 맞는 채널이 없습니다.");
        } else {
            result.forEach(System.out::println);
        }
    }

    private void changeChannelSettings() {
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;

        ChannelType newType = null;
        System.out.print("새 채널 이름 (변경 없으면 Enter): ");
        String newName = sc.nextLine();
        System.out.print("채널 타입 1. CHAT, 2. VIDEO, 3. TEST, 4. 변경 안함");
        switch (sc.nextInt()){
            case 1:
                newType = ChannelType.CHAT;
                break;
            case 2:
                newType = ChannelType.VIDEO;
                break;
            case 3:
                newType = ChannelType.TEST;
                break;
            case 4:
                break;
        }
        System.out.print("새 채널 주제 (변경 없으면 Enter): ");
        String newTopic = sc.nextLine();
        System.out.print("채널 공개/비공개 설정 :");
        System.out.print("1. 공개, 2. 비공개, 3. 유지");
        boolean chPrivate = channel.isPrivate();
        switch (sc.nextInt()){
            case 1:
                chPrivate = false;
                break;
            case 2:
                chPrivate = true;
                break;
            case 3:
                break;
        }
        
        channelService.changeSettings(channel.getId(), newName.isBlank() ? null : newName, newType, newTopic.isBlank() ? null : newTopic, chPrivate);
        System.out.println("채널 설정 변경 완료.");
    }

    private void softDeleteChannel() {
        Channel channel = selectChannelFromList(true);
        if (channel == null) return;
        channelService.softDeleteById(channel.getId());
        System.out.println("'" + channel.getChannelName() + "' 채널 논리적 삭제 완료.");
    }

    private void deleteChannel() {
        Channel channel = selectChannelFromList(false);
        if (channel == null) return;
        channelService.deleteById(channel.getId());
        System.out.println("'" + channel.getChannelName() + "' 채널 물리적 삭제 완료.");
    }

    // 공통 헬퍼 메서드
    public Channel selectChannelFromList(boolean nonDeletedOnly) {
        return sharedView.selectChannelFromList(nonDeletedOnly);
    }
}