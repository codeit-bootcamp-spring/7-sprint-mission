package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.Message;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.Optional;
import java.util.UUID;

public interface MessageRepository extends JpaRepository<Message, UUID> {
    void deleteAllByChannel(Channel channel);

    @Query("""
            select m from Message m
            where m.channel = :c
            order by m.createdAt desc
            limit 1""")
    Optional<Message> findLastByChannel(Channel c);

    Page<Message> findAllByChannel(Channel channel, Pageable pageable);
}
