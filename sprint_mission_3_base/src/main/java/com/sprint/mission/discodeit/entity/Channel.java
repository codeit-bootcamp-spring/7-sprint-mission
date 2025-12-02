package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

import java.util.ArrayList;
import java.util.List;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseEntity {

    @Column(nullable = false)
    private String name;

    // Message (1:N)
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Message> messages = new ArrayList<>();

    // ReadStatus (1:N)
    @OneToMany(mappedBy = "channel", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<ReadStatus> readStatuses = new ArrayList<>();

    @Builder
    public Channel(String name) {
        this.name = name;
    }

    public void update(String name) {
        this.name = name;
    }
}
