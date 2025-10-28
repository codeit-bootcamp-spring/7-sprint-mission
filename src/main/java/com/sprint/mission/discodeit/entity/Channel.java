package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.service.util.StaticString;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.Setter;

import java.io.Serializable;
import java.util.*;
import java.util.function.BiConsumer;
import java.util.function.Function;
@Getter
@Setter
@Builder
public class Channel extends Entity implements Serializable {
    private final HashSet<UUID> joinUserList;
    private final HashSet<UUID> messageIdList = new HashSet<>();
    private String name;
    private String description;
    private boolean isPublic;
    private boolean isTextChannel;



//    public Channel(String name, String description, boolean isPublic, boolean isTextChannel) {
//        super();
//        init(name, description, isPublic, isTextChannel);
//        this.joinUserList = new ArrayList<>();
//    }
//
//    public Channel(UUID id, String name, String description, boolean isPublic, boolean isTextChannel) {
//        super(id);
//        init(name, description, isPublic, isTextChannel);
//        this.joinUserList = new ArrayList<>();
//    }
//
//    public Channel(UUID id, String name, String description, boolean isPublic, boolean isTextChannel, List<User> userDb) {
//        super(id);
//        init(name, description, isPublic, isTextChannel);
//        this.joinUserList = userDb;
//    }
//
//    private void init(String name, String description, boolean isPublic, boolean isTextChannel) {
//        this.name = name;
//        this.description = description;
//        this.isPublic = isPublic;
//        this.isTextChannel = isTextChannel;
//    }

    public void addMessageToChannel(UUID messageId) {
        messageIdList.add(messageId);
    }
    public void addUserToChannel(UUID userId) {
        joinUserList.add(userId);
    }

    public void removeUserFromChannel(UUID userId) {
        if (!joinUserList.contains(userId)) {
            throw new IllegalArgumentException(StaticString.CHANNEL_NOT_EXIST);
        }
        joinUserList.remove(userId);
    }

    @Override
    public boolean equals(Object o) {
        if (!(o instanceof Channel channel)) return false;
        return isPublic == channel.isPublic && isTextChannel == channel.isTextChannel && Objects.equals(name, channel.name) && Objects.equals(description, channel.description);
    }

    @Override
    public int hashCode() {
        return Objects.hash(name, description, isPublic, isTextChannel);
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



    public String showUser(Channel channel) {
        StringBuilder out = new StringBuilder();
        for (UUID userId : channel.getJoinUserList()) {
            out.append(userId).append("\n");
        }
        return out.toString();
    }


}
