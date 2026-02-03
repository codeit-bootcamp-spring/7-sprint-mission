package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Slice;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.Instant;
import java.util.List;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {

    void deleteAllByChannelId(UUID channelId);

    @Query(value = "SELECT m FROM Message m "
            + "LEFT JOIN FETCH m.author a "
            + "LEFT JOIN FETCH a.profile "
            + "LEFT JOIN FETCH m.attachments at "
            + "WHERE m.channel.id =:channelId And m.createdAt < :createdAt")
    Slice<Message> findAllByChannelId(UUID channelId, Instant createdAt, Pageable pageable);

    @Query(value = "SELECT m FROM Message m "
            + "WHERE m.channel.id = :channelId "
            + "ORDER BY m.createdAt DESC ")
    List<Message> findLatestByChannelId(UUID channelId);
}

// Page<T>
// - 전체 페이지 수(totalElements)를 알고 싶을 때 사용
// - 내부적으로 count 쿼리 실행
// - 실제 데이터 조회는 OFFSET + LIMIT 사용
//   예: SELECT * FROM message
//        WHERE channel_id = ? AND created_at < ?
//        ORDER BY created_at DESC
//        LIMIT 10 OFFSET 20;
// - 데이터가 많으면 OFFSET 때문에 성능 저하 가능

// Slice<T>
// - 전체 페이지 수는 필요 없고 다음 페이지 존재 여부(hasNext)만 필요할 때 사용
// - count 쿼리 없음
// - 실제 데이터 조회는 LIMIT(+1) 사용 (커서 기반 페이징과 유사)
//   예: SELECT * FROM message
//        WHERE channel_id = ? AND created_at < ?
//        ORDER BY created_at DESC
//        LIMIT 11;  // 요청한 페이지 크기보다 1개 더 가져와 다음 페이지 존재 판단
// - 데이터가 많아도 OFFSET이 없어 성능 좋음

