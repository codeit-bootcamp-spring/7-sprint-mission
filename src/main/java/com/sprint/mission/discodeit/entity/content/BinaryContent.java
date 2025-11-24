package com.sprint.mission.discodeit.entity.content;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdateEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Lob;
import lombok.Getter;
import lombok.NoArgsConstructor;

import java.io.Serializable;
import java.time.Instant;
import java.util.UUID;

@Getter
@Entity
@NoArgsConstructor
public class BinaryContent extends BaseEntity {


    //
    @Column(name = "file_name", length = 255, nullable = false)// 디폴트가 255인가보다
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", length = 100, nullable = false)
    private String contentType;

    @Lob//jpa표준방식 이진데이터 표시
    @Column(name = "bytes", nullable = false)
    private byte[] bytes;

    public BinaryContent(String fileName, Long size, String contentType, byte[] bytes) {

        //
        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.bytes = bytes;
    }
}
