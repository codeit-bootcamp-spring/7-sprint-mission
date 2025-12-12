package com.sprint.mission.discodeit.user.repository;

import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.dao.InvalidDataAccessApiUsageException;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.UUID;

import static org.assertj.core.api.Assertions.*;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("User Repository Slice Test")
@Transactional
public class UserRepositorySliceTest {


    private static final Logger log = LoggerFactory.getLogger(UserRepositorySliceTest.class);
    @Autowired
    UserRepository userRepository;

    @Autowired
    TestEntityManager entityManager;

    private User user1;
    private User user2;

    @BeforeEach
    void setUp() {
        user1 = User.createUserFactory("user1","111@user","1234");
        user2 = User.createUserFactory("user2","222@user","1234");
    }

    @Test
    @DisplayName("[정상 케이스] 유저 다건 조회")
    void findAll_Success(){
        User save1 = userRepository.save(user1);
        User save2 = userRepository.save(user2);
        entityManager.flush();
        entityManager.clear();

        var actualResult = userRepository.findOneUser(List.of(save1.getId(),save2.getId()));

        assertThat(actualResult).contains(save1,save2);

    }

    @Test
    @DisplayName("[예외 케이스] 유저 다건 조회 실패")
    void findAll_Fail(){

        User save1 = userRepository.save(user1);
        User save2 = userRepository.save(user2);

        assertThatThrownBy(()->
                userRepository.findOneUser(List.of(null)))
                .isInstanceOf(NullPointerException.class);
    }


}
