package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {
    //UserRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "jcf",
            matchIfMissing = true
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
    public UserRepository fileUserRepository(RepositoryProperties repositoryProperties) {
        return new FileUserRepository(repositoryProperties);
    }

    //UserStatusRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "jcf",
            matchIfMissing = true
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
    public UserStatusRepository fileUserStatusRepository(RepositoryProperties repositoryProperties) {
        return new FileUserStatusRepository(repositoryProperties);
    }

    //BinaryContentRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "jcf",
            matchIfMissing = true
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
    public BinaryContentRepository fileBinaryContentRepository(RepositoryProperties repositoryProperties) {
        return new FileBinaryContentRepository(repositoryProperties);
    }

    //ChannelRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "jcf",
            matchIfMissing = true
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
    public ChannelRepository fileChannelRepository(RepositoryProperties repositoryProperties) {
        return new FileChannelRepository(repositoryProperties);
    }

    //MessageRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "jcf",
            matchIfMissing = true
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
    public MessageRepository fileMessageRepository(RepositoryProperties repositoryProperties) {
        return new FileMessageRepository(repositoryProperties);
    }

    //ReadStatusRepository
    @Bean
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "jcf",
            matchIfMissing = true
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
    public ReadStatusRepository fileReadStatusRepository(RepositoryProperties repositoryProperties) {
        return new FileReadStatusRepository(repositoryProperties);
    }
}
