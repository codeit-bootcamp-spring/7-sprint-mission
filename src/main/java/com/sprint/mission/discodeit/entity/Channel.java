package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.entity.dto.Dto_CreateChannelPublic;
import lombok.Getter;

import java.time.Instant;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Getter
public class Channel extends BaseModel {
//channelService -        [ ] PRIVATE 채널인 경우 참여한 User의 readStatusID 정보를 포함합니다.
    private ChannelType channelType;
    private String channelName;
    private String description;
//    private List<UUID> participantIds; // PRIVATE 일 경우 사용

    private Instant lastMessageAt; // [ ] 해당 채널의 가장 최근 메시지의 시간 정보를 포함합니다.

    public Channel(Dto_CreateChannelPublic dtoCreateChannel) {
        super();
        this.channelType = PUBLIC;
        this.channelName = dtoCreateChannel.name();
        this.description = dtoCreateChannel.description();
//        this.participantIds = new ArrayList<>();
        this.lastMessageAt = null;
    }

    public Channel(Dto_CreateChannelPrivate dtoCreateChannel) {
        super();
        this.channelType = PRIVATE;
        this.channelName = "";
        this.description = "";
//        this.participantIds = dtoCreateChannel.participantIds();
        this.lastMessageAt = null;
    }

    @Override
    public String toString() {
        return "Channel {" +
                super.toString() +
                "\n name = [" + channelName +
                "\n newDescription = [" + description + "]"   +
//                "]\n participantIds = [" + participantIds + "]" +
                "]\n lastMessageAt = [" + lastMessageAt + "]" +
                "]\n channelType = [" + channelType + "] }";
    }

    public void updateChannelType(ChannelType type) {
        this.channelType = type;
        super.setUpdatedAtNow();
    }

    public void update(ChannelDto_Update dtoChannelUpdate) {
        this.channelName = dtoChannelUpdate.newName();
        this.description = dtoChannelUpdate.newDescription();
    }

//    public void addUser(UUID userID) {
//        this.participantIds.add(userID);
//    }

    public void setLastMessageAt(Instant lastMessageAt) {
        this.lastMessageAt = lastMessageAt;
    }
}
