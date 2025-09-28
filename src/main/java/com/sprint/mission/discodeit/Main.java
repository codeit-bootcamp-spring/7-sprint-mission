package com.sprint.mission.discodeit;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.jcf.JCFChannel;

import java.util.ArrayList;

public class Main {
    public static void main(String[] args) {

        JCFChannel jcfChannel = new JCFChannel(new ArrayList<>());

        Channel test1 = new Channel("Test1","Test1 Description",false,false);
        Channel test2 = new Channel("Test2","Test2 Description",true,true);

        jcfChannel.createChannel(test1);
        jcfChannel.createChannel(test2);

        jcfChannel.readAllChannel();
        jcfChannel.updateChannel(test1, Channel.channelElement.NAME,"test1Updated");
        jcfChannel.readAllChannel();
        jcfChannel.readChannel(test1);
        System.out.println("==================");
        jcfChannel.readChannel(test1,test2);
        jcfChannel.readUpdatedChannel();
        jcfChannel.updateChannel(test2, Channel.channelElement.IS_PUBLIC,false);
    }
}
