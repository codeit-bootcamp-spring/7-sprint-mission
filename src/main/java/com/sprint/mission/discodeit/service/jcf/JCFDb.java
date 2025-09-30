package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.DbService;

import java.awt.List;
import java.util.*;

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
        if (!isValidateUser(userDb, user)) {
            System.out.println("존재하지 않는 유저입니다 : " + user.getName());
            return;
        }
        userDb.remove(user);
        deletedUserDb.put(user.getId(), user.getName());

        channelDb.stream()
                        .filter(x-> x.getUserDb().contains(user))
                                .forEach(x->
                                        new JCFChannel(this).deleteUserFromChannel(user,x)
                                        );
        messageDb.stream()
                        .filter(x->x.getSender()==user)
                                .forEach(x->
                                        new JCFMessage(this).changeToDeletedUser(x)
                                        );

        System.out.println("유저가 삭제되었습니다 : " + user.getName());
        return;
    }

    @Override
    public void deleteMessage(Message message) {
        if(!isValidateMessage(messageDb,message)){
            System.out.println("존재하지 않는 메세지입니다 : " + message.getContent());
            return;
        }
        messageDb.remove(message);
        deletedMessageDb.put(message.getId(),message.getContent());
        System.out.println("메세지가 삭제되었습니다 : " + message.getContent());
       return;
    }

    @Override
    public void deleteChannel(Channel channel) {
        if(!isValidateChannel(channelDb,channel)){
            System.out.println("존재하지 않는 채널입니다 : " + channel.getName());
            return;
        }
        channelDb.remove(channel);
        deletedChannelDb.put(channel.getId(),channel.getName());
        System.out.println("채널이 삭제되었습니다 : " + channel.getName());

    }

    @Override
    public void createUser(User user) {
        if(isValidateUser(userDb,user)){
            System.out.println("이미 존재하는 유저입니다 : " + user.getName());
            return;
        }
        userDb.add(user);
        System.out.println("유저가 생성되었습니다 : " + user.getName());
        return;

    }

    @Override
    public void createMessage(Message message) {
        if(isValidateMessage(messageDb,message)){
            System.out.println("이미 존재하는 메세지입니다 : " + message.getContent());
            return;
        }
        messageDb.add(message);
        System.out.println("메세지가 생성되었습니다 : " + message.getContent());
        return;
    }

    @Override
    public void createChannel(Channel channel) {
        if(isValidateChannel(channelDb,channel)){
            System.out.println("이미 존재하는 채널입니다 : " + channel.getName());
            return;
        }
        channelDb.add(channel);
        System.out.println("채널이 생성되었습니다 : " + channel.getName());
        return;

    }

    public boolean isValidateUser(ArrayList<User> dataBase, User user) {
        return dataBase.stream().anyMatch(x -> x.getId() == user.getId());

    }

    public boolean isValidateMessage(ArrayList<Message> messageDb, Message message) {
        return messageDb.stream().anyMatch(x -> x.getId() == message.getId());

    }

    public boolean isValidateChannel(ArrayList<Channel> channelDb, Channel channel) {
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
