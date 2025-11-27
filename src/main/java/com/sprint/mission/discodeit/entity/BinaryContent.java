package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@AllArgsConstructor
@Getter @Setter
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {
//    이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
//[ ] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 readStatusID 참조 필드를 추가하세요.

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @OneToOne(fetch = FetchType.LAZY, mappedBy = "binaryContent", orphanRemoval = true, cascade = CascadeType.REMOVE)
    private MessageAttachments attachments;

    @Column(name = "bytes", nullable = false)
    private byte[] bytes;


//    public BinaryContent(String fileName, Long size, String contentType, MessageAttachments attachments) {
//        this.fileName = fileName;
//        this.size = size;
//        this.contentType = contentType;
//        this.attachments = attachments;
//    }
}
