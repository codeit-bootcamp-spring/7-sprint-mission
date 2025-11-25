package com.sprint.mission.discodeit.entity;

import com.sprint.mission.discodeit.entity.base.BaseEntity;
import jakarta.persistence.*;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Builder;

@Getter
@NoArgsConstructor
@Entity
@Table(name = "binary_contents")
public class BinaryContent extends BaseEntity {

    @Column(nullable = false)
    private String fileName;

    @Column(nullable = false)
    private long size;

    @Column(nullable = false)
    private String contentType;

    // Message와 1:1(선택적)
    @OneToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "message_id")
    private Message message;

    // User 프로필 이미지(N:1)
    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "user_id")
    private User user;

    @Builder
    public BinaryContent(String fileName, long size, String contentType,
                         Message message, User user) {

        this.fileName = fileName;
        this.size = size;
        this.contentType = contentType;
        this.message = message;
        this.user = user;
    }
}
