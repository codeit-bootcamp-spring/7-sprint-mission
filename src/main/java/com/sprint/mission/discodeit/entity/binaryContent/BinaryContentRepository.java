package com.sprint.mission.discodeit.entity.binaryContent;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

public interface BinaryContentRepository {

    BinaryContent save(BinaryContent b);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAll();

    // 물리 삭제
    void deleteById(UUID id);

    //==========================

    // 유저의 프로필 이미지 확인 (채널Id가 null이어야함)
    Optional<BinaryContent> findProfileImageByUserId(UUID userId);
    // 유저 프로필 이미지 삭제
    void deleteProfileImageByUserId(UUID userId);


    // 하나의 메시지에 있는 모든 첨부파일 확인
    List<BinaryContent> findAllByMessageId(UUID messageId);
    // 하나의 메시지에 있는 모든 첨부파일 삭제
    void deleteAllByMessageId(UUID messageId);
    // 요구사항 findAll
    List<BinaryContent> findAllByIdIn(List<UUID> ids);

}
