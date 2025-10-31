package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.request.ChannelCreateReq;
import com.sprint.mission.discodeit.dto.request.ChannelCreateSecReq;
import com.sprint.mission.discodeit.dto.request.ChannelUpdateReq;
import com.sprint.mission.discodeit.dto.response.ChannelInfoRes;
import com.sprint.mission.discodeit.dto.response.ChannelPrivateInfoRes;
import com.sprint.mission.discodeit.dto.response.ChannelPublicInfoRes;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ChannelType;
import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.MessageRepository;
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
    private final MessageRepository messageRepository;

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
        return channelRepository.update(id, req.name(), req.description());
    }

    //채널 삭제
    @Override
    public Channel delete(UUID id) {
        return channelRepository.delete(id);
    }

    //채널 목록
    @Override
    public List<ChannelInfoRes> findAll(UUID userId) {
        return channelRepository.findAll().stream()
                .filter(channel ->
                        channel.getPublicType() == ChannelType.PUBLIC ||
                        (channel.getPublicType() == ChannelType.PRIVATE &&
                        channelRepository.isMember(userId, channel.getId()))
                ).map(this::mapChannelToInfoRes).toList();
    }

    //채널명으로 찾기
    @Override
    public ChannelInfoRes findByName(String name) {
        return mapChannelToInfoRes(channelRepository.findByName(name));
    }

    // 채널의 공개 여부에 따라 ResDTO를 맞게 변환해주는 메소드
    private ChannelInfoRes mapChannelToInfoRes(Channel channel) {
        Message lastMessage = messageRepository.findLastMessageByChannelId(channel.getId());

        return switch (channel.getPublicType()) {
            case PUBLIC -> ChannelPublicInfoRes.from(channel, lastMessage);
            case PRIVATE -> ChannelPrivateInfoRes.from(channel, lastMessage);
            default -> throw new IllegalStateException("Unknown ChannelType: " + channel.getPublicType());
        };
    }
}
