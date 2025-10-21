package service;

import entity.Channel;

import java.util.List;
import java.util.UUID;

public interface ChannelService {
    void create(Channel channel);
    Channel findById(UUID id);
    List<Channel> findAll();
    void update(Channel channel);
    void delete(UUID id);

}
