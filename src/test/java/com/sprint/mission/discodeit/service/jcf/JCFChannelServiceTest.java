package com.sprint.mission.discodeit.service.jcf;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.jcf.JCFChannelRepository;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.List;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class JCFChannelServiceTest {
    private JCFChannelRepository channelRepository;
    private JCFChannelService channelService;


    @BeforeEach
    void setUp(){
        channelRepository = JCFChannelRepository.getInstance();
        channelService = JCFChannelService.getInstance();

        channelRepository.findAll().forEach(u -> channelRepository.delete(u.getId()));
    }

    @Test
    @DisplayName("채널 생성 및 저장")
    void create() {
        UUID managerId = UUID.randomUUID();

        Channel channel = channelService.create(managerId, "테스트채널");

        assertNotNull(channel);
        assertEquals("테스트채널", channel.getName());

        // 🔹 리포지토리 내부 데이터 확인
        Channel stored = channelRepository.findById(channel.getId());
        assertNotNull(stored);
        assertEquals(channel.getId(), stored.getId());
    }

    @Test
    @DisplayName("채널 수정")
    void update() {
        UUID managerId = UUID.randomUUID();
        Channel created = channelService.create(managerId, "수정전채널");

        Channel updated = channelService.update(created.getId(), "수정후채널");

        assertEquals("수정후채널", updated.getName());

        // 🔹 리포지토리 내부 확인
        Channel stored = channelRepository.findById(created.getId());
        assertEquals("수정후채널", stored.getName());
    }

    @Test
    @DisplayName("채널 삭제")
    void delete() {
        UUID managerId = UUID.randomUUID();
        Channel created = channelService.create(managerId, "삭제채널");

        Channel deleted = channelService.delete(created.getId());

        assertNotNull(deleted);
        assertEquals("삭제채널", deleted.getName());

        // 🔹 리포지토리 내부에서 제거 확인
        Channel found = channelRepository.findById(created.getId());
        assertNull(found);
    }

    @Test
    @DisplayName("채널목록 조회")
    void findAll() {
        UUID manager1 = UUID.randomUUID();
        UUID manager2 = UUID.randomUUID();

        channelService.create(manager1, "첫번째채널");
        channelService.create(manager2, "두번째채널");

        List<Channel> fromService = channelService.findAll();
        List<Channel> fromRepo = channelRepository.findAll();

        assertEquals(fromRepo.size(), fromService.size());
        assertTrue(fromRepo.containsAll(fromService));
    }

    @Test
    @DisplayName("채널명으로 조회")
    void findByName() {
        UUID managerId = UUID.randomUUID();
        Channel created = channelService.create(managerId, "조회채널");

        Channel found = channelService.findByName("조회채널");

        assertNotNull(found);
        assertEquals(created.getId(), found.getId());

        // 🔹 리포지토리 직접 확인
        Channel direct = channelRepository.findByName("조회채널");
        assertEquals(found.getId(), direct.getId());
    }
}