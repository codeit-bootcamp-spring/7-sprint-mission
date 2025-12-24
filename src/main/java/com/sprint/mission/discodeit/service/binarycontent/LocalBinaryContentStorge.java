package com.sprint.mission.discodeit.service.binarycontent;

import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.util.UUID;

@Component
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local", matchIfMissing = true)
public class LocalBinaryContentStorge implements BinaryContentStorage {

    private final Path ROOT_PATH; // 루트 폴더

    public LocalBinaryContentStorge(
            @Value("${discodeit.storage.local.root-path}") String ROOT_PATH) {
        this.ROOT_PATH = Path.of(ROOT_PATH);
    }


    @PostConstruct
    void init() {
        System.out.println("ROOT PATH: " + ROOT_PATH.toAbsolutePath());
        try {
            Files.createDirectories(ROOT_PATH);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    @Override
    public InputStream get(UUID id) {
        Path filePath = ROOT_PATH.resolve(id.toString());
        InputStream inputStream;
        try {
            inputStream = Files.newInputStream(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return inputStream;
    }

    @Override
    public UUID put(MultipartFile file) {
        String contentType = file.getContentType();
        validateContentType(contentType);
        String fileName = UUID.randomUUID().toString();
        Path filePath = ROOT_PATH.resolve(fileName);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return UUID.fromString(fileName);
    }

    private void validateContentType(String contentType) {
        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + contentType);
        }
    }

    @Override
    public UrlResource getUrlResource(String fileName) {
        Path filePath = ROOT_PATH.resolve(fileName);
        UrlResource urlResource;
        try {
            urlResource = new UrlResource(filePath.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return urlResource;
    }

    @Override
    public void deleteFile(String fileName) {
        Path filePath = ROOT_PATH.resolve(fileName);
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }
}
