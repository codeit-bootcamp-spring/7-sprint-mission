package com.sprint.mission;

import com.sprint.mission.discodeit.entity.*;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfo;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfoDto;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.service.*;
import com.sprint.mission.discodeit.service.basic.*;


import java.util.List;
import java.util.Optional;
import java.util.UUID;

public class JavaApplication {

    private static UserInfoDto userCreateOrLoad(UserService userService
            , String email, String password, String userName, String phoneNum) {
        return userService.findUserInfoByEmail(email)
                .orElseGet(() -> {
                    System.out.println("새로운 계정 가입: " + userName);
                    return userService.createUser(email, password, userName, phoneNum);
                });
    }

    private static UserInfoDto userCreateOrLoad(UserService userService
            , String email, String password, String userName) {
        return userCreateOrLoad(userService, email, password, userName, null);
    }

    private static ChannelInfo channelCreateOrLoad(ChannelService channelService
            , UUID adminId, String channelName, ChannelType type) {
        return channelService.findChannelInfoByChannelName(channelName)
                .orElseGet(() -> {
                    System.out.println("새로운 채널 개설: " + channelName);
                    return channelService.createChannel(adminId, channelName, type);
                });
    }

    public static void main(String[] args) {

//        UserRepository userRepository = new JCFUserRepository();
//        ChannelRepository channelRepository = new JCFChannelRepository();
//        MessageRepository messageRepository = new JCFMessageRepository();

        // FileRepository를 사용할 경우
        UserRepository userRepository = new FileUserRepository();
        ChannelRepository channelRepository = new FileChannelRepository();
        MessageRepository messageRepository = new FileMessageRepository();

        UserService userService = new BasicUserService(userRepository, channelRepository);
        ChannelService channelService = new BasicChannelService(channelRepository, userService);
        MessageService messageService = new BasicMessageService(messageRepository, userService, channelService);

        // --- 2. 테스트 데이터 생성 ---
        System.out.println("\n---️ 테스트 데이터 설정 ---");
        UserInfoDto admin = userCreateOrLoad(userService, "admin@discodeit.com", "AdminPass1!", "관리자");
        UserInfoDto user = userCreateOrLoad(userService, "user@discodeit.com", "UserPass1!", "일반유저");
        UUID adminId = admin.getId();
        UUID userId = user.getId();

        ChannelInfo noticeChannel = channelCreateOrLoad(channelService, adminId, "공지사항", ChannelType.TEXT);
        UUID noticeChannelId = noticeChannel.getId();

        System.out.println("--- 채널 멤버 추가 ---");     // 버그 발생...
        channelService.addMemberToChannel(noticeChannelId, userId);


        // --- 3. 메시지 기능 테스트 ---
        System.out.println("\n--- 메시지 기능 테스트 ---");
        messageService.createChannelMessage(adminId, noticeChannelId, "여기는 공지 채널입니다.");
        MessageInfoDto userMessage = messageService.createChannelMessage(userId, noticeChannelId, "반갑습니다.");
        messageService.createDirectMessage(adminId, userId, "안녕하세요!");

        List<MessageInfoDto> channelMessages = messageService.findChannelMessage(noticeChannelId);
        System.out.println("\n[공지사항 채널 대화 내용]");
        channelMessages.forEach(System.out::println);
        List<MessageInfoDto> directMessages = messageService.findMessageBetweenUsers(adminId, userId);
        System.out.println("\n[둘의 대화 내용]");
        directMessages.forEach(System.out::println);


        // --- 4. 관리자 삭제 불가 테스트 ---
        System.out.println("\n--- 관리자 삭제 방지 기능 테스트 ---");
        try {
            System.out.println("관리자 계정 삭제 중...");
            userService.deleteUser(adminId);
        } catch (Exception e) {
            System.out.println("오류 발생: " + e.getMessage());
        }


        // --- 5. 일반 유저 삭제 테스트 (논리 삭제) ---
        System.out.println("\n--- 일반 유저 논리 삭제 ---");
        userService.deleteUser(userId);
        Optional<UserInfoDto> deletedUser = userService.findUserInfoById(userId);


        // --- 6. 최종 상태 확인 ---
        System.out.println("\n--- 최종 데이터 상태 확인 ---");
        System.out.println("[삭제 후 공지사항 채널 대화 내용]");
        List<MessageInfoDto> finalMessages = messageService.findChannelMessage(noticeChannelId);
        finalMessages.forEach(System.out::println);

        System.out.println(" 테스트가 종료되었습니다.");
    }
}