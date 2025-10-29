package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.ChannelMember;
import com.sprint.mission.discodeit.domain.ReadStatus;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.time.Duration;
import java.time.Instant;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class ChannelMemberService {

    private final ChannelRepository channelRepository;

    public void addChannelMember(UUID channelId,UUID userId){
        Channel channel = ChannelFindHelper.findById(channelRepository, channelId);
        channel.addChannelMember(userId);
        channelRepository.save(channel);
    }

    public long getMemberReadStatus(UUID channelId,UUID memberId){
        Channel channel = ChannelFindHelper.findById(channelRepository, channelId);
        ChannelMember channelMember = channel.getMembers().stream().filter(cm -> cm.getMemberId().equals(memberId))
                .findAny().orElseThrow(() -> new NoSuchElementException("해당 유저가 채널에 없습니다. "));
        ReadStatus readStatus = channelMember.getReadStatus();
        Instant lastReadAt = readStatus.getLastReadAt();
        return Duration.between(lastReadAt, Instant.now()).toMinutes();
    }
}
