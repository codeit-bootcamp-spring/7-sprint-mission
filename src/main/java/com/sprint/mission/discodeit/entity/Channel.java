package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.static_.StaticString;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;
import java.util.function.BiConsumer;
import java.util.function.Consumer;
import java.util.function.Function;

public class Channel extends Entity {
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isTextChannel;
    private final List<User> userDb = new ArrayList<>();

    public void addUserToChannel(User user){
        userDb.add(user);
    }

    public List<User> getUserDb() {
        return userDb;
    }

    public void removeUserFromChannel(User user){

        if(userDb.contains(user))
        {
            userDb.remove(user);
            return;
        }

            throw new IllegalArgumentException(StaticString.CHANNEL_NOT_EXIST);

    }

    public Channel(String name, String description, boolean isPublic, boolean isTextChannel) {
        super();
        this.name = name;
        this.description = description;
        this.isPublic = isPublic;
        this.isTextChannel = isTextChannel;
    }

    public Channel(UUID id, String name, String description, boolean isPublic, boolean isTextChannel) {
        super(id);
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

    public List<User> getUsers() {
        return userDb;
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
                ", userDb=" + showUser(this) +
                '}';
    }

    public boolean isTextChannel() {
        return isTextChannel;
    }

    public String showUser(Channel channel){
        StringBuilder out = new StringBuilder();
        for(User user : channel.getUserDb()){
            out.append(user.getName()).append("\n");
        }
        return out.toString();
    }

    public void setTextChannel(boolean textChannel) {
        isTextChannel = textChannel;
    }
    public enum channelElement
    {
        NAME(Channel::getName,(x, y) -> x.setName( (String) y)),
        DESCRIPTION(Channel::getDescription,(x,y)->x.setDescription( (String) y)),
        IS_PUBLIC(Channel::isPublic,(x,y)->x.setPublic( (boolean) y)),
        IS_TEXT_CHANNEL(Channel::isTextChannel,(x,y)->x.setTextChannel( (boolean) y)),
        ;
       public BiConsumer<Channel, Object> setter;



       public Function<Channel,Object> getter;


        channelElement(Function<Channel,Object> getter,BiConsumer<Channel,Object> setter) {
            this.getter = getter;
            this.setter = setter;
        }
    }
}
