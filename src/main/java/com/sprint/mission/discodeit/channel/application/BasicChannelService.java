package com.sprint.mission.discodeit.channel.application;



import com.sprint.mission.discodeit.channel.domain.Channel;
import com.sprint.mission.discodeit.channel.presentation.ChannelService;
import org.springframework.stereotype.Service;

import java.util.*;

@Service
public class BasicChannelService implements ChannelService {

    private final ChannelRoomRepository channelRoomRepository;

    public BasicChannelService(ChannelRoomRepository channelRoomRepository) {
        this.channelRoomRepository = channelRoomRepository;
    }

    @Override
    public void save(Channel channel) {
        channelRoomRepository.save(channel);
        System.out.println("채팅방 등록 성공");
    }

    @Override
    public void remove(Channel channel) {
        channelRoomRepository.remove(channel);
        System.out.println("채팅방 삭제 성공");
    }

    @Override
    public Channel findById(UUID uuid) {

        return channelRoomRepository.findById(uuid).orElseThrow(()->new NoSuchElementException("채팅방을 찾을 수 없습니다."));
    }

    @Override
    public List<Channel> findAll() {
        return channelRoomRepository.findAll();
    }

}
