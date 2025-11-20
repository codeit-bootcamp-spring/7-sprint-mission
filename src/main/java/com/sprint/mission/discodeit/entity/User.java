package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.util.StaticString;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;

@Builder
@Getter
@Setter
public class User extends Entity implements Serializable {


    private String name;
    private String userName;
    private String email;
    private boolean isOnline = true;
    private String password;
    private UUID profileId;

    private final HashSet<UUID> joinChannelList = new HashSet<>();

    public void addChannel(UUID channelId){
        if(joinChannelList.contains(channelId)) {
            throw new IllegalArgumentException(StaticString.CHANNEL_EXIST);

        }
        joinChannelList.add(channelId);

    }
    public void removeChannel(UUID channelId){
        if(!joinChannelList.contains(channelId)) throw new IllegalArgumentException(StaticString.CHANNEL_NOT_EXIST);
        joinChannelList.remove(channelId);
    }


//    public User(String name, String userName, String email,String password) {
//        super();
//        init(name, userName,email,password);
//    }
//
//    public User(UUID id, String name, String userName, String email,String password) {
//        super(id);
//        init(name, userName,email,password);
//    }
//    public void init(String name,String nickname,String email,String password){
//        this.name = name;
//        this.userName = nickname;
//        this.email = email;
//        this.password = password;
//
//    }





    @Override
    public String toString() {
        return "User{" +
                "name='" + name + '\'' +
                ", nickname='" + userName + '\'' +
                ", email='" + email + '\'' +
                ", isOnline=" + isOnline +
                ", channelDb=" + showChannel(this) +
                '}';
    }

    public String showChannel(User user){
        StringBuilder out = new StringBuilder();
        for(UUID channelId : user.getJoinChannelList()){
            out.append(channelId).append("\n");
        }
        return out.toString();
    }
}
