package com.sprint.mission.discodeit.entity.binaryContent;

import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;
import java.util.UUID;

@Repository
public interface BinaryContentRepository {

    BinaryContent save(BinaryContent t);

    Optional<BinaryContent> findById(UUID id);

    List<BinaryContent> findAll();

    // 물리 삭제
    void deleteById(UUID id);

    //==========================

    // 유저의 프로필 이미지 확인 (채널Id가 null이어야함)
    Optional<BinaryContent> findProfileImageByUserId(UUID userId);

    // 서비스에서 채널을 받아 UUID만 가져와서 binary만 뽑기 -> 한 채널의 모든 이미지, 파일 검색
    List<BinaryContent> findAllByMessageId(List<UUID> messageIds);
}
