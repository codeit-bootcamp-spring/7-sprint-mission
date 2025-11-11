package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.enums.ChannelType;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.ToString;

import java.io.Serial;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;


/**
 * 채널 생성 시 Private, Public을 나눠서 구현합니다.
 * Channel 엔티티에서 만든 participants의 경우, 제가 임의로 만든 멤버 필드입니다.
 * ChannelService에서 createChannel 메소드 로직에는 private인 경우에만 "참여하는 user의 정보를 받아 ReadStatus 정보를 생성한다."라고 정의되어 있습니다.
 * 따라서 participants 멤버 필드는 채널 생성 시, private인 경우 user을 바로 등록하도록 하고 (addParticipant 메소드)
 * public인 경우에는 메세지 생성 시에만 user를 participant List에 저장하도록 구현하였습니다.
 *
 * 즉, public, private 모두 participant 리스트에 참여 유저를 저장하는건 맞으나,
 * private인 경우 채널 생성 시에도 참여 유저들을 등록하도록 설계하였습니다.
 */
@Getter
@ToString(callSuper = true)
@EqualsAndHashCode(callSuper = true)
public class Channel extends Common {
    @Serial
    private static final long serialVersionUID = 1L;
    private Instant updateAt;

    private ChannelType channelType;
    private String channelName;
    private String desc;

    @ToString.Exclude
    private final List<User> participants;

    public Channel(ChannelType channelType, String channelName, String desc) {
        this.updateAt = Instant.now();
        this.channelType = channelType;
        this.channelName = channelName;
        this.desc = desc;
        participants = new ArrayList<>();
    }

    public void updateChannel(String channelName, String desc) {
        boolean isUpdate = false;

        if(channelName != null && !this.channelName.equals(channelName)) {
            this.channelName = channelName;
            isUpdate = true;
        }
        if(desc != null && !this.desc.equals(desc)) {
            this.desc = desc;
            isUpdate = true;
        }

        if(isUpdate) updateAt = Instant.now();
    }

    // JPA로 데이터를 제어한다면, 이런 메서드가 필요할까
    // CASCADE 옵션으로 참조 무결성을 보장할 수 있는데 흠...
    public void addParticipant(User user) {
        if (user != null && !participants.contains(user)) {
            participants.add(user);
            updateAt = Instant.now();
        }
    }

    public void removeParticipant(User user) {
        if (user != null && participants.contains(user)) {
            participants.remove(user);
            updateAt = Instant.now();
        }
    }

}
