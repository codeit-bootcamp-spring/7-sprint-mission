package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.file.FileChannelRepository;
import org.junit.jupiter.api.AfterEach;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;



import static org.junit.jupiter.api.Assertions.*;

class FileChannelServiceTest {
    private FileChannelService channelService;
    private FileChannelRepository repository;
    private Path dataDir;

    @BeforeEach
    void setUp() throws IOException {
        channelService = FileChannelService.getInstance();
        repository = FileChannelRepository.getInstance();

        // 파일 데이터 폴더 경로 확인
        dataDir = Paths.get(System.getProperty("user.dir"), "file-data-map", "Channel");

        // 테스트 전 폴더 초기화
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        } else {
            Files.createDirectories(dataDir);
        }
    }

    @AfterEach
    void tearDown() throws IOException {
        // 테스트 후 폴더 초기화
        if (Files.exists(dataDir)) {
            Files.walk(dataDir)
                    .map(Path::toFile)
                    .forEach(f -> f.delete());
        }
    }

    @Test
    @DisplayName("Service 메서드 전체 테스트")
    void testAllMethods() {
        UUID managerId = UUID.randomUUID();

        // -------------------
        // 1️⃣ create
        // -------------------
        Channel channel1 = channelService.create(managerId, "General");
        Channel channel2 = channelService.create(managerId, "Random");

        assertNotNull(channel1.getId());
        assertNotNull(channel2.getId());

        // 파일 생성 확인
        assertTrue(Files.exists(dataDir.resolve(channel1.getId() + ".ser")));
        assertTrue(Files.exists(dataDir.resolve(channel2.getId() + ".ser")));

        // -------------------
        // 2️⃣ findAll
        // -------------------
        List<Channel> allChannels = channelService.findAll();
        assertEquals(2, allChannels.size());

        // -------------------
        // 3️⃣ findByName
        // -------------------
        Channel find1 = channelService.findByName("General");
        assertNotNull(find1);
        assertEquals(channel1.getId(), find1.getId());

        Channel find2 = channelService.findByName("Random");
        assertNotNull(find2);
        assertEquals(channel2.getId(), find2.getId());

        // -------------------
        // 4️⃣ update
        // -------------------
        Channel updated = channelService.update(channel1.getId(), "UpdatedChannel");
        assertEquals("UpdatedChannel", updated.getName());

        // 파일 내용 확인
        Channel loaded = channelService.findByName("UpdatedChannel");
        assertNotNull(loaded);
        assertEquals("UpdatedChannel", loaded.getName());

        // -------------------
        // 5️⃣ delete
        // -------------------
        Channel deleted = channelService.delete(channel2.getId());
        assertEquals(channel2.getId(), deleted.getId());

        // 파일 삭제 확인
        assertFalse(Files.exists(dataDir.resolve(channel2.getId() + ".ser")));

        // 삭제 후 조회
        assertNull(channelService.findByName("Random"));

        // findAll 확인
        List<Channel> remaining = channelService.findAll();
        assertEquals(1, remaining.size());
        assertEquals(channel1.getId(), remaining.get(0).getId());
    }
}