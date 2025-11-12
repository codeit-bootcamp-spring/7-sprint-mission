package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.service.dto.response.BinaryContentResponse;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class BinaryContentService {

    private final Path rootDir = Paths.get("data/uploads"); // 루트 폴더

    private final BinaryContentRepository binaryContentRepository;

    // 유저별 폴더 생성
    public Path createUserFolder(UUID userId)  {

        Path userFolder;

        try {
            userFolder = Files.createDirectories(rootDir.resolve("user_" + userId.toString()));
            Files.createDirectories(userFolder.resolve("profile"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        return userFolder;
    }
    
    public BinaryContent saveMessageFile(UUID userId, MultipartFile file)  {
        createUserFolder(userId);
        Path userFolder = rootDir.resolve("user_" + userId.toString());
        String fileName= makeFileName(file.getOriginalFilename());
        Path filePath = userFolder.resolve(fileName);

        try {
            file.transferTo(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        BinaryContent content = new BinaryContent(
                fileName,
                file.getContentType(),
                filePath.toString(),
                file.getSize()
        );
        binaryContentRepository.save(content);

        return content;
    }

    public void deleteMessageImage(BinaryContent binaryContent) {
        Path filePath = Paths.get(binaryContent.getFilePath());
        try {
            Files.deleteIfExists(filePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }


    public BinaryContent saveUserProfile(UUID userId, MultipartFile profile) {
        String contentType = profile.getContentType();
        validateContentType(contentType);

        Path userFolder = rootDir.resolve("user_" + userId.toString());
        
        Path profileFolder;

        try {
            profileFolder = Files.createDirectories(userFolder.resolve("profile"));
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

        String profileFilePath = makeProfileName(profile.getOriginalFilename());
        Path profilePath = profileFolder.resolve(profileFilePath);

        try {
            Files.deleteIfExists(profilePath);
            profile.transferTo(profilePath);
        } catch (IOException e) {
            throw new RuntimeException(e);
        }

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

    public BinaryContent getById(UUID binaryId){
        return binaryContentRepository.findById(binaryId).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
    }

    public Resource getImageFile(UUID binaryId) {
        BinaryContent content = getById(binaryId);
        Path path = Paths.get(content.getFilePath());

        UrlResource urlResource =null;
        try {
            urlResource = new UrlResource(path.toUri());
        } catch (MalformedURLException e) {
            throw new RuntimeException(e);
        }
        return urlResource;
    }

    public List<BinaryContentResponse> getBinaryContents(List<UUID> ids){
        List<BinaryContentResponse> result = new ArrayList<>();
        for (UUID id : ids) {
            BinaryContent content =
                    binaryContentRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("없음"));

            result.add(BinaryContentResponse.from(content));
        }

        return result;
    }

    public BinaryContentResponse getBinaryContent(UUID id){
            BinaryContent content =
                    binaryContentRepository.findById(id)
                            .orElseThrow(() -> new IllegalArgumentException("없음"));
            return BinaryContentResponse.from(content);
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



