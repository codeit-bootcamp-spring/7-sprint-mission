package com.sprint.mission.discodeit.config;


import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig {


    @Bean(name = "notificationExecutor")
    public ThreadPoolTaskExecutor notificationExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("notification-");

        executor.setTaskDecorator(
                new CompositeDecorator(
                        List.of(
                                new LoggingTaskDecorator(),
                                new SecurityContextDecorator()
                        )
                )
        );
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        log.info("notificationExecutor 실행 시작 준비 완료");
        return executor;
    }


    @Bean(name = "binaryContentExecutor")
    public ThreadPoolTaskExecutor binaryContentExecutor(){
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("binaryContent-");

        executor.setTaskDecorator(
                new CompositeDecorator(
                        List.of(
                                new LoggingTaskDecorator(),
                                new SecurityContextDecorator()
                        )
                )
        );
        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.initialize();
        log.info("binaryContentExecutor 실행 시작 준비 완료");
        return executor;
    }
}
