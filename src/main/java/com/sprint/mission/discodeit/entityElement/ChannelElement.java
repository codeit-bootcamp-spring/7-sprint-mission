package com.sprint.mission.discodeit.entityElement;

import com.sprint.mission.discodeit.entity.Channel;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum ChannelElement {
    NAME(Channel::getName, (x, y) -> x.setName((String) y)),
    DESCRIPTION(Channel::getDescription, (x, y) -> x.setDescription((String) y)),
    IS_PUBLIC(Channel::isPublic, (x, y) -> x.setPublic((boolean) y)),
    IS_TEXT_CHANNEL(Channel::isTextChannel, (x, y) -> x.setTextChannel((boolean) y)),
    ;
    public BiConsumer<Channel, Object> setter;


    public Function<Channel, Object> getter;


    ChannelElement(Function<Channel, Object> getter, BiConsumer<Channel, Object> setter) {
        this.getter = getter;
        this.setter = setter;
    }
}

