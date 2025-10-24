package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.participation.Participation;
import com.sprint.mission.discodeit.config.enums.Role;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class ParticipationView {
    private final ParticipationService participationService;
    private final UserService userService;
    private final ChannelService channelService;
    private final Scanner sc;
    private final SharedView sharedView; // 공통 헬퍼 View

    public void showParticipationMenu() {
        while (true) {
            System.out.println("\n--- 참여 관리 ---");
            System.out.println("1. 채널 참여하기");
            System.out.println("2. 채널 나가기");
            System.out.println("3. 참여자 강퇴하기 (관리자)"); // 기능 추가
            System.out.println("4. 참여자 역할 변경 (관리자)"); // 기능 추가
            System.out.println("5. 채널 닉네임 변경"); // 기능 추가
            System.out.println("6. 특정 채널의 참여자 목록 조회");
            System.out.println("7. 특정 유저의 참여 채널 목록 조회");
            System.out.println("0. 이전 메뉴로");
            System.out.print(">> 참여 관리 메뉴: ");


            int choice = sc.nextInt();
            sc.nextLine();

            try {
                switch (choice) {
                    case 1: joinChannel(); break;
                    case 2: leaveChannel(); break;
                    case 3: kickUserFromChannel(); break; // 기능 연결
                    case 4: changeRole(); break; // 기능 연결
                    case 5: changeNickname(); break; // 기능 연결
                    case 6: findParticipantsByChannel(); break;
                    case 7: findChannelsByUser(); break;
                    case 0: return;
                    default: System.out.println("잘못된 입력입니다.");
                }
            } catch (Exception e) {
                System.out.println("작업 중 오류 발생: " + e.getMessage());
            }
        }
    }

    private void joinChannel() {
        System.out.println("--- 참여할 사용자 선택 ---");
        User user = sharedView.selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- 참여할 채널 선택 ---");
        Channel channel = sharedView.selectChannelFromList(true);
        if (channel == null) return;

        System.out.print("채널에서 사용할 닉네임: ");
        String nickname = sc.nextLine();
        participationService.joinChannel(channel.getId(), user.getId(), nickname);
        System.out.println("'" + user.getUsername() + "'님이 '" + channel.getChannelName() + "' 채널에 참여 완료.");
    }

    private void kickUserFromChannel() {
        System.out.println("--- 작업 대상 채널 선택 ---");
        Channel channel = sharedView.selectChannelFromList(true);
        if (channel == null) return;

        System.out.println("--- 강퇴시킬 사용자 선택 ---");
        User targetUser = sharedView.selectUserFromList(true);
        if (targetUser == null) return;

        System.out.println("--- 작업을 수행할 관리자 선택 ---");
        User admin = sharedView.selectUserFromList(true);
        if (admin == null) return;

        participationService.kickUserFromChannel(channel.getId(), targetUser.getId(), admin.getId());
        System.out.println("사용자를 채널에서 강퇴했습니다.");
    }

    private void changeRole() {
        System.out.println("--- 작업 대상 채널 선택 ---");
        Channel channel = sharedView.selectChannelFromList(true);
        if (channel == null) return;

        System.out.println("--- 역할을 변경할 사용자 선택 ---");
        User targetUser = sharedView.selectUserFromList(true);
        if (targetUser == null) return;

        System.out.println("--- 작업을 수행할 관리자 선택 ---");
        User admin = sharedView.selectUserFromList(true);
        if (admin == null) return;

        System.out.print("새 역할 (ADMIN, USER, GUEST): ");
        String roleStr = sc.nextLine();
        Role newRole;
        try {
            newRole = Role.valueOf(roleStr.toUpperCase());
        } catch (IllegalArgumentException e) {
            System.out.println("존재하지 않는 역할입니다.");
            return;
        }
    }
    private void changeNickname() {
        System.out.println("--- 닉네임을 변경할 사용자 선택 ---");
        User user = sharedView.selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- 닉네임을 변경할 채널 선택 ---");
        Participation participation = sharedView.selectParticipationFromUser(user);
        if (participation == null) return;

        System.out.print("새 닉네임: ");
        String newNickname = sc.nextLine();

        participationService.changeNickname(participation.getChannelId(), user.getId(), newNickname);
        System.out.println("채널 닉네임 변경 완료.");
    }

    private void leaveChannel() {
        System.out.println("--- 나갈 사용자 선택 ---");
        User user = sharedView.selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- 나갈 채널 선택 ---");
        Participation participation = sharedView.selectParticipationFromUser(user);
        if (participation == null) return;

        participationService.leaveChannel(participation.getChannelId(), user.getId());
        System.out.println("채널 나가기 완료.");
    }

    private void deleteParticipation() {
        System.out.println("--- 참여 정보를 삭제할 사용자 선택 ---");
        User user = sharedView.selectUserFromList(false); // 모든 사용자
        if (user == null) return;

        System.out.println("--- 삭제할 참여 정보 선택 ---");
        Participation participation = sharedView.selectParticipationFromUser(user);
        if (participation == null) return;

        participationService.deleteById(participation.getId());
        System.out.println("참여 정보 물리적 삭제 완료.");
    }


    private void findParticipantsByChannel() {
        System.out.println("--- 참여자 목록을 조회할 채널 선택 ---");
        Channel channel = sharedView.selectChannelFromList(true);
        if (channel == null) return;

        System.out.println("--- '" + channel.getChannelName() + "' 참여자 목록 ---");
        participationService.findParticipationsByChannelId(channel.getId())
                .forEach(p -> {
                    try {
                        User user = userService.findById(p.getUserId());
                        System.out.println("- " + user.getUsername() + " (닉네임: " + p.getNickname() + ", 역할: " + p.getRole() + ")");
                    } catch (Exception e) {
                        System.out.println("- " + p.getUserId() + " (사용자 정보 없음)");
                    }
                });
    }

    private void findChannelsByUser() {
        System.out.println("--- 조회할 사용자 선택 ---");
        User user = sharedView.selectUserFromList(true);
        if (user == null) return;

        System.out.println("--- '" + user.getUsername() + "'님이 참여 중인 채널 목록 ---");
        participationService.findParticipationsByUserId(user.getId())
                .forEach(p -> {
                    try {
                        Channel channel = channelService.findById(p.getChannelId());
                        System.out.println("- " + channel.getChannelName() + " (사용 닉네임: " + p.getNickname() + ")");
                    } catch (Exception e) {
                        System.out.println("- " + p.getChannelId() + " (채널 정보 없음)");
                    }
                });
    }
}