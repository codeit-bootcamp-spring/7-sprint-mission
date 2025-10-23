package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.MessageService;
import com.sprint.mission.discodeit.service.UserService;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {

    public static void main(String[] args) {
        ConfigurableApplicationContext context = SpringApplication.run(DiscodeitApplication.class, args);

        // ✅ Spring Context에서 Bean 조회
        UserService userService = context.getBean(UserService.class);
        ChannelService channelService = context.getBean(ChannelService.class);
        MessageService messageService = context.getBean(MessageService.class);

        // ✅ 아래 코드는 JavaApplication에서 복사해올 예정
        //    (setupUser, setupChannel, messageCreateTest 등)
        User user = setupUser(userService);
        Channel channel = setupChannel(channelService);
        messageCreateTest(messageService, channel, user);
    }

    // 아래 3개 메서드는 JavaApplication에서 복사해 넣어야 함
    private static User setupUser(UserService userService) {
        // TODO: JavaApplication에서 복사
        return null;
    }

    private static Channel setupChannel(ChannelService channelService) {
        // TODO: JavaApplication에서 복사
        return null;
    }

    private static void messageCreateTest(MessageService messageService, Channel channel, User user) {
        // TODO: JavaApplication에서 복사
    }
}
