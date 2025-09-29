package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;

public class ChannelTest {

    public static void main(String[] args) {
        JCFChannel channelService = new JCFChannel();
        Channel channel1 = new Channel("JAVA","JAVA 안전자산 놀이터",true,true);
        Channel channel2 = new Channel("Git","Git fork 선봉자의 모임",false,false);
        Channel channel3 = new Channel("던파", "던악귀의 모임", true, false);

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);
        channelService.createChannel(channel3);

        channelService.readAllChannel();
        channelService.deleteChannel(channel1);
        channelService.readDeletedChannel();

        channelService.updateChannel(channel3, Channel.channelElement.DESCRIPTION,false);
        channelService.readUpdatedChannel();
        channelService.updateChannel(channel3, Channel.channelElement.IS_PUBLIC,true);
        channelService.readAllChannel();
    }
}
