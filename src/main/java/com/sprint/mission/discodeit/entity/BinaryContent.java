package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import com.sprint.mission.discodeit.entity.base.BaseUpdatableEntity;
import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

@Slf4j
@Entity
@Table(name = "binary_contents")
@Getter @Setter
@NoArgsConstructor(access = AccessLevel.PROTECTED) // 🔥 추가 필수
//@Builder
public class BinaryContent extends BaseUpdatableEntity {
//    이미지, 파일 등 바이너리 데이터를 표현하는 도메인 모델입니다. 사용자의 프로필 이미지, 메시지에 첨부된 파일을 저장하기 위해 활용합니다.
//[ ] User, Message 도메인 모델과의 의존 관계 방향성을 잘 고려하여 readStatusID 참조 필드를 추가하세요.

    @Column(name = "file_name", nullable = false, length = 255)
    private String fileName;

    @Column(name = "size", nullable = false)
    private Long size;

    @Column(name = "content_type", nullable = false, length = 100)
    private String contentType;

    @Column(nullable = false, length = 20)
    @Enumerated(EnumType.STRING)
//    @Builder.Default
    BinaryContentStatus status = BinaryContentStatus.PROCESSING;

    void updateStatus(BinaryContentStatus status) {
        this.status = status;
    }

    public BinaryContent(MultipartFile file ) {
        this.fileName = file.getOriginalFilename();
        this.size = file.getSize();
        this.contentType = file.getContentType();
    }
}
