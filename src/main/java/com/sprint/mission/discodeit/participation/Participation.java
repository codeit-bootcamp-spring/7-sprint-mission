package com.sprint.mission.discodeit.participation;

import com.sprint.mission.discodeit.channel.Channel;
import com.sprint.mission.discodeit.common.entity.BaseEntity;
import com.sprint.mission.discodeit.config.enums.Role;
import com.sprint.mission.discodeit.user.User;
import lombok.Setter;

import java.time.Instant;
import java.util.UUID;

public class Participation extends BaseEntity<ParticipationDualKey> {
    private String nickname;
    private Role role;
    @Setter
    private Instant lastReadAt;
    /**
     * '어떤 유저'가 '어떤 채널'에 참여했는지에 대한 관계를 고유하게 식별하는 복합 키(Composite Key)입니다.
     * 이 키는 Participation 엔티티의 기본 키(Primary Key)로 사용됩니다.
     * <p>
     * Java Record를 사용하여 불변(immutable) 객체로 선언되었으며,
     * equals(), hashCode(), toString() 메서드가 자동으로 구현됩니다.
     *
     * @param channelId 참여 관계가 속한 채널의 고유 ID. {@link Channel}의 ID를 참조합니다.
     * @param userId    참여 관계의 주체인 사용자의 고유 ID. {@link User}의 ID를 참조합니다.
     */
    private Participation(UUID channelId, UUID userId) {
        super(new ParticipationDualKey(channelId, userId));
    }
    public static Participation create(UUID channelId, UUID userId, String nickname, Role role) {
        if(channelId == null){
            throw new IllegalArgumentException("참여하거나 관리할 채널을 찾을 수 없습니다.");
        }
        if(userId == null){
            throw new IllegalArgumentException("해당 사용자를 찾을 수 없습니다.");
        }
        if(role == null){
            role = Role.USER;
        }
        Participation participation = new Participation(channelId, userId);
        participation.nickname = nickname;
        participation.role = role;
        return participation;
    }

    public void changeNickname(String nickname) {
        this.nickname = nickname;
    }
    public void changeRole(Role role) {
        this.role = role;
    }

    public UUID getChannelId() {
        return getId().channelId();
    }
    public UUID getUserId() {
        return getId().authorId();
    }
    public String getNickname() {
        return nickname;
    }
    public Role getRole() {
        return role;
    }
    public Instant getLastReadAt() {return lastReadAt;}

    @Override
    public String toString() {
        return "Participation{"+ super.toString()+
                " nickname='" + nickname + '\'' +
                ", role=" + role +
                "} " ;
    }


}