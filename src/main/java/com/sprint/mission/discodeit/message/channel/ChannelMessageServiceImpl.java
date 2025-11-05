package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.exception.UserNotInChannelException;
import com.sprint.mission.discodeit.message.channel.dto.ChannelMSGRequestDTO;
import com.sprint.mission.discodeit.message.channel.dto.ChannelMSGResponseDTO;
import com.sprint.mission.discodeit.participation.Participation;
import com.sprint.mission.discodeit.participation.ParticipationDualKey;
import com.sprint.mission.discodeit.participation.ParticipationService;
import org.springframework.stereotype.Service;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

@Service
public class ChannelMessageServiceImpl extends BaseServiceImpl<ChannelMessage, UUID, ChannelMessageRepository> implements ChannelMessageService {

    private final ParticipationService participationService; // 사용자 참여 여부 확인을 위해 추가

    public ChannelMessageServiceImpl(ChannelMessageRepository channelMessageRepository, ParticipationService participationService) {
        super(channelMessageRepository);
        this.participationService = participationService;
    }

    @Override
    public ChannelMSGResponseDTO sendMessage(ChannelMSGRequestDTO requestDTO) {
        UUID channelId = requestDTO.channelId();
        UUID senderId = requestDTO.senderId();
        String message = requestDTO.message();
        // 1. 비즈니스 규칙 검증: 메시지를 보내는 사용자가 해당 채널에 참여하고 있는지 확인
        ParticipationDualKey participationId = new ParticipationDualKey(channelId, senderId);
        if (!participationService.existsByIdNonDel(participationId)) {
            throw new UserNotInChannelException(participationId);
        }

        // 2. 엔티티 생성 위임
        ChannelMessage newChannelMessage = ChannelMessage.create(channelId, senderId, message);

        // 3. 데이터 저장
        return ChannelMSGResponseDTO.from(save(newChannelMessage));
    }

    @Override
    public List<ChannelMSGResponseDTO> getMessagesByChannel(UUID channelId) {
        return repository.findAllByChannelId(channelId).stream()
                .map(ChannelMSGResponseDTO::from)
                .toList();
    }

    @Override
    public void deleteAllBySenderId(UUID senderId) {
        if(repository.findAllBySenderId(senderId).isEmpty()){
            return;
        }
        repository.deleteAllById(repository.findAllBySenderId(senderId).stream()
                .map(ChannelMessage::getId).toList());
    }

    @Override
    public int countNotReadChannelMessage(UUID channelId, UUID authorId) {
        // 1. 유저의 채널 참여 정보를 가져옵니다. (Participation 엔티티)
        Participation participation;
        try {
            // 참여 정보가 없으면 NoSuchElementException 발생
            participation = participationService.findByIdNonDel(
                    new ParticipationDualKey(channelId, authorId)
            );
        } catch (Exception e) {
            return 0;
        }

        List<ChannelMSGResponseDTO> messages = getMessagesByChannel(channelId);
        if (messages.isEmpty()) {
            return 0; // 채널에 메시지가 없으면 0개
        }

        Instant lastReadAt = participation.getLastReadAt();

        if (lastReadAt == null) {
            // 모든 메시지가 읽지 않은 메시지입니다.
            return messages.size();
        }

        long unreadCount = messages.stream()
                .filter(message -> message.createdAt().isAfter(lastReadAt)) //
                .count();

        return (int) unreadCount;
    }
}