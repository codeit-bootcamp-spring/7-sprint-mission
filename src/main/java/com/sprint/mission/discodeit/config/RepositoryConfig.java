package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

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
        return new FileUserRepository();
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
        return new FileChannelRepository();
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
        return new FileMessageRepository();
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
        return new FileReadStatusRepository();
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
        return new FileUserStatusRepository();
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
    public BinaryContentRepository fileBinaryContentRepository() {
        return new FileBinaryContentRepository();
    }
}
