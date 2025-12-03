package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import jakarta.persistence.EntityManager;
import org.hibernate.Session;
import org.hibernate.SessionFactory;
import org.hibernate.stat.Statistics;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Transactional
class UserServiceTest {

    @Autowired
    private EntityManager em;

    @Autowired
    private UserRepository userRepository;

    @Test
    void nPlusOneTest() {
        Session session = em.unwrap(Session.class); // SessionFactory 아님
        Statistics stats = session.getSessionFactory().getStatistics();
        stats.setStatisticsEnabled(true);

        // 초기화
        stats.clear();

        List<User> users = userRepository.findAll(); // LAZY 연관 엔티티 접근 시 N+1 발생 가능
        users.forEach(u -> System.out.println(u.getUserStatus().getLastActiveAt()));

        System.out.println("Entity fetch count: " + stats.getEntityFetchCount());
        System.out.println("Collection fetch count: " + stats.getCollectionFetchCount());
    }
}