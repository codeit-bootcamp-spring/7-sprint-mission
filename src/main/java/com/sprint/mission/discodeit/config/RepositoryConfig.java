package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.*;
import com.sprint.mission.discodeit.repository.file.*;
import com.sprint.mission.discodeit.repository.jcf.*;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration(proxyBeanMethods = false)
public class RepositoryConfig {
    @Configuration(proxyBeanMethods = false)
    // 이 메서드의 리턴 객체는 조건부로 빈 등록이 된다.
    @ConditionalOnProperty(
            prefix = "discodeit.repository", // 접두어
            name = "type", // 참조해야 할 속성 키값
            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록 해라
            matchIfMissing = true // 만약 값이 없다면 이 빈을 기본으로 등록
    )
    public static class JCFRepositoryConfig {
        @Bean
        public UserRepository jcfUserRepository() {
            return new JCFUserRepository();
        }

        @Bean
        public ChannelRepository jcfChannelRepository() {
            return new JCFChannelRepository();
        }

        @Bean
        public BinaryContentRepository jcfBinaryContentRepository() {
            return new JCFBinaryContentRepository();
        }

        @Bean
        public MessageRepository jcfMessageRepository() {
            return new JCFMessageRepository();
        }

        @Bean
        public ReadStatusRepository jcfReadStatusRepository() {
            return new JCFReadStatusRepository();
        }

        @Bean
        public UserStatusRepository jcfUserStatusRepository() {
            return new JCFUserStatusRepository();
        }
    }

    @Configuration(proxyBeanMethods = false)
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"
    )
    public static class FileRepositoryConfig {
        //        private final String dir;
//        public FileRepositoriesConfig(
//                @Value("${discodeit.repository.file-directory:./data}") String dir) {
//            this.dir = dir;
//        }
//    }
        @Bean
        public UserRepository fileUserRepository() {
            return new FileUserRepository();
        }

        @Bean
        public ChannelRepository fileChannelRepository() {
            return new FileChannelRepository();
        }

        @Bean
        public BinaryContentRepository fileBinaryContentRepository() {
            return new FileBinaryContentRepository();
        }

        @Bean
        public MessageRepository fileMessageRepository() {
            return new FileMessageRepository();
        }

        @Bean
        public ReadStatusRepository fileReadStatusRepository() {
            return new FileReadStatusRepository();
        }

        @Bean
        public UserStatusRepository fileUserStatusRepository() {
            return new FileUserStatusRepository();
        }
    }
}
