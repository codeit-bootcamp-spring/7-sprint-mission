package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);
/*
        ChannelService channelService = ctx.getBean(ChannelService.class);
        UserService userService = ctx.getBean(UserService.class);
        MessageService messageService = ctx.getBean(MessageService.class);r
 */
    }
}
