package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFUserRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicUserServiceTest {
    private UserRepository repo;

    @BeforeEach
    void setUp() {
        // 싱글톤 인스턴스 테스트용 repo
        repo = JCFUserRepository.getInstance();

        // 테스트 전 기존 싱글톤 초기화 (Reflection 이용)
        try {
            var field = BasicUserService.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("싱글톤 인스턴스 생성 및 주입 테스트")
    void singletonInstanceTest() {
    }

    // Reflection으로 private final userRepository 필드 확인
    private UserRepository getInjectedRepo(BasicUserService service) {
        try {
            var field = BasicUserService.class.getDeclaredField("userRepository");
            field.setAccessible(true);
            return (UserRepository) field.get(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}