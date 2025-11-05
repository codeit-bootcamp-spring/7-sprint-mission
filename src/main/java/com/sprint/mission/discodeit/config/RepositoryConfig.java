//package com.sprint.mission.discodeit.config;
//
//import com.sprint.mission.discodeit.repository.UserRepository;
//import com.sprint.mission.discodeit.repository.file.FileUserRepository;
//import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
//import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
//import org.springframework.context.annotation.Bean;
//import org.springframework.context.annotation.Configuration;
//
//// 설정용 클래스
//@Configuration
//public class RepositoryConfig {
//
//    @Bean
//    // 조건부 어노테이션 설정값에 따라서 조건에 따라서 등록될 Bean
//    // 이 메서드의 리턴 객체는 조건부로 빈 등록
//    @ConditionalOnProperty(
//            prefix = "discodeit.repository", // 접두어
//            name = "type", //type(jcf | file) 이냐 타입
//            havingValue = "jcf", // 이 값일 때 해당 객체를 빈 등록
//            matchIfMissing = true // 만약에 해당 타입이 없다면, jcf가 기본 값으로 사용.
//    )
//    public UserRepository jcfUserRepository(){
//        return new JCFUserRepository();
//    }
//
//    @Bean
//    // 조건부 어노테이션 설정값에 따라서 조건에 따라서 등록될 Bean
//    // 이 메서드의 리턴 객체는 조건부로 빈 등록
//    @ConditionalOnProperty(
//            prefix = "discodeit.repository",
//            name = "type", //type(jcf | file) 이냐 타입
//            havingValue = "file",
//            matchIfMissing = false // 만약에 해당 타입이 없다면, jcf가 기본 값으로 사용.
//    )
//    public UserRepository fileUserRepository(){
//        return new FileUserRepository();
//    }
//
//    // 내가 만들지 않은 라이브러리 객체에서 가져올 때 이 방식을 사용
//}
