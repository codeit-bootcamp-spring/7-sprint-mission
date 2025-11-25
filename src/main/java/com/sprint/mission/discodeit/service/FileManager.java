package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

@Service
@Slf4j
@RequiredArgsConstructor
public class FileManager {

    private final Path rootDir = Paths.get("data/uploads"); // 루트 폴더


    public Path put(UUID userId, MultipartFile file) {
        String contentType = file.getContentType();
        validateContentType(contentType);
        Path userFolder = getUserFolder(userId);

        String filePath =
                makeFileName(file.getOriginalFilename() == null ? file.getContentType() : file.getOriginalFilename());
        Path profilePath = userFolder.resolve(filePath);

        try {
            Files.deleteIfExists(profilePath);
            file.transferTo(profilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return profilePath;
    }

    public void delete(BinaryContent content) {
        Path filePath = Paths.get(content.getFilePath());
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



    private String makeFileName(String originalName) {
        int pos = originalName.lastIndexOf(".");
        String Ext = originalName.substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        return uuid + "." + Ext;
    }

//    public Resource getImageFile(String id) {
//        BinaryContent content = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
//        Path path = Paths.get(content.getFilePath());
//
//        UrlResource urlResource = null;
//        try {
//            urlResource = new UrlResource(path.toUri());
//        } catch (MalformedURLException e) {
//            throw new RuntimeException(e);
//        }
//        return urlResource;
//    }
}
