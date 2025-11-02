package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.entity.binaryContent.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.binaryContent.file.*;
import com.sprint.mission.discodeit.entity.binaryContent.jcf.*;
import com.sprint.mission.discodeit.entity.status.repository.*;
import com.sprint.mission.discodeit.entity.status.repository.file.*;
import com.sprint.mission.discodeit.entity.status.repository.jcf.*;
import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Value("${discodeit.repository.file-directory}")
    private String BASE_PATH;

    // User
    @Bean
    // 이 메서드의 리턴개체는 조건부로 빈 등록이 된다.
    @ConditionalOnProperty(
            prefix = "discodeit.repository",    // 접두어
            name = "type",  // 참조해야 할 속성 키값
            havingValue = "jcf",    // 이 값일 때 해당 객체를 빈 등록
            matchIfMissing = true   // 만약 값이 없다면 이 객채로 빈 등록
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
        return new FileUserRepository(BASE_PATH);
    }

    // Channel
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
    public ChannelRepository fileChannelRepository() {
        return new FileChannelRepository(BASE_PATH);
    }

    // Message
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
    public MessageRepository fileMessageRepository() {
        return new FileMessageRepository(BASE_PATH);
    }

    // ReadStatus
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
    public ReadStatusRepository fileReadStatusRepository() {
        return new FileReadStatusRepository(BASE_PATH);
    }

    // UserStatus
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
    public UserStatusRepository fileUserStatusRepository() {
        return new FileUserStatusRepository(BASE_PATH);
    }

    // BinaryContent
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
    public BinaryContentRepository fileBinaryContentRepository() {
        return new FileBinaryContentRepository(BASE_PATH);
    }
}
