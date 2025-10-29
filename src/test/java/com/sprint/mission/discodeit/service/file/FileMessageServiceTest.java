package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Message;
import com.sprint.mission.discodeit.repository.file.FileMessageRepository;
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

class FileMessageServiceTest {

    private FileMessageService messageService;
    private FileMessageRepository repository;
    private Path dataDir;

    @BeforeEach
    void setUp() throws IOException {

        repository = new FileMessageRepository();
        messageService = new FileMessageService(repository);

        // 파일 데이터 폴더 경로 확인
        dataDir = Paths.get(System.getProperty("user.dir"), "file-data-map", "Message");

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
        UUID userId = UUID.randomUUID();
        UUID channelId = UUID.randomUUID();

        // -------------------
        // 1️⃣ create
        // -------------------
        Message msg1 = messageService.create(new Message(channelId, userId, "Hello"));
        Message msg2 = messageService.create(new Message(channelId, userId, "World"));

        assertNotNull(msg1.getId());
        assertNotNull(msg2.getId());

        // 파일 생성 확인
        assertTrue(Files.exists(dataDir.resolve(msg1.getId() + ".ser")));
        assertTrue(Files.exists(dataDir.resolve(msg2.getId() + ".ser")));

        // -------------------
        // 2️⃣ findAllByUser
        // -------------------
        List<Message> userMessages = messageService.findAllByUser(userId);
        assertEquals(2, userMessages.size());

        // -------------------
        // 3️⃣ findAllByChannel
        // -------------------
        List<Message> channelMessages = messageService.findAllByChannel(channelId);
        assertEquals(2, channelMessages.size());

        // -------------------
        // 4️⃣ searchMessagesByContent
        // -------------------
        List<Message> searchHello = messageService.searchMessagesByContent("Hello");
        assertEquals(1, searchHello.size());
        assertEquals(msg1.getId(), searchHello.get(0).getId());

        List<Message> searchWorld = messageService.searchMessagesByContent("World");
        assertEquals(1, searchWorld.size());
        assertEquals(msg2.getId(), searchWorld.get(0).getId());

        // -------------------
        // 5️⃣ update
        // -------------------
        Message updated = messageService.update(msg1.getId(), "Hello Updated");
        assertEquals("Hello Updated", updated.getContent());

        // 파일 내용 확인
        List<Message> updatedSearch = messageService.searchMessagesByContent("Hello Updated");
        assertEquals(1, updatedSearch.size());
        assertEquals(msg1.getId(), updatedSearch.get(0).getId());

        // -------------------
        // 6️⃣ delete
        // -------------------
        Message deleted = messageService.delete(msg2.getId());
        assertEquals(msg2.getId(), deleted.getId());

        // 파일 삭제 확인
        assertFalse(Files.exists(dataDir.resolve(msg2.getId() + ".ser")));

        // 삭제 후 조회
        List<Message> postDeleteSearch = messageService.searchMessagesByContent("World");
        assertTrue(postDeleteSearch.isEmpty());

        // findAllByUser 확인
        List<Message> remaining = messageService.findAllByUser(userId);
        assertEquals(1, remaining.size());
        assertEquals(msg1.getId(), remaining.get(0).getId());
    }
}