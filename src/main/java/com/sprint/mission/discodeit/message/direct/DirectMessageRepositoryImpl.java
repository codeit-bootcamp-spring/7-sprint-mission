package com.sprint.mission.discodeit.message.direct;

import com.sprint.mission.discodeit.common.repository.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Repository
public class DirectMessageRepositoryImpl extends BaseRepositoryImpl<DirectMessage, UUID> implements DirectMessageRepository {

    @Override
    public List<DirectMessage> findByReceiverId(UUID receiverId) {
        return dataMap.values().stream()
                .filter(dm -> !dm.isDeleted() && dm.getReceiverId().equals(receiverId))
                .sorted(Comparator.comparing(DirectMessage::getCreatedAt)) // 생성 시간 순으로 정렬
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectMessage> findBySenderId(UUID senderId) {
        return dataMap.values().stream()
                .filter(dm -> !dm.isDeleted() && dm.getSenderId().equals(senderId))
                .sorted(Comparator.comparing(DirectMessage::getCreatedAt)) // 생성 시간 순으로 정렬
                .collect(Collectors.toList());
    }

    @Override
    public List<DirectMessage> findByParticipants(UUID userOneId, UUID userTwoId) {
        return dataMap.values().stream()
                .filter(dm -> !dm.isDeleted() &&
                        ((dm.getSenderId().equals(userOneId) && dm.getReceiverId().equals(userTwoId)) ||
                                (dm.getSenderId().equals(userTwoId) && dm.getReceiverId().equals(userOneId)))
                )
                .sorted(Comparator.comparing(DirectMessage::getCreatedAt)) // 생성 시간 순으로 정렬
                .collect(Collectors.toList());
    }

    @Override
    public void deleteAllBySenderId(UUID senderId) {
        findBySenderId(senderId).stream()
                .map(DirectMessage::getId).forEach(this::deleteById);
    }
}