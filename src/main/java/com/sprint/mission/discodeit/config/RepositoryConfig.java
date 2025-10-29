//package com.sprint.mission.discodeit.config;
//
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnNotWarDeployment;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//@Configuration
//public class RepositoryConfig {
//
//    @Bean
//    // 이 메서드의 리턴개체는 조건부로 빈 등록이 된다.
//    @ConditionalOnProperty(
//            prefix = "discodeit.repository",    // 접두어
//            name = "type",  // 참조해야 할 속성 키값
//            havingValue = "jcf",    // 이 값일 때 해당 객체를 빈 등록
//            matchIfMissing = true   // 만약 값이 없다면 이 객채로 빈 등록
//    )
//    public UserRepository jcfUserRepository() {
//        return new JCFUserRepository();
//    }
//
//    @Bean
//    // 이 메서드의 리턴개체는 조건부로 빈 등록이 된다.
//    @ConditionalOnProperty(
//            prefix = "discodeit.repository",
//            name = "type",
//            havingValue = "file"
//    )
//    public UserRepository fielUserRepository() {
//        return new FileUserRepository();
//    }
//}
