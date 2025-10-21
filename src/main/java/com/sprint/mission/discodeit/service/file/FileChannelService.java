package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.io.*;
import java.util.*;

public class FileChannelService implements ChannelService {

    private static final String CHANNEL_FILE_PATH = "channels.ser";
    private static final File FILE = new File(CHANNEL_FILE_PATH);
    private final Map<UUID, Channel> channelMap;

    public FileChannelService() {
        this.channelMap = loadData();
    }


    private Map<UUID, Channel> loadData(){
        try (FileInputStream load = new FileInputStream(CHANNEL_FILE_PATH);
             ObjectInputStream ois = new ObjectInputStream(load)) {

            return (Map<UUID, Channel>) ois.readObject();

        } catch (IOException | ClassNotFoundException e) {
            e.printStackTrace();
            return new HashMap<>();
        }
    }

    private void saveData(Map<UUID, Channel> data) {
        try (FileOutputStream fis = new FileOutputStream(CHANNEL_FILE_PATH);
             ObjectOutputStream oos = new ObjectOutputStream(fis);) {

            oos.writeObject(data);

        } catch (IOException e){
            e.printStackTrace();
        }
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel newChannel = new Channel(channelName);
        UUID channelId = newChannel.getId();
        channelMap.put(channelId, newChannel);
        saveData(channelMap);
        return newChannel;
    }

    @Override
    public Channel findByChannelName(String channelName) {
        for(Channel channel : channelMap.values()){
            if(channel.getChannelName().equals(channelName)){
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(channelMap.values());
    }

    @Override
    public Channel updateChannel(UUID id, String channelName) {
        Channel channel = channelMap.get(id);
        channel.setChannelName(channelName);
        saveData(channelMap);
        return channel;
    }

    @Override
    public Channel addMember(UUID channelId, UUID userId) {
        Channel channel = channelMap.get(channelId);
        channel.addMember(userId);
        saveData(channelMap);
        return channel;
    }

    @Override
    public Channel removeMember(UUID channelId, UUID userId) {
        Channel channel = channelMap.get(channelId);
        channel.removeMember(userId);
        saveData(channelMap);
        return channel;
    }

    @Override
    public void deleteChannel(UUID id) {
        channelMap.remove(id);
        saveData(channelMap);
    }
}
