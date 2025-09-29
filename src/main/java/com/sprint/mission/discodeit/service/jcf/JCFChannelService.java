package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.ChannelService;

import java.lang.reflect.Array;
import java.util.*;
import java.util.stream.Stream;

public class JCFChannelService implements ChannelService {

    Map<UUID, Channel> channels = new HashMap<>();

    @Override
    public Channel create(Channel channel) {
        channels.put(channel.getId(), channel);
        return channel;
    }

    @Override
    public Channel read(UUID id) {
        return channels.get(id);
    }

    @Override
    public List<Channel> readAll() {
        List<Channel> chnnalList = new ArrayList<>(channels.values());  // users 전체출력
        chnnalList.sort(Comparator.comparing(Channel::getCreatedAt));    // 생성일자 오름차순 정렬
        return chnnalList;
    }



    @Override
    public void update(UUID id, Channel channel) {
        // 뭘 써야함...??
    }

    @Override
    public void delete(UUID id) {
        channels.remove(id);
    }
}
