package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    // 데이터 저장 필드
    private final Map<UUID, Channel> channels = new HashMap<>();

    // 싱글톤 패턴
    private static JCFChannelService instance;
    public static JCFChannelService getInstance(){
        if (instance == null){
            instance = new JCFChannelService();
        }
        return instance;
    }

    @Override
    public Channel createChannel(String channelName) {
        Channel channel = new Channel(channelName);
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel findByChannelName(String channelName) {
        for (Channel channel : channels.values()) {
            if(channel.getChannelName().equals(channelName)){
                return channel;
            }
        }
        return null;
    }

    @Override
    public List<Channel> findAllChannels() {
        return new ArrayList<>(channels.values());
    }

    @Override
    public Channel updateChannel(UUID id, String channelName) {
        Channel channel = channels.get(id);
        channel.setChannelName(channelName);
        return channel;
    }

    @Override
    public Channel addMember(UUID channelId, UUID userId) {
        Channel channel = channels.get(channelId);
        channel.addMember(userId);
        return channel;
    }

    @Override
    public Channel removeMember(UUID channelId, UUID userId) {
        Channel channel = channels.get(channelId);
        channel.removeMember(userId);
        return channel;
    }

    @Override
    public void deleteChannel(UUID id) {
        channels.remove(id);
    }
}