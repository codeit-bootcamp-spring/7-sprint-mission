package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    // ✅ 모든 Repository를 JCF로 통일 (원하면 File*로 교체 가능)
    @Bean public UserRepository userRepository() { return new JCFUserRepository(); }
    @Bean public UserStatusRepository userStatusRepository() { return new JCFUserStatusRepository(); }
    @Bean public ChannelRepository channelRepository() { return new JCFChannelRepository(); }
    @Bean public MessageRepository messageRepository() { return new JCFMessageRepository(); }
    @Bean public ReadStatusRepository readStatusRepository() { return new JCFReadStatusRepository(); }
    @Bean public BinaryContentRepository binaryContentRepository() { return new JCFBinaryContentRepository(); }
}
