package com.sprint.mission.discodeit.repository.jcf;

import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.repository.ParticipationRepository;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;

import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

// JCFBaseRepository의 ID 타입을 ParticipationDualKey로 지정합니다.
public class JCFParticipationRepository extends JCFBaseRepository<Participation, ParticipationDualKey> implements ParticipationRepository {

    @Override
    public List<Participation> findAllByChannelId(UUID channelId) {
        return dataMap.values().stream()
                .filter(p -> !p.isDeleted() && p.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Participation> findAllByUserId(UUID userId) {
        return dataMap.values().stream()
                .filter(p -> !p.isDeleted() && p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
    @Override
    public List<Participation> findAllByChannelIdIsDel(UUID channelId) {
        return dataMap.values().stream()
                .filter(p -> p.isDeleted() && p.getChannelId().equals(channelId))
                .collect(Collectors.toList());
    }

    @Override
    public List<Participation> findAllByUserIdIsDel(UUID userId) {
        return dataMap.values().stream()
                .filter(p -> p.isDeleted() && p.getUserId().equals(userId))
                .collect(Collectors.toList());
    }
}