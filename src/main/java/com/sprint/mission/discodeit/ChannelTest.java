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


        channelService.deleteChannel(channel1);
        channelService.readChannel(channel1);
    }
}
