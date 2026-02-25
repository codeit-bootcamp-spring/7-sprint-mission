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
        return messageExecutor(); // @Async 만사용시 일단 messageExecutor로 사용, 추후 리펙토링 가능
    }

    @Bean(name = "messageExecutor")
    public ThreadPoolTaskExecutor messageExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("messageExecutor-");

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

    @Bean(name = "roleExecutor")
    public ThreadPoolTaskExecutor roleExecutor() {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();

        executor.setCorePoolSize(10);
        executor.setMaxPoolSize(20);
        executor.setQueueCapacity(100);
        executor.setKeepAliveSeconds(60);

        executor.setThreadNamePrefix("roleExecutor-");

        executor.setWaitForTasksToCompleteOnShutdown(true);
        executor.setAwaitTerminationSeconds(60);

        executor.setTaskDecorator(
                new CompositeTaskDecorator(List.of(
                        new SecurityContextTaskDecorator(),
                        new LoggingTaskDecorator()

                ))
        );

        executor.setRejectedExecutionHandler(new ThreadPoolExecutor.CallerRunsPolicy()); // 큐/스레드가 꽉차면 호출한 스레드가 직접 실행하도록

        executor.initialize();

        log.info("roleExecutor Initialized: corePoolSize={}, maxPoolSize={}, queueCapacity={}, keepAliveSeconds={}",
                executor.getCorePoolSize(),
                executor.getMaxPoolSize(),
                executor.getQueueCapacity(),
                executor.getKeepAliveSeconds()
        );

        return executor;
    }

    // TODO: 실패 예외처리

}
