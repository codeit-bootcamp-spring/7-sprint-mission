package com.sprint.mission.discodeit.service.input;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.jcf.JCFDb;

public class InputUtil {
    private final JCFDb jcfDb;

    public InputUtil(JCFDb jcfDb) {
        this.jcfDb = jcfDb;
    }
    public boolean checkValidateBoolean(String input) {

        if (input == null) return false;
        String inputLowerCase = input.toLowerCase();
        return inputLowerCase.equals("true") || inputLowerCase.equals("false");
    }

    public Channel targetChannel(String channelName) {

        return jcfDb.getChannelDb()
                .stream()
                .filter(m -> m.getName().equals(channelName)).findFirst().orElse(null);
    }

    public User targetUser(String userName) {
        return jcfDb.getUserDb()
                .stream()
                .filter(m -> m.getName().equals(userName)).findFirst().orElse(null);
    }

    public Message targetMessage(String messageContent) {
        return jcfDb.getMessageDb()
                .stream()
                .filter(m -> m.getContent().equals(messageContent)).findFirst().orElse(null);
    }
}
