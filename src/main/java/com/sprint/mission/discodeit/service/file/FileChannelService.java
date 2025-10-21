package com.sprint.mission.discodeit.service.file;


import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import com.sprint.mission.discodeit.service.basic.BasicChannelService;

import java.util.List;
import java.util.UUID;

public class FileChannelService implements ChannelService {
    private final BasicChannelService basicChannelService;

    public FileChannelService() {
        this.basicChannelService = new BasicChannelService(new FileChannelRepository());
    }

    public FileChannelService(ChannelRepository channelRepository) {
        this.basicChannelService = new BasicChannelService(channelRepository);
    }

    @Override public Channel create(Channel channel) {return basicChannelService.create(channel);}
    @Override public Channel get(UUID id) {return basicChannelService.get(id);}
    @Override public Channel update(Channel channel) {return basicChannelService.update(channel);}
    @Override public boolean delete(UUID id) {return basicChannelService.delete(id);}
    @Override public List<Channel> getAll() {return basicChannelService.getAll();}
    @Override public boolean join(UUID channelId, UUID userId) {return basicChannelService.join(channelId, userId);}
    @Override public boolean leave(UUID channelId, UUID userId) {return basicChannelService.leave(channelId, userId);}
    @Override public void setSlowModeSeconds(UUID channelId, int slowModeSeconds) {basicChannelService.setSlowModeSeconds(channelId, slowModeSeconds);}
}
