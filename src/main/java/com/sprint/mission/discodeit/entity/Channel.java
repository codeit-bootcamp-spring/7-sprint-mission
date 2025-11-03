package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import com.sprint.mission.discodeit.exception.InvalidInputException;
import lombok.Getter;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter
@ToString
public class Channel extends BaseEntity {

    private String channelName;
    private final ChannelType type;    // 채널타입(public, private)
    private final User channelAdmin;
    private final List<User> members;

    // 피드백을 통한 수정
    private String SetChannelName(User user, String channelName, ChannelType type) {
        if (type == ChannelType.PRIVATE)
            return user.getUserName() + "의 비공개 채널";

        else {
            if (channelName == null || channelName.isBlank())
                return user.getUserName() + "의 채널";
            else return channelName;
        }
    }

    public Channel(User user, String channelName, ChannelType type) {
        super();

        this.channelAdmin = user;
        this.type = type;
        this.channelName = SetChannelName(user, channelName, type);
        this.members = new ArrayList<>();
        members.add(user);
    }

    // updateMessage (private는 수정불가)
    public void changeChannelName(String channelName) {
        if (channelName == null || channelName.isBlank()) {
            throw new InvalidInputException("잘못된 입력");
        }
        this.channelName = channelName;
        updateTimestamp();
    }

    public boolean addMember(User user) {
        if (!members.contains(user)) {
            members.add(user);
            updateTimestamp();
            return true;
        }
        return false;
    }

    public boolean removeMember(User user) {
        if (members.contains(user)) {
            members.remove(user);
            updateTimestamp();
            return true;
        }
        return false;
    }

/* 현재 코드가 '누가' '어떤 채널'의 정보를 바꾼다는 것이 아니라 미구현
    public boolean isAdmin(User user) {
        return this.channelAdmin.getId().equals(user.getId());
    }
*/
}
