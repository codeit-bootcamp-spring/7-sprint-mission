package com.sprint.mission.discodeit.global.config.async;

import com.sprint.mission.discodeit.global.async.decorator.MdcTaskDecorator;
import com.sprint.mission.discodeit.global.async.decorator.SecurityContextTaskDecorator;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.TaskExecutor;
import org.springframework.core.task.support.CompositeTaskDecorator;
import org.springframework.scheduling.annotation.EnableAsync;
import org.springframework.scheduling.concurrent.ThreadPoolTaskExecutor;

import java.util.List;

@Configuration
@EnableAsync
@RequiredArgsConstructor
public class AsyncConfig {


    @Bean(name = "eventExecutor")
    public TaskExecutor eventExecutor(TaskDecorator taskDecorator) {
        ThreadPoolTaskExecutor executor = new ThreadPoolTaskExecutor();
        executor.setCorePoolSize(10); // 기본 스레드 수
        executor.setMaxPoolSize(20); // 최대 스레드 수
        executor.setQueueCapacity(100); // 큐 크기
        executor.setThreadNamePrefix("eventTask-"); // 스레드 이름
        executor.setTaskDecorator(taskDecorator);
        executor.initialize();
        return executor;
    }

    @Bean
    public TaskDecorator taskDecorator(
            MdcTaskDecorator mdcTaskDecorator,
            SecurityContextTaskDecorator securityContextTaskDecorator) {

        return new CompositeTaskDecorator(List.of(mdcTaskDecorator, securityContextTaskDecorator));
    }
}
