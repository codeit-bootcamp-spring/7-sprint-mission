package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.UUID;

public interface ChannelRepository extends JpaRepository<Channel, UUID> {

    @Query("""
            select c from Channel c
            where c.type = com.sprint.mission.discodeit.common.enums.ChannelScope.PUBLIC""")
    List<Channel> findAllPublic();

    @Query("""
            select c from Channel c
            where :user member of c.participants
                        and c.type = com.sprint.mission.discodeit.common.enums.ChannelScope.PRIVATE""")
    List<Channel> findAllPrivateByUser(User user);

}
