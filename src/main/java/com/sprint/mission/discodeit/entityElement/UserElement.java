package com.sprint.mission.discodeit.entityElement;

import com.sprint.mission.discodeit.entity.User;

import java.util.function.BiConsumer;
import java.util.function.Function;

public enum UserElement {
    NAME(User::getName,(x, y) -> x.setName( (String) y)),
    NICKNAME(User::getUserName,(x, y)->x.setUserName( (String) y)),
    EMAIL(User::getEmail,(x,y)->x.setEmail( (String) y)),
    ONLINE(User::isOnline,(x,y)->x.setOnline( (boolean) y));

    public final BiConsumer<User, Object> setter;
    public final Function<User,Object> getter;

    UserElement(Function<User,Object> getter, BiConsumer<User, Object> setter)
    {
        this.getter = getter;
        this.setter = setter;
    }

}
