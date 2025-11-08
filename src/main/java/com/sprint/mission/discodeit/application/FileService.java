package com.sprint.mission.discodeit.application;

import com.sprint.mission.discodeit.domain.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class FileService {

    private final Path rootDir = Paths.get("data/uploads"); // 루트 폴더

    private final BinaryContentRepository binaryContentRepository;

    // 유저별 폴더 생성
    public Path createUserFolder(UUID userId) throws IOException {

        Path userFolder = Files.createDirectories(rootDir.resolve("user_" + userId.toString()));
        Path profileFolder = Files.createDirectories(userFolder.resolve("profile"));
        return userFolder;
    }
    
    public BinaryContent saveMessageFile(UUID userId, MultipartFile file) throws IOException {
        createUserFolder(userId);
        Path userFolder = rootDir.resolve("user_" + userId.toString());
        String fileName= makeFileName(file.getOriginalFilename());
        Path filePath = userFolder.resolve(fileName);
        file.transferTo(filePath);

        BinaryContent content = new BinaryContent(
                fileName,
                file.getContentType(),
                filePath.toString(),
                file.getSize()
        );
        binaryContentRepository.save(content);

        return content;
    }

    public void deleteMessageImage(BinaryContent binaryContent) throws IOException {
        Path filePath = Paths.get(binaryContent.getFilePath());
        Files.deleteIfExists(filePath);
    }


    public BinaryContent saveUserProfile(UUID userId, MultipartFile profile) throws IOException {
        String contentType = profile.getContentType();
        validateContentType(contentType);

        Path userFolder = rootDir.resolve("user_" + userId.toString());
        
        Path profileFolder = Files.createDirectories(userFolder.resolve("profile"));
        String profileFilePath = makeProfileName(profile.getOriginalFilename());
        Path profilePath = profileFolder.resolve(profileFilePath);
        
        Files.deleteIfExists(profilePath);
        
        profile.transferTo(profilePath);

        BinaryContent content = new BinaryContent("profile",
                profile.getContentType(),
                profilePath.toString(),
                profile.getSize()
        );

        binaryContentRepository.save(content);

        return content;
    }

    private String makeProfileName(String originalName){
        int pos = originalName.lastIndexOf(".");
        String ext = originalName.substring(pos + 1);
        String fileName = "profile" + "." + ext;
        return fileName;
    }


    private String makeFileName(String originalName){
        int pos = originalName.lastIndexOf(".");
        String Ext = originalName.substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        String fileName = uuid + "." + Ext;
        return fileName;
    }



    public void deleteUserFolder(UUID userId) {
        Path userFolder = rootDir.resolve("user_" + userId.toString());

        try {
            Files.walkFileTree(userFolder, new SimpleFileVisitor<>() {
                // 파일 삭제
                @Override
                public FileVisitResult visitFile(Path file, BasicFileAttributes attrs) throws IOException {
                    Files.delete(file);
                    return FileVisitResult.CONTINUE;
                }

                // 폴더 삭제 (하위 파일/폴더 삭제 후)
                @Override
                public FileVisitResult postVisitDirectory(Path dir, IOException exc) throws IOException {
                    Files.delete(dir);
                    return FileVisitResult.CONTINUE;
                }

            });
        } catch (IOException e) {
            e.printStackTrace();
        }
    }

    public BinaryContent findById(UUID binaryId){
        return binaryContentRepository.findById(binaryId).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
    }

    public Resource findImageFile(UUID binaryId) throws MalformedURLException {
        BinaryContent content = findById(binaryId);
        Path path = Paths.get(content.getFilePath());
        return new UrlResource(path.toUri());
    }

    private void validateContentType(String contentType) {
        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + contentType);
        }
    }



}



