package com.sprint.mission.discodeit.entityElement;

import com.sprint.mission.discodeit.entity.Message;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum MessageElement {
    CONTENT(Message::getContent,(x, y) -> x.setContent( (String) y)),
    IS_MARKDOWN(Message::isMarkDown,(x,y)->x.setMarkDown( (boolean) y));

    public final BiConsumer<Message, Object> setter;
    public final Function<Message,Object> getter;

    MessageElement(   Function<Message,Object>  getter,BiConsumer<Message, Object> setter)
    {   this.getter = getter;
        this.setter = setter;
    }
}
