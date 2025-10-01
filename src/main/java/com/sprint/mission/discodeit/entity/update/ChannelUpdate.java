package com.sprint.mission.discodeit.entity.update;

import com.sprint.mission.discodeit.entity.User;

public class ChannelUpdate {

    private String channelName;
    private User memberName;

    public ChannelUpdate(){}

    public String getChannelName() {
        return channelName;
    }

    public void updateChannelName(String channelName) {
        this.channelName = channelName;
    }

    public User getMemberName() {
        return memberName;
    }

    public void updateMemberName(User memberName) {
        this.memberName = memberName;
    }
}
