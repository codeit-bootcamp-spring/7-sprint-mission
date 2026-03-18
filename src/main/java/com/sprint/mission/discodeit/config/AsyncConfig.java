package com.sprint.mission.discodeit.config;

import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.support.CompositeTaskDecorator;
import org.springframework.scheduling.annotation.AsyncConfigurer;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;
import java.util.concurrent.Executor;
import java.util.concurrent.ThreadPoolExecutor;

@Configuration
@EnableAsync
@Slf4j
public class AsyncConfig implements AsyncConfigurer {

    @Override
    public Executor getAsyncExecutor() {
        return notificationExecutor(); // @Async 만사용시 일단 messageExecutor로 사용, 추후 리펙토링 가능
    }

    @Bean(name = "eventTaskExecutor")
    public ThreadPoolTaskExecutor notificationExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("eventTaskExecutor-");

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.setTaskDecorator(
                new CompositeTaskDecorator(List.of(
                        new SecurityContextTaskDecorator(),
                        new LoggingTaskDecorator()

                ))
        );

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.DiscardOldestPolicy()); // 실패시 오래된것부터 버림

        executor.initialize();

        log.info("messageExecutor Initialized: corePoolSize={}, maxPoolSize={}, queueCapacity={}, keepAliveSeconds={}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize(),
                executor.getQueueCapacity(),
                executor.getKeepAliveSeconds()
        );

        return executor;
    }

    @Bean(name = "binaryExecutor")
    public ThreadPoolTaskExecutor binaryExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(5);
        executor.setMaxPoolSize(10);
        executor.setQueueCapacity(200);
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("binaryExecutor-");

        executor.setTaskDecorator(
                new CompositeTaskDecorator(List.of(
                        new SecurityContextTaskDecorator(),
                        new LoggingTaskDecorator()
                ))
        );

        // 업로드는 버리면 데이터 유실 느낌이라 보통 CallerRuns/Abort 중 선택
        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy());

        executor.initialize();
        return executor;
    }

    // TODO: 실패 예외처리

}
