package com.sprint.mission.discodeit.message.channel;

import com.sprint.mission.discodeit.common.repository.impl.BaseRepositoryImpl;
import org.springframework.stereotype.Repository;

import java.util.Comparator;
import java.util.List;
import java.util.UUID;

@Repository
public class ChannelMessageRepositoryImpl extends BaseRepositoryImpl<ChannelMessage, UUID> implements ChannelMessageRepository {

    @Override
    public List<ChannelMessage> findAllByChannelId(UUID channelId) {
        if (dataMap.isEmpty()) {
            return List.of();
        }
        return dataMap.values().stream()
                .filter(cm -> !cm.isDeleted() && channelId.equals(cm.getChannelId()))
                .sorted(Comparator.comparing(ChannelMessage::getCreatedAt)) // 생성 시간(오름차순)으로 정렬
                .toList();
    }

    @Override
    public List<ChannelMessage> findAllBySenderId(UUID senderId) {
        if(dataMap.isEmpty()){
            return List.of();
        }
        return  dataMap.values().stream()
                .filter(cm -> !cm.isDeleted() && cm.getSenderId().equals(senderId))
                .sorted(Comparator.comparing(ChannelMessage::getCreatedAt))
                .toList();
    }

}