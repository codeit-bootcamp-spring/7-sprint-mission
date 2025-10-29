package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.dto.user.LoginRequest;
import com.sprint.mission.discodeit.dto.channel.ChannelDto;
import com.sprint.mission.discodeit.dto.channel.CreatePublicChannelRequest;
import com.sprint.mission.discodeit.dto.message.MessageCreateRequest;
import com.sprint.mission.discodeit.dto.message.MessageDto;
import com.sprint.mission.discodeit.dto.readstatus.ReadStatusCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserCreateRequest;
import com.sprint.mission.discodeit.dto.user.UserDto;

import com.sprint.mission.discodeit.service.AuthService;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.ReadStatusService;
import com.sprint.mission.discodeit.service.UserService;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);

        UserService userService = ctx.getBean(UserService.class);
        AuthService authService = ctx.getBean(AuthService.class);
        ChannelService channelService = ctx.getBean(ChannelService.class);
        MessageService messageService = ctx.getBean(MessageService.class);
        ReadStatusService readStatusService = ctx.getBean(ReadStatusService.class);

        // 1) 유저 생성 (create 가 UserDto 를 직접 반환하므로 orElseThrow 제거)
        UserDto created = userService.create(
                new UserCreateRequest("woody", "woody@codeit.com", "woody1234", null)
        );
        System.out.println("[CREATE] " + created.username() + ", id=" + created.id());
        UUID userId = created.id();

        // 2) 로그인
        var login = authService.login(new LoginRequest(created.username(), "woody1234"));
        System.out.println("[LOGIN] " + login.user().username() + " 로그인 성공");

        // 3) PUBLIC 채널 생성
        ChannelDto channel = channelService.createPublic(
                new CreatePublicChannelRequest("공지", "공지 채널입니다.")
        );
        System.out.println("[CHANNEL] 생성 완료, id=" + channel.id()
                + ", name=" + channel.name()
                + ", visibility=" + channel.visibility());
        UUID channelId = channel.id();

        try { Thread.sleep(200); } catch (InterruptedException ignored) {}

        // 4) 메시지 생성
        MessageDto message = messageService.create(
                new MessageCreateRequest(channelId, userId, "안녕하세요 첫 메시지 입니다!", null)
        );
        System.out.println("[MESSAGE] " + message.content()
                + " (작성자: " + created.username() + ", createdAt=" + message.createdAt() + ")");

        // 5) ReadStatus 생성 (create 가 UUID 를 반환한다고 가정)
        UUID rsId = readStatusService.create(
                new ReadStatusCreateRequest(userId, channelId, Instant.now())
        );

        // find 가 Optional<ReadStatus> 를 반환한다고 가정 → 엔티티 getter 사용
        readStatusService.find(rsId).ifPresent(rs -> {
            System.out.println("[READ] saved lastReadAt=" + rs.getLastReadAt()
                    + " (user=" + rs.getUserId() + ", channel=" + rs.getChannelId() + ")");
        });

        // 6) 내가 볼 수 있는 채널 목록
        var visibles = channelService.findAllByUserId(userId);
        System.out.println("[VISIBLE] count=" + visibles.size());
        for (ChannelDto dto : visibles) {
            System.out.println(" - id=" + dto.id()
                    + ", name=" + dto.name()
                    + ", visibility=" + dto.visibility()
                    + ", latest=" + dto.latest()
                    + ", participants=" + dto.participants());
        }

        // 7) 채널 삭제 (연관 데이터 정리)
        channelService.delete(channelId);
        System.out.println("[DELETE] channel id=" + channelId + " 삭제 완료");

        // 8) 삭제 후 ReadStatus 잔여 확인
        List<?> leftByUser = readStatusService.findAllByUserId(userId); // List<ReadStatus>
        boolean hasLeftForDeletedChannel =
                leftByUser.stream().anyMatch(r -> {
                    // 엔티티 타입 캐스팅 없이, 리플렉션 대신 람다에서 메서드 레퍼런스를 못 쓰므로 안전하게 처리
                    // 하지만 우리의 ReadStatus 엔티티에 getChannelId() 가 있다고 전제
                    try {
                        var m = r.getClass().getMethod("getChannelId");
                        Object ch = m.invoke(r);
                        return channelId.equals(ch);
                    } catch (Exception e) {
                        return false;
                    }
                });
        System.out.println("[CHECK] leftover ReadStatus for deleted channel? " + hasLeftForDeletedChannel);

        System.out.println("\n==== DONE ====\n");
    }
}
