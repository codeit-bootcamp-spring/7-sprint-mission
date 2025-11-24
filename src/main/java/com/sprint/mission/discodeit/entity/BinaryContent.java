package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

import static java.time.Instant.*;

@Builder
@Getter
@Entity
@Table(name = "binary_contents")
@NoArgsConstructor
@AllArgsConstructor
public class BinaryContent extends BaseEntity implements Serializable {

    @Column(name = "file_name",length = 255,nullable = false)
    private String fileName;

    @Column(name = "content_type",nullable = false)
    private String contentType;

//    @Column(name = "bytes",nullable = false,columnDefinition = "bytea")
//    private byte [] bytes;

    @Column(name = "size",nullable = false,columnDefinition = "bigint")
    private Long size;

}
