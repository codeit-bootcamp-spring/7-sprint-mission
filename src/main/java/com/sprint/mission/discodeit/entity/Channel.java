package com.sprint.mission.discodeit.entity;

import javax.naming.Name;
import javax.naming.NamingEnumeration;
import java.util.ArrayList;
import java.util.Objects;
import java.util.function.BiConsumer;
import java.util.function.Consumer;

public class Channel extends Entity {
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isTextChannel;

    public Channel(String name, String description, boolean isPublic, boolean isTextChannel) {
        super();
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getDescription() {
        return description;
    }

    public void setDescription(String description) {
        this.description = description;
    }

    public boolean isPublic() {
        return isPublic;
    }

    public void setPublic(boolean aPublic) {
        isPublic = aPublic;
    }

    @Override
    public String toString() {
        return "Channel{" +
                "name='" + name + '\'' +
                ", description='" + description + '\'' +
                ", isPublic=" + isPublic +
                ", isTextChannel=" + isTextChannel +
                '}';
    }

    public boolean isTextChannel() {
        return isTextChannel;
    }

    public void setTextChannel(boolean textChannel) {
        isTextChannel = textChannel;
    }
    public enum channelElement
    {
        NAME((x,y) -> x.setName( (String) y)),
        DESCRIPTION((x,y)->x.setDescription( (String) y)),
        IS_PUBLIC((x,y)->x.setPublic( (boolean) y)),
        IS_TEXT_CHANNEL((x,y)->x.setTextChannel( (boolean) y)),
        ;
       public BiConsumer<Channel, Object> setter;







        channelElement(BiConsumer<Channel,Object> setter) {
            this.setter = setter;
        }
    }
}
