package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;

@Entity
@Table(name = "channels")
public class ChannelEntity extends BaseUpdatableEntity{

    @Column(nullable = false)
    private String name;

    @Column(length = 500)
    private String description;

    @Enumerated(EnumType.STRING)
    private ChannelType type;


}
