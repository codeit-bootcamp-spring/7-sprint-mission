package com.sprint.mission.discodeit.participation;

import com.sprint.mission.discodeit.common.repository.BaseRepository;

import java.util.List;
import java.util.UUID;

// BaseRepository의 ID 타입을 ParticipationDualKey로 지정합니다.
public interface ParticipationRepository extends BaseRepository<Participation, ParticipationDualKey> {


    /**
     * 특정 채널에 속한 모든 (삭제되지 않은) 참여 정보를 조회합니다.
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 활성 참여 정보 목록
     */
    List<Participation> findAllByChannelId(UUID channelId);

    /**
     * 특정 사용자가 참여하고 있는 모든 (삭제되지 않은) 참여 정보를 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 모든 활성 참여 정보 목록
     */
    List<Participation> findAllByUserId(UUID userId);

    /**
     * 특정 채널에 속한 모든 (삭제된) 참여 정보를 조회합니다.
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 활성 참여 정보 목록
     */
    List<Participation> findAllByChannelIdIsDel(UUID channelId);

    /**
     * 특정 사용자가 참여하고 있는 모든 (삭제된) 참여 정보를 조회합니다.
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 모든 활성 참여 정보 목록
     */
    List<Participation> findAllByUserIdIsDel(UUID userId);
}