package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.Channel;

import com.sprint.mission.discodeit.service.UserService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;

import com.sprint.mission.discodeit.service.jcf.JCFUserService;
import com.sprint.mission.discodeit.service.jcf.JCFChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFMessageService;

import java.util.List;
import java.util.UUID;

/**
 * ✅ MessageService 기본 동작 테스트 (연관관계 검증 X)
 * 흐름:
 *  1) User / Channel 생성
 *  2) Message 생성
 *  3) 단건 조회 / 전체 조회
 *  4) 내용 수정
 *  5) 삭제 및 확인
 */
public class MessageServiceTest {
    public static void main(String[] args) {
        // --- 준비: User/Channel/Message 서비스 준비 (기본 구현체) ---
        UserService userService = new JCFUserService();
        ChannelService channelService = new JCFChannelService();
        MessageService messageService = new JCFMessageService(); // ← 기본(검증 없음)

        // 1) User / Channel 생성
        User user = userService.create("우유", "milk@example.com");
        Channel channel = channelService.create("공지사항"); // topic은 나중에 업데이트하는 설계

        System.out.println("[1] 생성된 User: " + user.getId() + " / " + user.getUsername());
        System.out.println("[1] 생성된 Channel: " + channel.getId() + " / " + channel.getChannelName());

        // 2) Message 생성
        Message m = messageService.create(user.getId(), channel.getId(), "안녕하세요!");
        System.out.println("\n[2] 생성된 Message: " + m.getId() + " / content=" + m.getContent());

        // 3) 단건 조회 / 전체 조회
        Message found = messageService.read(m.getId());
        System.out.println("\n[3-1] 단건 조회: " + found.getId() + " / content=" + found.getContent());

        List<Message> all = messageService.readAll();
        System.out.println("[3-2] 전체 조회 개수: " + all.size());

        // 4) 내용 수정
        messageService.updateContent(m.getId(), "내용 수정됨!");
        System.out.println("\n[4] 수정 후 content: " + messageService.read(m.getId()).getContent());

        // 5) 삭제 및 확인
        boolean deleted = messageService.delete(m.getId());
        System.out.println("\n[5-1] 삭제 결과: " + deleted);

        Message afterDelete = messageService.read(m.getId());
        System.out.println("[5-2] 삭제 확인(읽기 결과): " + afterDelete); // null이면 정상
    }
}