package com.sprint.mission.discodeit;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;
import org.springframework.context.ConfigurableApplicationContext;
import org.springframework.core.io.ClassPathResource;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Base64;

@SpringBootApplication
public class DiscodeitApplication {
	public static void main(String[] args) {
        ConfigurableApplicationContext ctx = SpringApplication.run(DiscodeitApplication.class, args);

//        ClassPathResource resource = new ClassPathResource("static/춘식이.jpg");
//        byte[] bytes = null;
//        try {
//            bytes = Files.readAllBytes(resource.getFile().toPath());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//
//        String base64 = Base64.getEncoder().encodeToString(bytes);
//        System.out.println("base64 = " + base64);
/*
        ChannelService channelService = ctx.getBean(ChannelService.class);
        UserService userService = ctx.getBean(UserService.class);
        MessageService messageService = ctx.getBean(MessageService.class);r
 */
    }
}
