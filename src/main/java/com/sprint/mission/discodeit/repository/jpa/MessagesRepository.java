package com.sprint.mission.discodeit.repository.jpa;

import com.sprint.mission.discodeit.entity.Message;
import java.util.List;
import java.util.Optional;
import java.util.UUID;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

public interface MessagesRepository extends JpaRepository<Message, UUID> {
//    void save(T model);
//    void deleteById(UUID id);
//    Optional<T> findById(UUID id);
//    List<T> findAll();

//    @Query("""
//    SELECT m
//    FROM Message m
//    WHERE m.channelId = :channelId
//      AND m.createdAt = (
//          SELECT MAX(m2.createdAt)
//          FROM Message m2
//          WHERE m2.channelId = :channelId
//      )
//    """)
//    Optional<Message> findLatestMessageByCreatedAt(@Param("channelId") UUID channelId);

    @Query("""
    SELECT m
    FROM Message m
    WHERE m.channelId = :channelId
    ORDER BY m.createdAt DESC
    """)
    Optional<Message> findLatestMessage(@Param("channelId") UUID channelId, Pageable pageable);
//    Optional<Message> findFirstByChannelIdOrderByCreatedAtDesc(UUID channelId); //?? Instant 하나만 리턴 가능??

    List<Message> findAllByChannelId(UUID channelID);
//    List<Message> findAllByChannelIdOrderByUpdatedAtDesc(UUID channelID);
//    Set<UUID> findAllUsersInChannel(List<Message> allMessageInChannel);
}
