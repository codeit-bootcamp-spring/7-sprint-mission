package com.sprint.mission.discodeit.binarycontent.domain;

import lombok.Getter;
import lombok.RequiredArgsConstructor;

import java.time.Instant;
import java.util.UUID;
@Getter
@RequiredArgsConstructor
public class BinaryContent {

    private final UUID id;
    private final Instant createdAt;

    private String fileName;  // profile.png
    private String filePath;  // uploads/user123/profile.png
    private String fileType;  // image/png
    private long fileSize;    // 1024

//    public BinaryContent saveProfileImage(MultipartFile file, UUID userId) throws IOException {
//        if (file == null || file.isEmpty()) return null;
//
//        String originalFileName = file.getOriginalFilename();
//        String fileType = file.getContentType();
//        long fileSize = file.getSize();
//
//        // 서버에 저장할 경로 생성
//        String filePath = uploadDir + "user_" + userId + "/" + originalFileName;
//        File dest = new File(filePath);
//
//        // 부모 디렉터리 없으면 생성
//        dest.getParentFile().mkdirs();
//
//        // 파일 저장
//        file.transferTo(dest);
//
//        // BinaryContent 생성
//        BinaryContent binaryContent = new BinaryContent(
//                UUID.randomUUID(),
//                Instant.now(),
//                originalFileName,
//                filePath,
//                fileType,
//                fileSize
//        );

//    @Service
//    public class FileService {
//
//        private final String rootDir = "data/uploads/"; // 프로젝트 루트 아래
//
//        public BinaryContent saveProfileImage(MultipartFile file, UUID userId) throws IOException {
//            if (file == null || file.isEmpty()) return null;
//
//            String originalFileName = file.getOriginalFilename();
//            String fileType = file.getContentType();
//            long fileSize = file.getSize();
//
//            // 유저별 폴더 생성
//            String userFolderPath = rootDir + "user_" + userId + "/";
//            File userFolder = new File(userFolderPath);
//            if (!userFolder.exists()) {
//                userFolder.mkdirs();
//            }
//
//            // 파일 경로
//            String filePath = userFolderPath + originalFileName;
//            File dest = new File(filePath);
//
//            // 실제 파일 저장
//            file.transferTo(dest);
//
//            // BinaryContent 생성
//            return new BinaryContent(
//                    UUID.randomUUID(),
//                    Instant.now(),
//                    originalFileName,
//                    filePath,
//                    fileType,
//                    fileSize
//            );
//        }
//
//        // 유저 삭제 시 폴더 통째로 삭제
//        public void deleteUserFolder(UUID userId) {
//            String userFolderPath = rootDir + "user_" + userId;
//            File userFolder = new File(userFolderPath);
//            if (userFolder.exists()) {
//                deleteDirectoryRecursively(userFolder);
//            }
//        }
//
//        private void deleteDirectoryRecursively(File file) {
//            if (file.isDirectory()) {
//                for (File child : file.listFiles()) {
//                    deleteDirectoryRecursively(child);
//                }
//            }
//            file.delete();
//        }
//    }

}
