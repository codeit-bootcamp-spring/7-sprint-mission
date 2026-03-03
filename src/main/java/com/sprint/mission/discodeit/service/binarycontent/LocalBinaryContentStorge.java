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
    public void put(String fileName, MultipartFile file) {
        try {
            Thread.sleep(3000);
        } catch (InterruptedException e) {
            Thread.currentThread().interrupt();
            throw new RuntimeException("Thread interrupted while simulating delay", e);
        }
        Path filePath = ROOT_PATH.resolve(fileName);
        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
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
