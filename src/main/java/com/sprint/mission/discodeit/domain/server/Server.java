package com.sprint.mission.discodeit.domain.server;



import com.sprint.mission.discodeit.domain.channel.Channel;
import com.sprint.mission.discodeit.domain.user.User;
import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

//서버
@Getter
public class Server implements Serializable {

    private static final long serialVersionUID = 2L;

    private final UUID id;
    private final Instant createdAt;
    private Instant updatedAt;

    private String serverName;
    private Long serverLevel;
    private boolean isPrivate; //true -> private
    private final List<UUID> members;
    private final List<UUID> channels = new ArrayList<>();

    public Server(String serverName, boolean isPrivate,Long serverLevel, List<UUID> members) {
        validateServerName(serverName);
        validateServerLevel(serverLevel);
        validateMembersNumber(members);
        this.id = UUID.randomUUID();
        this.createdAt = Instant.now();
        this.serverName=serverName;
        this.serverLevel=serverLevel;
        this.isPrivate=isPrivate;
        this.members=members;
    }

    private static void validateServerName(String serverName){
        if (serverName == null || serverName.length()<1){
            throw new IllegalArgumentException("서버의 이름을 입력하세요");
        }
    }

    private static void validateServerLevel(Long level){
        if (level<0 || level>3){
            throw new IllegalArgumentException("서버의 레벨을 1-3이어야 합니다.");
        }
    }

    private static void validateMembersNumber(List<UUID> members){
        if (members.size()<0){
            throw new IllegalArgumentException("서버에는 최소 한 명의 멤버가 있어야 합니다");
        }
    }


    public void updateServerName(String newServerName){
        validateServerName(newServerName);
        this.updatedAt=Instant.now();
        this.serverName=newServerName;
    }


    public void updateServeLevel(Long newServerLevel){
        validateServerLevel(newServerLevel);
        this.updatedAt=Instant.now();
        this.serverLevel=newServerLevel;
    }

    public void updatePrivate(boolean b){
        this.updatedAt=Instant.now();
        this.isPrivate=b;
    }

    public List<UUID> getMembers() {
        return List.copyOf(members);
    }

    public void addMember(User user){
        if (members.contains(user.getId())){
            throw new IllegalArgumentException("이미 존재하는 멤버입니다.");
        }
        members.add(user.getId());
        updatedAt=Instant.now();
    }
    public void removeMember(User user){
        if (!members.contains(user.getId())){
            throw new IllegalArgumentException("존재하지 않는 멤버입니다");
        }
        members.remove(user.getId());
        updatedAt=Instant.now();
    }

    public List<UUID> getChannel() {
        return List.copyOf(channels);
    }

    public void makeChannel(Channel channel){
        channels.add(channel.getId());
        updatedAt=Instant.now();
    }


}
