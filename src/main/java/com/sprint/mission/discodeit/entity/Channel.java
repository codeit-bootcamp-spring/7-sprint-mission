package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.Dto_ChannelUpdate;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPublic;
import lombok.Getter;

import java.time.Instant;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Getter
public class Channel extends BaseModel {
//channelService -        [ ] PRIVATE 채널인 경우 참여한 User의 id 정보를 포함합니다.
    private ChannelType channelType;
    private String channelName;
    private String description;
//    private List<UUID> userIDs; // PRIVATE 일 경우 사용

    private Instant lastMessageAt; // [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.

    public Channel(Dto_CreateChannelPublic dtoCreateChannel) {
        super();
        this.channelType = PUBLIC;
        this.channelName = dtoCreateChannel.channelName();
        this.description = dtoCreateChannel.description();
//        this.userIDs = new ArrayList<>();
        this.lastMessageAt = null;
    }

    public Channel(Dto_CreateChannelPrivate dtoCreateChannel) {
        super();
        this.channelType = PRIVATE;
        this.channelName = "";
        this.description = "";
//        this.userIDs = dtoCreateChannel.userIDs();
        this.lastMessageAt = null;
    }

    @Override
    public String toString() {
        return "Channel {" +
                super.toString() +
                "\n name = [" + channelName +
                "\n description = [" + description + "]"   +
//                "]\n userIDs = [" + userIDs + "]" +
                "]\n lastMessageAt = [" + lastMessageAt + "]" +
                "]\n channelType = [" + channelType + "] }";
    }

    public void updateChannelType(ChannelType type) {
        this.channelType = type;
        super.setUpdatedAt();
    }

    public void update(Dto_ChannelUpdate dtoChannelUpdate) {
        this.channelName = dtoChannelUpdate.channelName();
        this.description = dtoChannelUpdate.description();
    }

//    public void addUser(UUID userID) {
//        this.userIDs.add(userID);
//    }

    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}
