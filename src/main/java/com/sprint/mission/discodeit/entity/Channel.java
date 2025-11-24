package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import com.sprint.mission.discodeit.dto.ChannelDto_Update;
import com.sprint.mission.discodeit.dto.Dto_CreateChannelPrivate;
import com.sprint.mission.discodeit.dto.Dto_CreateChannelPublic;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.MappedSuperclass;
import jakarta.persistence.Table;
import lombok.Getter;

import java.time.Instant;

import static com.sprint.mission.discodeit.entity.ChannelType.PRIVATE;
import static com.sprint.mission.discodeit.entity.ChannelType.PUBLIC;

@Getter
@Entity
@Table(name = "channels")
public class Channel extends BaseUpdatableEntity {

    @Enumerated(EnumType.STRING)
    private ChannelType type;

    @Column(name = "name", length =100)
    private String name;

    @Column(name = "description", length = 500)
    private String description;
}
