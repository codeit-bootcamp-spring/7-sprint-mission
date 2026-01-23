package com.sprint.mission.discodeit.storage.s3;

import com.sprint.mission.discodeit.dto.response.binarycontent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.storage.S3BinaryContentStorage;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.core.io.Resource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;

import java.io.BufferedReader;
import java.io.IOException;
import java.io.InputStream;
import java.nio.charset.StandardCharsets;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.Properties;
import java.util.UUID;

import static org.junit.jupiter.api.Assertions.*;

class S3BinaryContentStorageTest {
    @Test
    @DisplayName("put 후 get하면 동일한 바이트를 얻는다")
    void putThenGet_returnsSameBytes() throws Exception {
        Properties prop = loadEnv();
        S3BinaryContentStorage storage = createStorage(prop);

        UUID id = UUID.randomUUID();
        byte[] data = ("s3-put-get-test-" + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);

        try {
            storage.put(id, data);

            try (InputStream inputStream = storage.get(id)) {
                byte[] bytes = inputStream.readAllBytes();
                assertArrayEquals(data, bytes);
            }
        } finally {
            try { storage.delete(id); }
            catch (Exception ignored) {}
        }
    }

    @Test
    @DisplayName("download는 presigned url로 302 리다이렉트한다.")
    void download_redirectsToPresignedUrl() {
        Properties prop = loadEnv();
        S3BinaryContentStorage storage = createStorage(prop);

        UUID id = UUID.randomUUID();
        byte[] data = ("s3-download-test-" + UUID.randomUUID()).getBytes(StandardCharsets.UTF_8);

        try {
            storage.put(id, data);

            BinaryContentResponseDto binaryContentResponseDto = new BinaryContentResponseDto(
                    id,
                    "test.txt",
                    "text/plain",
                    (long) data.length
            );

            ResponseEntity<Resource> download = storage.download(binaryContentResponseDto);
            assertEquals(HttpStatus.FOUND, download.getStatusCode());

            String location = download.getHeaders().getFirst(HttpHeaders.LOCATION);
            assertNotNull(location);
            assertTrue(location.contains("X-Amz-"), "Location should be a presigned URL");
            assertTrue(location.startsWith("http"), "Location should be a URL");
        } finally {
            try {storage.delete(id); }
            catch (Exception ignored) {}
        }
    }

    private Properties loadEnv() {
        Path envPath = Path.of(".env");
        if (!Files.exists(envPath)) {
            throw new IllegalStateException(".env not found");
        }

        Properties prop = new Properties();
        try(BufferedReader br = Files.newBufferedReader(envPath)) {
            String line;
            while ((line = br.readLine()) != null) {
                line = line.trim();
                if (line.isEmpty() || line.startsWith("#")) continue;

                int index = line.indexOf('=');
                if (index < 0) continue;

                String key = line.substring(0, index).trim();
                String value = line.substring(index + 1).trim();

                if (value.length() >= 2) {
                    char c = value.charAt(0), l = value.charAt(value.length() - 1);
                    if ((c == '"' && l == '"') || (c == '\'' && l == '\'')) {
                        value = value.substring(1, value.length() - 1);
                    }
                }

                prop.setProperty(key, value);
            }
        } catch (IOException e) {
            throw new IllegalStateException("Failed to read .env", e);
        }
        return prop;
    }

    private static String propertyCheck(Properties prop, String key) {
        String v = prop.getProperty(key);
        if (v == null || v.isBlank()) {
            throw new IllegalStateException("key is null or blank");
        }
        return v.trim();
    }

    private S3BinaryContentStorage createStorage(Properties prop) {
        return new S3BinaryContentStorage(
                propertyCheck(prop, "AWS_S3_ACCESS_KEY"),
                propertyCheck(prop, "AWS_S3_SECRET_KEY"),
                propertyCheck(prop, "AWS_S3_REGION"),
                propertyCheck(prop, "AWS_S3_BUCKET"),
                Long.parseLong(prop.getProperty("AWS_S3_PRESIGNED_URL_EXPIRATION", "600"))

        );
    }
}