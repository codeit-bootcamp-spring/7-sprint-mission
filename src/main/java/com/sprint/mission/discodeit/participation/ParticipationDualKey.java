package com.sprint.mission.discodeit.participation;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.user.User;

import java.io.Serializable;
import java.util.UUID;


/**
 * '어떤 유저'가 '어떤 채널'에 참여했는지에 대한 관계를 고유하게 식별하는 복합 키(Composite Key)입니다.
 * 이 키는 Participation 엔티티의 기본 키(Primary Key)로 사용됩니다.
 * <p>
 * Java Record를 사용하여 불변(immutable) 객체로 선언되었으며,
 * equals(), hashCode(), toString() 메서드가 자동으로 구현됩니다.
 *
 * @param channelId 참여 관계가 속한 채널의 고유 ID. {@link Channel}의 ID를 참조합니다.
 * @param authorId    참여 관계의 주체인 사용자의 고유 ID. {@link User}의 ID를 참조합니다.
 */
public record ParticipationDualKey(UUID channelId, UUID authorId) implements Serializable {
}
