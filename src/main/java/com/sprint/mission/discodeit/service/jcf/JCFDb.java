package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.etc.StaticString;
import com.sprint.mission.discodeit.service.DbService;

import java.awt.List;
import java.util.*;

import static com.sprint.mission.discodeit.etc.StaticString.*;

public class JCFDb implements DbService {

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

    @Override
    public void deleteUser(User user) {
        if (!isValidateUser(user)) {
            System.out.println(USER_NOT_EXIST + user.getName());
            return;
        }
        userDb.remove(user);
        deletedUserDb.put(user.getId(), user.getName());

        channelDb.forEach(x->x.removeUserFromChannel(user));
//        channelDb.stream()
//                        .filter(x-> x.getUserDb().contains(user))
//                                .forEach(x->
//                                        new JCFChannel(this).deleteUserFromChannel(user,x)
//                                        );
        messageDb.forEach(x->x.setSender(JCFUser.DELETED_USER));

        System.out.println(DELETE_USER + user.getName());
        return;
    }

    @Override
    public void deleteMessage(Message message) {
        if(!isValidateMessage(message)){
            System.out.println(MESSAGE_NOT_EXIST + message.getContent());
            return;
        }
        messageDb.remove(message);
        deletedMessageDb.put(message.getId(),message.getContent());
        System.out.println(DELETE_MESSAGE + message.getContent());
       return;
    }

    @Override
    public void deleteChannel(Channel channel) {
        if(!isValidateChannel(channel)){
            System.out.println(CHANNEL_NOT_EXIST + channel.getName());
            return;
        }
        channelDb.remove(channel);
        deletedChannelDb.put(channel.getId(),channel.getName());
        userDb.stream()
                .filter(x->x.getChannelDb().contains(channel))
                .forEach(x->x.removeChannel(channel));
        System.out.println(DELETE_CHANNEL + channel.getName());

    }

    @Override
    public void createUser(User user) {
        if(isValidateUser(user)){
            System.out.println(USER_EXIST + user.getName());
            return;
        }
        userDb.add(user);
        System.out.println(CREATE_USER + user.getName());
        return;

    }

    @Override
    public void createMessage(Message message) {
        if(isValidateMessage(message)){
            System.out.println(MESSAGE_EXIST + message.getContent());
            return;
        }
        if(!isValidateUser(message.getSender())){
            System.out.println(USER_EXIST + message.getSender().getName());
            return;
        }
        messageDb.add(message);
        System.out.println(CREATE_MESSAGE + message.getContent());
        return;
    }

    @Override
    public void createChannel(Channel channel) {
        if(isValidateChannel(channel)){
            System.out.println(CHANNEL_EXIST + channel.getName());
            return;
        }
        channelDb.add(channel);
        System.out.println(CREATE_CHANNEL + channel.getName());
        return;

    }

    public boolean isValidateUser( User user) {
        return userDb.stream().anyMatch(x -> x.getId() == user.getId());

    }

    public boolean isValidateMessage( Message message) {
        return messageDb.stream().anyMatch(x -> x.getId() == message.getId());

    }

    public boolean isValidateChannel( Channel channel) {
        return channelDb.stream().anyMatch(x -> x.getId() == channel.getId());

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
