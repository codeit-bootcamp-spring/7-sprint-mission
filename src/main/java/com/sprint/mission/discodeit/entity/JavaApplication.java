package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.service.ChannelService;
import com.sprint.mission.discodeit.entity.service.jcf.*;

public class JavaApplication {

    public static void main(String[] args) {

        JCFChannelService jcfChannelService = new JCFChannelService();
        JCFUserService jcfUserService = new JCFUserService();
       JCFMessageService jcfMessageService = new JCFMessageService();

        User user = new User("jam@ewew","123123","shinjewon","powerboy");


        Channel channel2 = new Channel(user,"바보2");

       jcfChannelService.create(user,"바보");

        jcfChannelService.read(channel2);

        jcfChannelService.readAll();

        jcfChannelService.delete(channel2);



    }
}
