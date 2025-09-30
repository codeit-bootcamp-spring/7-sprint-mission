package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;
import com.sprint.mission.discodeit.service.jcf.JCFDb;
import com.sprint.mission.discodeit.service.jcf.JCFMessage;

public class ChannelTest {

    public static void main(String[] args) {
        JCFDb jcfDb = new JCFDb();
        JCFChannel channelService = new JCFChannel(jcfDb);
        Channel channel1 = new Channel("JAVA","JAVA 안전자산 놀이터",true,true);
        Channel channel2 = new Channel("Git","Git fork 선봉자의 모임",false,false);
        Channel channel3 = new Channel("던파", "던악귀의 모임", true, false);

        channelService.createChannel(channel1);
        channelService.createChannel(channel2);

        channelService.updateChannel(channel1, Channel.channelElement.NAME, 1 );
        channelService.updateChannel(channel2, Channel.channelElement.IS_PUBLIC, true);

        channelService.readUpdatedChannel();
    }
}
