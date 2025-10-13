package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.ChannelMessage;
import com.sprint.mission.discodeit.repository.ChannelMessageRepository;
import com.sprint.mission.discodeit.repository.ParticipationRepository;
import com.sprint.mission.discodeit.service.ChannelMessageService;
import com.sprint.mission.discodeit.service.EventService;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;

import java.util.List;
import java.util.UUID;

public class JCFChannelMessageService extends JCFBaseService<ChannelMessage, UUID, ChannelMessageRepository> implements ChannelMessageService {

    private final ChannelMessageRepository channelMessageRepository;
    private final ParticipationRepository participationRepository; // 사용자 참여 여부 확인을 위해 추가
    private final EventService eventService;

    public JCFChannelMessageService(ChannelMessageRepository channelMessageRepository, ParticipationRepository participationRepository, JCFEventService eventService) {
        super(channelMessageRepository);
        this.channelMessageRepository = channelMessageRepository;
        this.participationRepository = participationRepository;
        this.eventService = eventService;
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
        channelMessageRepository.save(newChannelMessage);
        return newChannelMessage;
    }

    @Override
    public List<ChannelMessage> getMessagesByChannel(UUID channelId) {
        return channelMessageRepository.findByChannelId(channelId);
    }
}