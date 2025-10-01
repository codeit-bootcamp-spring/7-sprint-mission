package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.static_.StaticString;

import java.util.ArrayList;
import java.util.function.BiConsumer;

public class User extends Entity {


    private String name;
    private String nickname;
    private String email;
    private boolean isOnline;
    private final ArrayList<Channel> channelDb = new ArrayList<>();

    public ArrayList<Channel> getChannelDb() {
        return channelDb;
    }
    public void addChannel(Channel channel){
        if(channelDb.contains(channel)) {
            throw new IllegalArgumentException(StaticString.CHANNEL_EXIST);

        }
        channelDb.add(channel);

    }
    public void removeChannel(Channel channel){
        if(!channelDb.contains(channel)) throw new IllegalArgumentException(StaticString.CHANNEL_NOT_EXIST);
        channelDb.remove(channel);
    }

    public User(String name, String nickname, String email, boolean isOnline) {
        super();
        this.name = name;
        this.nickname = nickname;
        this.email = email;
        this.isOnline = isOnline;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getNickname() {
        return nickname;
    }

    public void setNickname(String nickname) {
        this.nickname = nickname;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public boolean isOnline() {
        return isOnline;
    }

    public void setOnline(boolean online) {
        isOnline = online;
    }

    public enum userElement
    {
        NAME((x,y) -> x.setName( (String) y)),
        NICKNAME((x,y)->x.setNickname( (String) y)),
        EMAIL((x,y)->x.setEmail( (String) y)),
        ONLINE((x,y)->x.setOnline( (boolean) y));

        public BiConsumer<User, Object> setter;

        userElement(BiConsumer<User, Object> setter)
        {
            this.setter = setter;
        }

    }

    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", nickname='" + nickname + '\'' +
                ", email='" + email + '\'' +
                ", isOnline=" + isOnline +
                ", channelDb=" + showChannel(this) +
                '}';
    }

    public String showChannel(User user){
        StringBuilder out = new StringBuilder();
        for(Channel channel : user.getChannelDb()){
            out.append(channel.getName()).append("\n");
        }
        return out.toString();
    }
}
