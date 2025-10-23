package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.file.FileManager;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.nio.file.Path;

@Configuration
public class FileConfig {

    @Bean
    public FileManager<User> userFileManager() {
        return new FileManager<>(Path.of("src/main/resources/users.ser"));
    }

    @Bean
    public FileManager<Message> messageFileManager() {
        return new FileManager<>(Path.of("src/main/resources/messages.ser"));
    }

    @Bean
    public FileManager<Channel> channelFileManager() {
        return new FileManager<>(Path.of("src/main/resources/channels.ser"));
    }
}
