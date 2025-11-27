package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.entity.entityType.ChannelType;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.ToString;

import java.util.ArrayList;
import java.util.List;

@Getter @ToString
@Entity
@Table(name = "channels")
@NoArgsConstructor
public class Channel extends BaseUpdatableEntity {

    @Column(nullable = false, length = 10)
    @Enumerated(EnumType.STRING)
    private ChannelType type;    // 채널타입(public, private)

    @Column(length = 100)
    private String name;
    @Column(length = 500)
    private String description;

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadStatus> readStatuses = new ArrayList<>();

    // PUBLIC
    public Channel(String name, String description, ChannelType type) {
        this.name = name;
        this.description = description;
        this.type = type;
    }

    // PRIVATE
    public Channel(ChannelType type) {
        super();
        this.name = null;
        this.description = null;
        this.type = type;
    }

    // updateMessage (private는 수정불가)
    public void updateChannelInfo(String newName, String newDescription) {
        if (newName != null && !newName.isBlank()) {
            this.name = newName;
        }

        if (newDescription != null) {
            this.description = newDescription;
        }
    }

}
