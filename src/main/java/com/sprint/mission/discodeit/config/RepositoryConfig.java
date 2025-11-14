package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import lombok.RequiredArgsConstructor;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

// 설정용 클래스
@Configuration
@RequiredArgsConstructor
public class RepositoryConfig {

  @Value("${discodeit.repository.file-directory}")
  private String fileDirectory;

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
      havingValue = "jcf",
      matchIfMissing = true
  )
  public JCFReadStatusRepository jcfReadStatusRepository() {
    return new JCFReadStatusRepository();
  }


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
      havingValue = "file",
      matchIfMissing = false
  )
  public UserRepository fileUserRepository() {
    return new FileUserRepository(fileDirectory);
  }

  @Bean
  @ConditionalOnProperty(
      prefix = "discodeit.repository",
      name = "type",
      havingValue = "file",
      matchIfMissing = false
  )
  public ChannelRepository fileChannelRepository() {
    return new FileChannelRepository(fileDirectory);
  }


  @Bean
  @ConditionalOnProperty(
      prefix = "discodeit.repository",
      name = "type",
      havingValue = "file",
      matchIfMissing = false
  )
  public MessageRepository fileMessageRepository() {
    return new FileMessageRepository(fileDirectory);
  }


  @Bean
  @ConditionalOnProperty(
      prefix = "discodeit.repository",
      name = "type",
      havingValue = "file",
      matchIfMissing = false
  )
  public UserStatusRepository fileUserStatusRepository() {
    return new FileUserStatusRepository(fileDirectory);
  }


  @Bean
  @ConditionalOnProperty(
      prefix = "discodeit.repository",
      name = "type",
      havingValue = "file",
      matchIfMissing = false
  )
  public ReadStatusRepository fileReadStatusRepository() {
    return new FileReadStatusRepository(fileDirectory);
  }


  @Bean
  @ConditionalOnProperty(
      prefix = "discodeit.repository",
      name = "type",
      havingValue = "file",
      matchIfMissing = false
  )
  public BinaryContentRepository fileBinaryContentRepository() {
    return new FileBinaryContentRepository(fileDirectory);
  }
}
