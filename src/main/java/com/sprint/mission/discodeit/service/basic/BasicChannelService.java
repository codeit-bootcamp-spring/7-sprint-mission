package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.ChannelService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@RequiredArgsConstructor
@Service
public class BasicChannelService implements ChannelService {
    //레포지토리
    private final ChannelRepository channelRepository;

    //채널 생성
    @Override
    public Channel create(ChannelCreateReq req) {
        return channelRepository.save(req.to());
    }

    //채널 생성: 비공개
    @Override
    public Channel create(ChannelCreateSecReq req) {
        return channelRepository.save(req.to());
    }
    
    //채널 수정
    @Override
    public Channel update(UUID id, ChannelUpdateReq req) {
        Channel channel = channelRepository.findById(id);
        if (channel.getPublicType() == ChannelType.PRIVATE){
            throw new RuntimeException("Cannot update private channel");
        }
        return channelRepository.update(id, req.name(), req.description());
    }

    //채널 삭제
    @Override
    public Channel delete(UUID id) {
        return channelRepository.delete(id);
    }

    //채널 목록 : Public 인 경우 전부, Private 인 경우 자신이 참여한 채널만
    @Override
    public List<Channel> findAll(UUID userId) {
        return channelRepository.findAll().stream().filter(channel ->
                channel.getPublicType() == ChannelType.PUBLIC ||
                (channel.getPublicType() == ChannelType.PRIVATE &&
                channelRepository.isMember(userId, channel.getId()))).toList();
    }

    //자신이 참여한 채널 목록 : Public과 Private 전부 자신이 참여한 채널들만
    public List<Channel> findAllByUserId(UUID userId){
        return channelRepository.findAllByUserId(userId);
    }

    //채널명으로 찾기
    @Override
    public Channel findByName(String name) {
        return channelRepository.findByName(name);
    }
}
