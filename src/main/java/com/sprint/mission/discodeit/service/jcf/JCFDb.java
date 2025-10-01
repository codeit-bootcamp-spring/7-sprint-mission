package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;

import java.util.*;

public class JCFDb {

    private final ArrayList<User> userDb;
    private final ArrayList<Message> messageDb;
    private final ArrayList<Channel> channelDb;

    private final Map<UUID, String> deletedUserDb;
    private final Map<UUID, String> deletedMessageDb;
    private final Map<UUID, String> deletedChannelDb;


    public JCFDb() {
        userDb = new ArrayList<>();
        messageDb = new ArrayList<>();
        channelDb = new ArrayList<>();
        deletedUserDb = new HashMap<>();
        deletedMessageDb = new HashMap<>();
        deletedChannelDb = new HashMap<>();

    }





    public ArrayList<User> getUserDb() {
        return userDb;
    }

    public ArrayList<Message> getMessageDb() {
        return messageDb;
    }

    public ArrayList<Channel> getChannelDb() {
        return channelDb;
    }

    public Map<UUID, String> getDeletedUserDb() {
        return deletedUserDb;
    }

    public Map<UUID, String> getDeletedMessageDb() {
        return deletedMessageDb;
    }

    public Map<UUID, String> getDeletedChannelDb() {
        return deletedChannelDb;
    }
}
