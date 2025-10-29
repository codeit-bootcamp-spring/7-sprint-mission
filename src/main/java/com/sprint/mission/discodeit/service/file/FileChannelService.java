package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
public class FileChannelService implements ChannelService {

    //레포지토리
    private final FileChannelRepository fileChannelRepository;

    //채널 생성
    @Override
    public Channel create(UUID managerId, String name) {
        return fileChannelRepository.save(new  Channel(managerId, name));
    }
    
    //채널 수정
    @Override
    public Channel update(UUID id, String name) {
        return fileChannelRepository.update(id, name);
    }

    //채널 삭제
    @Override
    public Channel delete(UUID id) {
        return fileChannelRepository.delete(id);
    }

    //채널 목록
    @Override
    public List<Channel> findAll() {
        return fileChannelRepository.findAll();
    }

    //채널명으로 찾기
    @Override
    public Channel findByName(String name) {
        return fileChannelRepository.findByName(name);
    }
}
