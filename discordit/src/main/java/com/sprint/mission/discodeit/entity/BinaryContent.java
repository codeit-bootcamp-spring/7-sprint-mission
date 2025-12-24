package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Entity
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Table(name = "binary_contents")
@Getter
@AllArgsConstructor
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    String fileName;

    @Column(nullable = false)
    Long size;

    @Column(nullable = false)
    String contentType;
}
