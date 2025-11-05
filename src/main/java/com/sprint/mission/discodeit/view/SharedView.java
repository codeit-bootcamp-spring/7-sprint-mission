package com.sprint.mission.discodeit.view;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.participation.dto.ParticipationResponseDTO;
import com.sprint.mission.discodeit.user.User;
import com.sprint.mission.discodeit.channel.ChannelService;
import com.sprint.mission.discodeit.participation.ParticipationService;
import com.sprint.mission.discodeit.user.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Scanner;

@Component
@RequiredArgsConstructor
public class SharedView {
    private final UserService userService;
    private final ChannelService channelService;
    private final ParticipationService participationService;
    private final Scanner sc;

    /**
     * 사용자 목록을 출력하고 사용자로부터 한 명을 선택받습니다.
     * @param nonDeletedOnly true이면 활성 사용자만, false이면 모든 사용자를 보여줍니다.
     * @return 선택된 User 객체, 취소 시 null을 반환합니다.
     */
    public User selectUserFromList(boolean nonDeletedOnly) {
        List<User> users = nonDeletedOnly ? userService.findAllNonDel() : userService.findAll();
        if (users.isEmpty()) {
            System.out.println("선택할 수 있는 사용자가 없습니다.");
            return null;
        }

        for (int i = 0; i < users.size(); i++) {
            User u = users.get(i);
            System.out.printf("%d. %s (닉네임: %s, 삭제됨: %s)\n", i + 1, u.getUsername(), u.getNickname(), u.isDeleted());
        }
        System.out.println("0. 취소");
        System.out.print(">> 번호를 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= users.size()) {
            return users.get(choice - 1);
        }
        return null;
    }

    /**
     * 채널 목록을 출력하고 사용자로부터 하나를 선택받습니다.
     * @param nonDeletedOnly true이면 활성 채널만, false이면 모든 채널을 보여줍니다.
     * @return 선택된 Channel 객체, 취소 시 null을 반환합니다.
     */
    public Channel selectChannelFromList(boolean nonDeletedOnly) {
        List<Channel> channels = nonDeletedOnly ? channelService.findAllNonDel() : channelService.findAll();
        if (channels.isEmpty()) {
            System.out.println("선택할 수 있는 채널이 없습니다.");
            return null;
        }

        for (int i = 0; i < channels.size(); i++) {
            Channel ch = channels.get(i);
            System.out.printf("%d. %s (ID: %s, 삭제됨: %s)\n", i + 1, ch.getChannelName(), ch.getId(), ch.isDeleted());
        }
        System.out.println("0. 취소");
        System.out.print(">> 번호를 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= channels.size()) {
            return channels.get(choice - 1);
        }
        return null;
    }

    /**
     * 특정 사용자의 참여 정보를 보여주고 하나를 선택받습니다.
     * @param user 참여 정보를 조회할 User 객체
     * @return 선택된 Participation 객체, 취소 시 null을 반환합니다.
     */
    public ParticipationResponseDTO selectParticipationFromUser(User user) {
        List<ParticipationResponseDTO> participations = participationService.findParticipationsByUserId(user.getId());

        if (participations.isEmpty()) {
            System.out.println("해당 유저가 참여중인 채널 정보가 없습니다.");
            return null;
        }

        System.out.println("--- 채널 참여 정보 선택 ---");
        for (int i = 0; i < participations.size(); i++) {
            ParticipationResponseDTO p = participations.get(i);
            try {
                Channel c = channelService.findById(p.participationDualKey().channelId());
                System.out.printf("%d. 채널: %s (닉네임: %s)\n", i + 1, c.getChannelName(), p.nickname());
            } catch (Exception e) {
                System.out.printf("%d. 알 수 없는 채널 (ID: %s)\n", i + 1, p.participationDualKey().channelId());
            }
        }
        System.out.println("0. 취소");
        System.out.print(">> 번호를 선택하세요: ");
        int choice = sc.nextInt();
        sc.nextLine();

        if (choice > 0 && choice <= participations.size()) {
            return participations.get(choice - 1);
        }
        return null;
    }
}