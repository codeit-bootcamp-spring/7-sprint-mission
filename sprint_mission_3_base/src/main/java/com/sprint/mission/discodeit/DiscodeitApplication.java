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

        UserDto created = userService.create(
                new UserCreateRequest("woody", "woody@codeit.com", "woody1234", null)
        );
        System.out.println("[CREATE] " + created.username() + ", id=" + created.id());
        UUID userId = created.id();

        var login = authService.login(new LoginRequest(created.username(), "woody1234"));
        System.out.println("[LOGIN] " + login.user().username() + " 로그인 성공");

        ChannelDto channel = channelService.createPublic(
                new CreatePublicChannelRequest("공지", "공지 채널입니다.")
        );
        System.out.println("[CHANNEL] 생성 완료, id=" + channel.id()
                + ", name=" + channel.name()
                + ", visibility=" + channel.visibility());
        UUID channelId = channel.id();

        try { Thread.sleep(200); } catch (InterruptedException ignored) {}

        MessageDto message = messageService.create(
                new MessageCreateRequest(channelId, userId, "안녕하세요 첫 메시지 입니다!", null)
        );
        System.out.println("[MESSAGE] " + message.content()
                + " (작성자: " + created.username() + ", createdAt=" + message.createdAt() + ")");

        UUID rsId = readStatusService.create(
                new ReadStatusCreateRequest(userId, channelId, Instant.now())
        );

        readStatusService.find(rsId).ifPresent(rs -> {
            System.out.println("[READ] saved lastReadAt=" + rs.getLastReadAt()
                    + " (user=" + rs.getUserId() + ", channel=" + rs.getChannelId() + ")");
        });

        var visibles = channelService.findAllByUserId(userId);
        System.out.println("[VISIBLE] count=" + visibles.size());
        for (ChannelDto dto : visibles) {
            System.out.println(" - id=" + dto.id()
                    + ", name=" + dto.name()
                    + ", visibility=" + dto.visibility()
                    + ", latest=" + dto.latest()
                    + ", participants=" + dto.participants());
        }

        channelService.delete(channelId);
        System.out.println("[DELETE] channel id=" + channelId + " 삭제 완료");


        List<?> leftByUser = readStatusService.findAllByUserId(userId); // List<ReadStatus>
        boolean hasLeftForDeletedChannel =
                leftByUser.stream().anyMatch(r -> {

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
