package service.jcf;

import entity.Channel;
import service.ChannelService;

import java.util.*;

public class JCFChannelService implements ChannelService {

    // 싱글톤
    private static JCFChannelService instance;
    private final Map<UUID, Channel> data;

    private JCFChannelService() {
        this.data = new HashMap<>();
    }

    public static JCFChannelService getInstance() {
        if (instance == null) {
            instance = new JCFChannelService();
        }
        return instance;
    }

    @Override
    public void create(Channel channel) {
        data.put(channel.getId(), channel);
    }

    @Override
    public Channel findById(UUID id) {
        return data.get(id);
    }

    @Override
    public List<Channel> findAll() {
        return new ArrayList<>(data.values());
    }

    @Override
    public void update(Channel channel) {
        if (data.containsKey(channel.getId())) {
            // privacy는 변경 불가, topic만 변경 가능
            Channel existing = data.get(channel.getId());
            existing.changeTopic(channel.getTopic()); // touch() 포함
        }
    }

    @Override
    public void delete(UUID id) {
        data.remove(id);
    }
}