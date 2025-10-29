package com.sprint.mission.discodeit;

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

        // 서비스 초기화
        UserService userService;
        ChannelService channelService;
        MessageService messageService;

        // 할당
        userService = context.getBean(UserService.class);
        channelService = context.getBean(ChannelService.class);
        messageService = context.getBean(MessageService.class);
	}

}
