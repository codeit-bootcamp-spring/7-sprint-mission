package com.sprint.mission.discodeit.config;

import org.springframework.core.task.TaskDecorator;

import java.util.List;

public class CompositeDecorator implements TaskDecorator {

    private final List<TaskDecorator> decorators;

    public CompositeDecorator(List<TaskDecorator> decorators) {
        this.decorators = decorators;
    }
    @Override
    public Runnable decorate(Runnable runnable) {
        Runnable wrappedRunnable = runnable;
        for(TaskDecorator decorator : decorators){
            wrappedRunnable = decorator.decorate(wrappedRunnable);
        }
        return wrappedRunnable;
    }
}
