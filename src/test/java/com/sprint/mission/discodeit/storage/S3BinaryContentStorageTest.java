package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import org.junit.jupiter.api.Disabled;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.http.ResponseEntity;

import java.io.IOException;
import java.io.InputStream;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

@SpringBootTest
@Disabled
class S3BinaryContentStorageTest {

    @Autowired
    private BinaryContentStorage binaryContentStorage;

    @Nested
    @DisplayName("put")
    class PutTest {

        @Test
        @DisplayName("s3 put 메서드 성공")
        void put_success() throws IOException {
            // given
            UUID binaryId = UUID.randomUUID();
            byte[] bytes = "test".getBytes();

            // when
            UUID addedId = binaryContentStorage.put(binaryId, bytes);
            try (InputStream inputStream = binaryContentStorage.get(binaryId)) {
                byte[] loaded = inputStream.readAllBytes();
                assertArrayEquals(bytes, loaded);
            }

            // then
            assertEquals(binaryId, addedId);
            assertNotNull(addedId);

        }

        @Test
        @DisplayName("잘못된 request 값일경우 RuntimeException 예외발생")
        void put_throws_when_invalid_request() {
            // given
            byte[] bytes = null;

            // when & then
            assertThrows(RuntimeException.class, () -> binaryContentStorage.put(UUID.randomUUID(), bytes));
        }
    }

    @Nested
    @DisplayName("get")
    class GetTest {

        @Test
        @DisplayName("s3 get 메서드 성공")
        void get_success() throws IOException {
            // given
            UUID binaryId = UUID.randomUUID();
            byte[] bytes = "test".getBytes();
            binaryContentStorage.put(binaryId, bytes);

            // when & then
            try (InputStream inputStream = binaryContentStorage.get(binaryId)) {
                byte[] loaded = inputStream.readAllBytes();
                assertArrayEquals(bytes, loaded);
            }


        }

        @Test
        @DisplayName("없는 값일경우 RuntimeException 예외발생")
        void get_throws_when_not_found() {
            // given
            UUID binaryId = UUID.randomUUID();

            // when & then
            assertThrows(RuntimeException.class, () -> binaryContentStorage.get(binaryId));
        }
    }

    @Nested
    @DisplayName("download")
    class DownloadTest {

        @Test
        @DisplayName("Download 성공하여 302 redirect 반환")
        void download_return_302_and_location_header() throws IOException {
            // given
            BinaryContentResponseDto binaryContentResponseDto = new BinaryContentResponseDto(
                    UUID.randomUUID(),
                    "test.txt",
                    "text/plain",
                    4L);

            // when
            ResponseEntity<?> response = binaryContentStorage.download(binaryContentResponseDto);

            // then
            assertEquals(302, response.getStatusCode().value());
            assertNotNull(response.getHeaders().getLocation());

            String location = response.getHeaders().getLocation().toString();
            assertTrue(location.contains("X-Amz-Algorithm="));
        }
    }
}