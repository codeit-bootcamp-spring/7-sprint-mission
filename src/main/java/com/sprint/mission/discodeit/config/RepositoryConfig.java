package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Configuration
public class RepositoryConfig {

    @Bean
    //이 메서드의 리턴 객체는 조건부로 빈 등록이 된다
    //안에 속성에따라 하겠다
    @ConditionalOnProperty(
           prefix = "discodeit.repository",
           name = "type",
           havingValue = "jcf",
           matchIfMissing = true
    )
    public UserRepository jcfUserRepository(){
        return new JCFUserRepository();
    }

    @Bean
    //이 메서드의 리턴 객체는 조건부로 빈 등록이 된다
    //안에 속성에따라 하겠다
    @ConditionalOnProperty(
            prefix = "discodeit.repository",
            name = "type",
            havingValue = "file"

    )
    public UserRepository fileUserRepository(){
        return new JCFUserRepository();
    }

}
