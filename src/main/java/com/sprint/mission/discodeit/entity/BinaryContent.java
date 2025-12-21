package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.Getter;
import lombok.ToString;
import org.apache.tomcat.util.http.fileupload.InvalidFileNameException;

import java.util.Objects;


@Getter
@ToString
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {
    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(name = "size", nullable = false)
    private Long size;

    protected BinaryContent() {}

    public BinaryContent(String fileName, String contentType, long size) {
        super();
        this.fileName = Objects.requireNonNull(fileName).trim();
        if(this.fileName.isEmpty() || this.fileName.length() > 255) {
            throw new InvalidFileNameException(fileName, "fileName");
        }
        this.contentType = Objects.requireNonNull(contentType);
        this.size = (long) size;
    }
}
