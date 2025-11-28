package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.Table;
import jakarta.validation.constraints.NotNull;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;

import java.time.Instant;
import lombok.NoArgsConstructor;
import lombok.Setter;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Entity
@Table(name = "channels")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
@AllArgsConstructor
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(name = "name", length =100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;

//    public Channel(ChannelType type, String name, String description) {
//        this.type = type;
//        this.name = name;
//        this.description = description;
//    }
}
