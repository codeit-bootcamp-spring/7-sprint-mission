package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ValidateService;

import java.util.ArrayList;

public class JCFValidateOperator implements ValidateService {

    private final JCFDb jcfDb;
    private final ArrayList<User> userDb ;
    private final ArrayList<Message> messageDb ;
    private final ArrayList<Channel> channelDb ;

    public JCFValidateOperator(JCFDb jcfDb) {
        this.jcfDb = jcfDb;
        this.userDb = jcfDb.getUserDb();
        this.messageDb = jcfDb.getMessageDb();
        this.channelDb = jcfDb.getChannelDb();
    }

    public boolean isValidateUser(User user) {
        if (user == null) return false;
        return userDb.stream().anyMatch(x -> x.getId() == user.getId());

    }

    public boolean isValidateMessage( Message message) {
        if (message == null) return false;
        return messageDb.stream().anyMatch(x -> x.getId() == message.getId());

    }

    public boolean isValidateChannel( Channel channel) {
        if (channel == null) return false;
        return channelDb.stream().anyMatch(x -> x.getId() == channel.getId());

    }


}
