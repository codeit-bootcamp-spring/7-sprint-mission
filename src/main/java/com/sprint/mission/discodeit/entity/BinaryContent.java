package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entityElement.BinaryContentUsage;
import com.sprint.mission.discodeit.subTable.MessageAttachment;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.ArrayList;
import java.util.List;
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

    @Column(name = "size",nullable = false,columnDefinition = "bigint")
    private Long size;

    @OneToMany( mappedBy = "binaryContent",fetch = FetchType.LAZY)
    private List<MessageAttachment> messageAttachment;

}
