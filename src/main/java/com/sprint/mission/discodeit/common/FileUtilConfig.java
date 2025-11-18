package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.entity.ModelType;
import com.sprint.mission.discodeit.repository.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration(proxyBeanMethods = false) // 👍위의 설정 클래스에 대해서는 프록시가 적용되지 않으며 모든 @Bean 메소드 호출마다 새로운 객체를 생성해준다
public class FileUtilConfig {

    @Bean
    public FileUtil userFileUtil() { return new FileUtil(ModelType.USER); }

    @Bean
    public FileUtil channelFileUtil() {
        return new FileUtil(ModelType.CHANNEL);
    }

    @Bean
    public FileUtil messageFileUtil() {
        return new FileUtil(ModelType.MESSAGE);
    }

    @Bean
    public FileUtil userStatusFileUtil()  { return new FileUtil(ModelType.USERSTATUS); }

    @Bean
    public FileUtil readStatusFileUtil()  { return new FileUtil(ModelType.READSTATUS); }

    @Bean
    public FileUtil binaryContentFileUtil()  { return new FileUtil(ModelType.BINARYCONTENT); }
}
