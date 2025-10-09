package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.Participation;
import com.sprint.mission.discodeit.entity.Role;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.utils.ParticipationDualKey;

import java.util.List;
import java.util.UUID;

/**
 * 채널과 사용자의 '참여 관계'에 대한 비즈니스 로직을 처리하는 서비스 인터페이스입니다.
 */
public interface ParticipationService extends BaseService<Participation, ParticipationDualKey> {

    /**
     * 특정 사용자를 채널에 참여시킵니다.
     *
     * @param channelId 참여할 채널의 ID
     * @param userId    참여할 사용자의 ID
     * @param nickname  해당 채널에서 사용할 닉네임 (선택 사항)
     * @return 생성된 Participation 참여 관계 정보
     */
    Participation joinChannel(UUID channelId, UUID userId, String nickname);

    /**
     * 사용자 스스로 채널에서 나갑니다.
     *
     * @param channelId 나갈 채널의 ID
     * @param userId    나가는 사용자의 ID
     * @return 채널의 마지막 사용자가 나갔으면 true아니면 False
     * ** 추후App에서 Channel에게 삭제 명령을 내리게 하기 위해 boolean을 리턴함**
     */
    boolean leaveChannel(UUID channelId, UUID userId);

    /**
     * (추천) 관리자/소유자가 다른 사용자를 채널에서 강제로 내보냅니다.
     *
     * @param channelId 채널 ID
     * @param userIdToKick 강제로 내보낼 사용자의 ID
     * @param adminUserId 작업을 수행하는 관리자의 ID (권한 검증용)
     */
    void kickUserFromChannel(UUID channelId, UUID userIdToKick, UUID adminUserId);

    /**
     * 특정 채널에 참여하고 있는 모든 참여자 목록을 조회합니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 Participation 목록
     */
    /**
     * 특정 채널에 참여하고 있는 모든 참여자 목록을 조회합니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 해당 채널의 모든 Participation 목록
     */
    List<Participation> findParticipationsByChannelId(UUID channelId);

    /**
     * (추천) 특정 채널의 소유자를 찾습니다.
     *
     * @param channelId 조회할 채널의 ID
     * @return 채널의 소유자(OWNER)인 User 객체
     * @throws java.util.NoSuchElementException 소유자를 찾을 수 없는 경우
     */
    User findOwner(UUID channelId);

    /**
     * 특정 사용자가 참여하고 있는 모든 채널의 참여 정보를 조회합니다.
     *
     * @param userId 조회할 사용자의 ID
     * @return 해당 사용자의 모든 Participation 목록
     */
    List<Participation> findParticipationsByUserId(UUID userId);

    /**
     * 특정 사용자가 특정 채널에 참여하고 있는지 확인합니다.
     *
     * @param channelId 확인할 채널의 ID
     * @param userId    확인할 사용자의 ID
     * @return 참여하고 있으면 true, 그렇지 않으면 false
     */
    boolean isUserInChannel(UUID channelId, UUID userId);

    /**
     * 채널 내 특정 사용자의 역할을 변경합니다. (관리자 권한 필요)
     *
     * @param channelId    역할을 변경할 채널의 ID
     * @param targetUserId 역할을 변경할 대상 사용자의 ID
     * @param actorId      역할 변경을 시도하는 사용자(행위자)의 ID
     * @param newRole      새로운 역할
     */
    void changeRole(UUID channelId, UUID targetUserId, UUID actorId, Role newRole);

    /**
     * 채널 내 사용자의 닉네임을 변경합니다.
     *
     * @param channelId 닉네임을 변경할 채널의 ID
     * @param userId    닉네임을 변경할 사용자의 ID
     * @param newNickname 새로운 닉네임
     */
    void changeNickname(UUID channelId, UUID userId, String newNickname);

    /**
     * 특정 채널의 특정 사용자가 관리자인지 확인하고 boolean을 반환합니다
     *
     * @param channelId 사용자가 관리자인지 확인 할 채널 ID
     * @param userId    관리자인지 확인 할 사용자 ID
     * @return 관리자일 경우 true, 아닌 경우 false
     */
    boolean isOwner(UUID channelId, UUID userId);

}