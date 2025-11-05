package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.enums.ContentOwner;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

@Service
public class BinaryContentServiceImpl extends BaseServiceImpl<BinaryContent, UUID, BinaryContentRepository> implements BinaryContentService {
    public BinaryContentServiceImpl(BinaryContentRepository repository) {
        super(repository);
    }

    @Override
    public BinaryContentResponseDTO uploadFile(UUID ownerId, ContentOwner owner, MultipartFile file) throws IOException {
        if(file.isEmpty()){
            throw new IllegalArgumentException("파일이 비어있습니다.");
        }
        String fileName = file.getOriginalFilename();
        String fileType = file.getContentType();
        Long fileSize = file.getSize();
        BinaryContent content = BinaryContent.create(ownerId, owner, fileName, fileType, fileSize);

        String fileDirectoryPath = owner.getOwnerSpecificPath(ownerId);

        String storedFileName = content.getId().toString();
        Path fullStoragePath = Paths.get(fileDirectoryPath + storedFileName);

        try {
            Files.createDirectories(fullStoragePath.getParent());
            file.transferTo(fullStoragePath);

            content.updateStoragePath(fullStoragePath.toString());
            return BinaryContentResponseDTO.from(save(content));
        } catch (IOException e){
            throw new IOException("파일 저장에 실패했습니다.",e);
        }

    }

    @Override
    public List<BinaryContentResponseDTO> findAllByOwnerId(UUID ownerId) {
        return repository.findAllByOwnerId(ownerId).stream().map(BinaryContentResponseDTO::from).toList();
    }

    @Override
    public List<BinaryContentResponseDTO> findAllByFilePath(String filePath) {
        return repository.findAllByFilePath(filePath).stream().map(BinaryContentResponseDTO::from).toList();
    }

    @Override
    public void deleteAllByOwnerId(UUID ownerId) {
        List<BinaryContent> contentsToDelete = repository.findAllByOwnerId(ownerId);

        for(BinaryContent content : contentsToDelete){
            String strFilePath = content.getFilePath();
            if (strFilePath != null && !strFilePath.isBlank()) {
                Path filePath = Path.of(strFilePath);
                try {
                    if( Files.deleteIfExists(filePath)){
                        System.out.println("✅ 물리적 파일 삭제 성공: " + strFilePath);
                        repository.deleteById(content.getId());
                    }else{
                        System.out.println("⚠️ 물리적 파일을 찾을 수 없거나 삭제할 수 없습니다: " + strFilePath);
                    }
                } catch (IOException e) {
                   throw new RuntimeException("❌ 데이터 삭제 실패");
                }
            }else{
                System.out.println("⚠️ 해당 경로가 존재하지 않습니다:" + strFilePath);
            }
        }
    }
}
