package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.UUID;

@Repository
public interface ReadStatusRepository extends JpaRepository<ReadStatus, UUID> {

    // 채널 DTO 생성시 사용되며, 조회된 readStatus의 user를 모두 조회하기 때문에 fetch join을 사용
    @Query("select rs from ReadStatus rs join fetch rs.user where rs.channel = :channel")
    List<ReadStatus> findAllByChannelWithUser(Channel channel);

    // 비공개 채널 조회에 사용되며, 조회된 readStatus의 channel을 모두 조회하기 때문에 fetch join을 사용
    @Query("select rs from ReadStatus rs join fetch rs.channel where rs.user.id = :userId")
    List<ReadStatus> findAllByUserIdWithChannel(UUID userId);


    @Query("select rs from ReadStatus rs join fetch rs.user join fetch rs.channel where rs.user.id = :userId")
    List<ReadStatus> findAllByUserIdWithUserAndChannel(UUID userId);

    void deleteByChannelId(UUID id);

    boolean existsByUserIdAndChannelId(UUID userId, UUID channelId);

    List<ReadStatus> findAllByChannelIdAndNotificationEnabled(UUID channelId, boolean notificationEnabled);
}