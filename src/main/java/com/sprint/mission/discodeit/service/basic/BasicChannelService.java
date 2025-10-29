package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.context.annotation.Primary;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    //레포지토리
    private ChannelRepository channelRepository;

    //채널 생성
    @Override
    public Channel create(UUID managerId, String name) {
        return channelRepository.save(new  Channel(managerId, name));
    }
    
    //채널 수정
    @Override
    public Channel update(UUID id, String name) {
        return channelRepository.update(id, name);
    }

    //채널 삭제
    @Override
    public Channel delete(UUID id) {
        return channelRepository.delete(id);
    }

    //채널 목록
    @Override
    public List<Channel> findAll() {
        return channelRepository.findAll();
    }

    //채널명으로 찾기
    @Override
    public Channel findByName(String name) {
        return channelRepository.findByName(name);
    }
}
