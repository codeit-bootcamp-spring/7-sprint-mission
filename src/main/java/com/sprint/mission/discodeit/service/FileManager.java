package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.io.InputStream;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.NoSuchElementException;
import java.util.UUID;

@Component
@Slf4j
@RequiredArgsConstructor
@ConditionalOnProperty(prefix = "discodeit.storage", name = "type", havingValue = "local", matchIfMissing = true)
public class FileManager{

    @Value("${discodeit.storage.local.root-path}")
    private String ROOT_PATH;

    private final Path rootDir = Paths.get(ROOT_PATH); // 루트 폴더

    private final BinaryContentRepository binaryContentRepository;

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

    public ResponseEntity<UrlResource> getUrl(UUID id) {
        BinaryContent content = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
        Path path = Paths.get(content.getFilePath());
        UrlResource urlResource;
        try {
            urlResource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        String contentType = content.getFileType();
        if(contentType == null || contentType.isBlank()){
            contentType = "application/octet-stream";
        }
        String fileName = content.getFileName();


        return ResponseEntity.ok()
                .contentType(MediaType.parseMediaType(contentType))
                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment; filename=\"" + fileName + "\"")
                .body(urlResource);
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
