package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

@Transactional
@SpringBootTest
class ChannelServiceTest {

    @Autowired
    private EntityManager em;
    @Autowired
    private ChannelService channelService;

    @Autowired
    private ChannelRepository channelRepository;
    @Autowired
    private UserRepository userRepository;


    @Test
    void getAllByUser() {
        Session session = em.unwrap(Session.class); // SessionFactory 아님
        Statistics stats = session.getSessionFactory().getStatistics();
        stats.setStatisticsEnabled(true);



        stats.clear();

        channelService.getAllByUser(UUID.fromString("2e43e001-7815-450a-9f79-7c837aa871d8"));


        System.out.println("Entity fetch count: " + stats.getEntityFetchCount());
        System.out.println("Collection fetch count: " + stats.getCollectionFetchCount());
    }
}