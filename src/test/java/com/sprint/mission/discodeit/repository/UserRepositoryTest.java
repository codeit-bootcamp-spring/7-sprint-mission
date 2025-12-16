package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.entity.UserStatus;
import jakarta.persistence.EntityManagerFactory;
import org.hibernate.SessionFactory;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.orm.jpa.DataJpaTest;
import org.springframework.boot.test.autoconfigure.orm.jpa.TestEntityManager;
import org.springframework.mock.web.MockMultipartFile;
import org.springframework.test.context.ActiveProfiles;

import java.util.List;

import static org.assertj.core.api.Assertions.assertThat;

@DataJpaTest
@ActiveProfiles("test")
@DisplayName("사용자 레포지토리 슬라이스 테스트")
class UserRepositoryTest {

    @Autowired
    private UserRepository userRepository;

    @Autowired
    TestEntityManager em;

    @Autowired
    EntityManagerFactory emf;

    private MockMultipartFile mockFile() {
        return new MockMultipartFile(
                "file",
                "profile.png",
                "image/png",
                "dummy image".getBytes()
        );
    }

    @Test
    @DisplayName("정상적으로 fetch join을 사용해 한 번에 사용자를 조회할 수 있다")
    void findUser_Success() {
        // given
        MockMultipartFile mockFile = mockFile();

        BinaryContent profile = new BinaryContent(
                mockFile.getOriginalFilename(),
                mockFile.getSize(),
                mockFile.getContentType()
        );
        em.persist(profile); // profile을 영속성 컨텍스트에 저장

        User user = new User("test", "test@naver.com", "test1234", profile);
        em.persist(user); // user를 영속성 컨텍스트에 저장

        UserStatus status = new UserStatus(user);
        em.persist(status); // userstatus를 영속성 컨텍스트에 저장

        // 조회 쿼리 보기 위함
        em.flush(); // DB에 데이터 반영
        em.clear(); // 영속성 컨텍스트 삭제

        emf.unwrap(SessionFactory.class)
                .getStatistics().clear(); // 쿼리 횟수 초기화 (fetch join 테스트 위함)

        // when
        List<User> users = userRepository.findAllWithProfileAndStatus();

        // then
        assertThat(users).hasSize(1);

        User found = users.get(0);

        assertThat(found.getCreatedAt()).isNotNull();
        assertThat(found.getUpdatedAt()).isNotNull();

        long count = emf.unwrap(SessionFactory.class)
                .getStatistics().getPrepareStatementCount();

        assertThat(count).isEqualTo(1); // 실제 조회시 fetch join으로 쿼리를 1번만 보냈는지 확인
    }
}