package com.sprint.mission.discodeit.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import lombok.RequiredArgsConstructor;
import org.springframework.retry.annotation.EnableRetry;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@EnableRetry
@RequiredArgsConstructor
public class AsyncConfig {

    private final ContextCopyingTaskDecorator contextCopyingTaskDecorator;

    @Bean(name = "applicationTaskExecutor")
    public Executor applicationTaskExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);          // 기본 유지 스레드 수
        executor.setMaxPoolSize(20);          // 최대 스레드 수
        executor.setQueueCapacity(100);       // 대기 큐 크기
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("async-");
        executor.setTaskDecorator(contextCopyingTaskDecorator);
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());
        executor.initialize();
        return executor;
    }
}
