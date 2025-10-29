package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.MessageRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFMessageRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicMessageServiceTest {
    private MessageRepository repo;

    @BeforeEach
    void setUp() {
        // 싱글톤 인스턴스 테스트용 repo
        repo = JCFMessageRepository.getInstance();

        // 테스트 전 기존 싱글톤 초기화 (Reflection 이용)
        try {
            var field = BasicMessageService.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("싱글톤 인스턴스 생성 및 주입 테스트")
    void singletonInstanceTest() {
        BasicMessageService service1 = BasicMessageService.getInstance(repo);
        BasicMessageService service2 = BasicMessageService.getInstance(repo);

        // 1️⃣ 싱글톤이므로 동일 인스턴스
        assertSame(service1, service2);

        // 2️⃣ 주입된 리포지토리 객체 확인
        assertNotNull(service1);
        assertEquals(repo, getInjectedRepo(service1));
    }

    // Reflection으로 private final messageRepository 필드 확인
    private MessageRepository getInjectedRepo(BasicMessageService service) {
        try {
            var field = BasicMessageService.class.getDeclaredField("messageRepository");
            field.setAccessible(true);
            return (MessageRepository) field.get(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}