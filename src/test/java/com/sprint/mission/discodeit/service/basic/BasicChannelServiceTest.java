package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import static org.junit.jupiter.api.Assertions.*;

class BasicChannelServiceTest {
    private ChannelRepository repo;

    @BeforeEach
    void setUp() {
        // 싱글톤 인스턴스 테스트용 repo
        repo = JCFChannelRepository.getInstance();

        // 테스트 전 기존 싱글톤 초기화 (Reflection 이용)
        try {
            var field = BasicChannelService.class.getDeclaredField("instance");
            field.setAccessible(true);
            field.set(null, null);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }

    @Test
    @DisplayName("싱글톤 인스턴스 생성 및 주입 테스트")
    void singletonInstanceTest() {
        BasicChannelService service1 = BasicChannelService.getInstance(repo);
        BasicChannelService service2 = BasicChannelService.getInstance(repo);

        // 1️⃣ 싱글톤이므로 동일 인스턴스
        assertSame(service1, service2);

        // 2️⃣ 주입된 리포지토리 객체 확인
        assertNotNull(service1);
        assertEquals(repo, getInjectedRepo(service1));
    }

    // Reflection으로 private final channelRepository 필드 확인
    private ChannelRepository getInjectedRepo(BasicChannelService service) {
        try {
            var field = BasicChannelService.class.getDeclaredField("channelRepository");
            field.setAccessible(true);
            return (ChannelRepository) field.get(service);
        } catch (Exception e) {
            throw new RuntimeException(e);
        }
    }
}