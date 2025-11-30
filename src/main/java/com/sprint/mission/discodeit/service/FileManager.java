package com.sprint.mission.discodeit.service;

import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
public class FileManager {

    private final Path rootDir = Paths.get("data/uploads"); // 루트 폴더

    @PostConstruct
    void init() {
        try {
            Files.createDirectories(rootDir);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

//        try {
//            List<Path> list = Files.walk(rootDir)
//                    .sorted((a, b) -> b.compareTo(a))
//                    .toList();// 파일부터 삭제, 폴더 나중에 삭제 (역순 정렬)
//            for (Path path : list) {
//                Files.deleteIfExists(path);
//            }
//
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }


    }

    public Path put(UUID userId, MultipartFile file, String binaryContentId) {
        String contentType = file.getContentType();
        validateContentType(contentType);

        Path userFolder = getUserFolder(userId);
        String fileName = makeFileName(file, binaryContentId);
        Path profilePath = userFolder.resolve(fileName);

        try {
            Files.deleteIfExists(profilePath);
            file.transferTo(profilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return profilePath;
    }

    public void delete(Path filePath) {
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    private Path getUserFolder(UUID userId) {
        Path userFolder;
        try {
            userFolder = Files.createDirectories(rootDir.resolve("user_" + userId));
        } catch (IOException e) {
            throw new RuntimeException("유저의 폴더 생성이 실패", e);
        }
        return userFolder;
    }

    private void validateContentType(String contentType) {
        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + contentType);
        }
    }

    private String makeFileName(MultipartFile file, String binaryContentId){
        int i = file.getOriginalFilename().lastIndexOf(".");
        String substring = file.getOriginalFilename().substring(i + 1);
        return binaryContentId+"."+substring;
    }
}
