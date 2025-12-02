package com.sprint.mission.discodeit.entity;

import jakarta.persistence.*;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Getter
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity{

    @Column(name = "name")
    private String channelName;

    @Column(name = "type", nullable = false)
    @Enumerated(EnumType.STRING)
    private ChannelType channelType;

    private String description;

    public void update(String name, String description) {
        if(name != null && !name.isEmpty()){
            this.channelName = name;
        }
        if(description != null && !description.isEmpty()){
            this.description = description;
        }
    }

    @Override
    public String toString() {
        String str = super.toString();

        return "Channel{" +
                "channelName='" + channelName + '\'' +
                ", channelType=" + channelType +
                ", description=" + description +
                str +
                '}';
    }
}
