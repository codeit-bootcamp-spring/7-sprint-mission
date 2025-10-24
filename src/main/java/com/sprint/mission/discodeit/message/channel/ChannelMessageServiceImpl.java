package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.participation.ParticipationRepository;
import com.sprint.mission.discodeit.common.utils.ParticipationDualKey;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
public class ChannelMessageServiceImpl extends BaseServiceImpl<ChannelMessage, UUID, ChannelMessageRepository> implements ChannelMessageService {

    private final ParticipationRepository participationRepository; // 사용자 참여 여부 확인을 위해 추가

    public ChannelMessageServiceImpl(ChannelMessageRepository channelMessageRepository, ParticipationRepository participationRepository) {
        super(channelMessageRepository);
        this.participationRepository = participationRepository;
    }

    @Override
    public ChannelMessage sendMessage(UUID channelId, UUID senderId, String message) {
        // 1. 비즈니스 규칙 검증: 메시지를 보내는 사용자가 해당 채널에 참여하고 있는지 확인
        ParticipationDualKey participationId = new ParticipationDualKey(channelId, senderId);
        if (!participationRepository.existsByIdNonDel(participationId)) {
            throw new SecurityException("해당 채널에 메시지를 보낼 권한이 없습니다.");
        }

        // 2. 엔티티 생성 위임
        ChannelMessage newChannelMessage = ChannelMessage.create(channelId, senderId, message);

        // 3. 데이터 저장
        save(newChannelMessage);
        return newChannelMessage;
    }

    @Override
    public List<ChannelMessage> getMessagesByChannel(UUID channelId) {
        return repository.findAllByChannelId(channelId);
    }
}