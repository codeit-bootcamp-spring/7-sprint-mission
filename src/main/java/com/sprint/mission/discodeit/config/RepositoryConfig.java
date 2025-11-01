package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    @Value("${discodeit.repository.file-directory}\\user.sav")
    private String userPath;

    @Value("${discodeit.repository.file-directory}\\channel.sav")
    private String channelPath;

    @Value("${discodeit.repository.file-directory}\\joined.sav")
    private String joinedPath;

    @Value("${discodeit.repository.file-directory}\\message.sav")
    private String messagePath;

    @Value("${discodeit.repository.file-directory}\\readstatus.sav")
    private String readstatusPath;

    @Value("${discodeit.repository.file-directory}\\userstatus.sav")
    private String userstatusPath;

    @Value("${discodeit.repository.file-directory}\\content.sav")
    private String binarycontentPath;


    // UserRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야할 속성 키 값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public UserRepository jcfUserRepository() {
        return new JCFUserRepository();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public UserRepository fileUserRepository() {
        return new FileUserRepository(userPath);
    }


    // ChannelRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야할 속성 키 값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public ChannelRepository jcfChannelRepository() {
        return new JCFChannelRepository();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public ChannelRepository fileChannelRepository() {
        return new FileChannelRepository(channelPath, joinedPath);
    }

    // MessageRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야할 속성 키 값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public MessageRepository jcfMessageRepository() {
        return new JCFMessageRepository();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public MessageRepository fileMessageRepository() {
        return new FileMessageRepository(messagePath);
    }

    // ReadStatusRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야할 속성 키 값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public ReadStatusRepository jcfReadStatusRepository() {
        return new JCFReadStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public ReadStatusRepository fileReadStatusRepository() {
        return new FileReadStatusRepository(readstatusPath);
    }

    // UserStatusRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야할 속성 키 값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public UserStatusRepository jcfUserStatusRepository() {
        return new JCFUserStatusRepository();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public UserStatusRepository fileUserStatusRepository() {
        return new FileUserStatusRepository(userstatusPath);
    }

    // BinaryContentRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야할 속성 키 값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public BinaryContentRepository jcfBinaryContentRepository() {
        return new JCFBinaryContentRepository();
    }

    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public BinaryContentRepository fileBinaryContentRepository() { return new FileBinaryContentRepository(binarycontentPath); }
}
