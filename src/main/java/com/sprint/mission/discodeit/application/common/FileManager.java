package com.sprint.mission.discodeit.application.common;

import com.sprint.mission.discodeit.domain.binarycontent.BinaryContentRepository;
import com.sprint.mission.discodeit.domain.binarycontent.BinaryContent;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.*;
import java.nio.file.attribute.BasicFileAttributes;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class FileManager {

    private final Path rootDir = Paths.get("data/uploads"); // 루트 폴더

    private final BinaryContentRepository binaryContentRepository;

    // 유저별 폴더 생성
    public Path createUserFolder(UUID userId) throws IOException {
        return Files.createDirectories(rootDir.resolve("user_" + userId.toString()));
    }

    //MultipartFile을 받아서 유저 폴더에 저장
    public BinaryContent saveUserFile(UUID userId, MultipartFile file) throws IOException {
        if (file == null || file.isEmpty()) {
            throw new IllegalArgumentException("파일이 비어있거나 null입니다.");
        }
        String contentType = file.getContentType();
        validateContentType(contentType);

        Path userFolder = createUserFolder(userId);

        //getInputStream()은 업로드된 파일의 바이트 데이터를 읽을 수 있는 통로를 열어주는 것
        //스트림 자체에 데이터가 있는 게 아니라, 읽는 순간 소스에서 데이터를 가져오는 구조
        //Multipart를 통해 가져온 스트림은 Multipart의 데이터만 읽을 수 있음


        String fileName = UUID.randomUUID().toString();
        Path filePath = userFolder.resolve(fileName);

        file.transferTo(filePath);
        // 이렇게도 가능
//        try (InputStream is = file.getInputStream()) {
//            Files.copy(is, filePath, StandardCopyOption.REPLACE_EXISTING);
//        }
        //new BufferedInputStream(file.getInputStream()) 이런 식으로 버퍼드로 감쌀 수도 있음
        // 이미 내부적으로 버퍼드 처리가 되지만 이중으로 감싸면 더 안정적

        // 4. BinaryContent 생성
        BinaryContent content = new BinaryContent(fileName,
                file.getContentType(),
                filePath.toString(),
                file.getSize()
        );


        return content;
    }

    private void validateContentType(String contentType) {
        if (contentType == null ||
                !(contentType.equals("image/png")
                        || contentType.equals("image/jpeg")
                        || contentType.equals("image/gif"))) {
            throw new IllegalArgumentException("허용되지 않는 파일 형식입니다: " + contentType);
        }
    }


    // 유저 삭제 시 폴더 통째로 삭제
    public void deleteUserFolder(UUID userId) {
        Path userFolder = rootDir.resolve("user_" + userId.toString());

//        이런 로직도 가능
//        try (Stream<Path> walk = Files.walk(userFolder)) {
//            // 하위 경로부터 거꾸로 정렬 (파일 → 폴더 순)
//            walk.sorted(Comparator.reverseOrder())
//                    .forEach(path -> {
//                        try {
//                            Files.delete(path); // 각 파일/폴더 삭제
//                        } catch (IOException e) {
//                            System.err.println("삭제 실패: " + path + " (" + e.getMessage() + ")");
//                        }
//                    });
//            System.out.println("폴더 삭제 완료: " + userFolder);
//        } catch (IOException e) {
//            System.err.println("폴더 삭제 중 오류 발생: " + e.getMessage());
//        }

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

}



