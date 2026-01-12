package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.data.BinaryContentDto;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import java.nio.charset.StandardCharsets;
import java.util.UUID;
import org.junit.jupiter.api.Assertions;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.HttpHeaders;
import org.springframework.http.ResponseEntity;

@SpringBootTest(properties = {"discodeit.storage.type=s3"})
class S3BinaryContentStorageTest {

    @Autowired
    private BinaryContentStorage storage;

    @Test
    void put_and_download_redirect() {
        UUID id = UUID.randomUUID();
        byte[] bytes = "s3-storage-test".getBytes(StandardCharsets.UTF_8);

        storage.put(id, bytes);

        BinaryContentDto meta = new BinaryContentDto(
                id,
                "test.txt",
                (long) bytes.length,
                "text/plain"
        );

        ResponseEntity<?> res = storage.download(meta);

        Assertions.assertTrue(res.getStatusCode().is3xxRedirection());
        String location = res.getHeaders().getFirst(HttpHeaders.LOCATION);
        Assertions.assertNotNull(location);
        Assertions.assertTrue(location.startsWith("https://"));
    }
}
