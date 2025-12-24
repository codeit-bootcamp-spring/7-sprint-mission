package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.common.enums.ChannelScope;
import jakarta.persistence.*;
import lombok.*;

import java.util.*;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Getter
@Table(name = "channels")
public class Channel extends BaseEntity {

    @Setter
    private String name;

    @Column(nullable = false, updatable = false)
    @Enumerated(EnumType.STRING)
    private ChannelScope type;

    @Setter
    private String description;


    @ManyToMany
    @JoinTable(
            name = "channel_participants",
            joinColumns = @JoinColumn(name = "channel_id"),
            inverseJoinColumns = @JoinColumn(name = "user_id")
    )
    private Set<User> participants = new HashSet<>();

    public static Channel createPublicChannel(String name, String description) {
        Channel c= new Channel();
        c.name = name;
        c.type = ChannelScope.PUBLIC;
        c.description = description;
        c.participants = new HashSet<>();
        return c;
    }

    public static Channel createPrivateChannel(Set<User> participants) {
        Channel c = new Channel();
        c.type = ChannelScope.PRIVATE;
        c.participants = new HashSet<>();
        c.participants.addAll(participants);
        return c;
    }

    public void addParticipant(User user) {
        participants.add(user);
    }

    public void addParticipant(HashSet<User> users) {
        participants.addAll(users);
    }

    public void removeParticipant(User user) {
        participants.remove(user);
    }

    public void removeParticipant(HashSet<User> user) {
        participants.removeAll(user);
    }
}
