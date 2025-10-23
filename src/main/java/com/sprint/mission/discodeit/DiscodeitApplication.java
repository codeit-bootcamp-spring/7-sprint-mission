package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.dto.channelDto.ChannelInfo;
import com.sprint.mission.discodeit.entity.dto.messageDto.MessageInfo;
import com.sprint.mission.discodeit.entity.dto.userDto.UserInfoDto;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    // JavaApplication의 main을 제외한 메소드
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
		ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);

        // JavaApplication의 main 메소드에서 Service를 초기화하는 코드
        UserService userService = ctx.getBean(UserService.class);
        ChannelService channelService = ctx.getBean(ChannelService.class);
        MessageService messageService = ctx.getBean(MessageService.class);

        // JavaApplication의 main 메소드의 셋업
        System.out.println("\n---️ 데이터 셋업 ---");
        UserInfoDto admin = userCreateOrLoad(userService, "admin@discodeit.com", "AdminPass1!", "관리자");
        UserInfoDto user = userCreateOrLoad(userService, "user@discodeit.com", "UserPass1!", "일반유저", "010-1234-5678");
        UUID adminId = admin.getId();
        UUID userId = user.getId();

        ChannelInfo noticeChannel = channelCreateOrLoad(channelService, adminId, "공지사항", ChannelType.TEXT);
        UUID noticeChannelId = noticeChannel.getId();

        channelService.addMemberToChannel(noticeChannelId, userId);

        // ================================================================

        // --- 3. 메시지 기능 테스트 ---
        System.out.println("\n--- 메시지 기능 테스트 ---");
        messageService.createChannelMessage(adminId, noticeChannelId, "여기는 공지 채널입니다.");
        MessageInfo userMessage = messageService.createChannelMessage(userId, noticeChannelId, "반갑습니다.");
        messageService.createDirectMessage(adminId, userId, "안녕하세요!");

        List<MessageInfo> channelMessages = messageService.findChannelMessage(noticeChannelId);
        System.out.println("\n[공지사항 채널 대화 내용]");
        channelMessages.forEach(System.out::println);
        List<MessageInfo> directMessages = messageService.findMessageBetweenUsers(adminId, userId);
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
        System.out.println("\n--- 일반 유저 논리 삭제 후 채널정보 출력 ---");
        userService.deleteUser(userId);
        Optional<UserInfoDto> deletedUser = userService.findUserInfoById(userId);
        System.out.println(channelService.findChannelInfoById(noticeChannelId));


        // --- 6. 최종 상태 확인 ---
        System.out.println("\n--- 최종 데이터 상태 확인 ---");
        System.out.println("[삭제 후 공지사항 채널 대화 내용]");
        List<MessageInfo> finalMessages = messageService.findChannelMessage(noticeChannelId);
        finalMessages.forEach(System.out::println);

	}
    /* [ ] JavaApplication과 DiscodeitApplication에서 Service를 초기화하는 방식의 차이에 대해 다음의 키워드를 중심으로 정리해보세요.
    - IoC Container (제어 역전)
    - Dependency Injection (의존성 주입)
    - Bean

    JavaApplication 에서는  사용자가 new 등을 이용하여 직접 객체를 만들고 의존성을 수동으로 연결해줍니다.
    하지만 File*Repository와 Basic*Service에 @Repository 나 @Service를 붙여 Bean으로 만들면
    이들의 제어권이 개발자에서 Spring으로 역전이 됩니다. 그리고 DiscodeitApplication에서 *Service 선언 시 필요한 객체들을
    Spring이 적절한 Bean을 찾아 의존성을 자동으로 주입해 줍니다.

     */


}
