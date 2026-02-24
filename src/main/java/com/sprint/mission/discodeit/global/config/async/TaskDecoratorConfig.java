package com.sprint.mission.discodeit.global.config.async;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.task.TaskDecorator;
import org.springframework.core.task.support.CompositeTaskDecorator;

import java.util.List;

@Configuration
public class TaskDecoratorConfig {

    @Bean
    public TaskDecorator taskDecorator(List<TaskDecorator> decorators) {
        return new CompositeTaskDecorator(decorators);
    }
}
