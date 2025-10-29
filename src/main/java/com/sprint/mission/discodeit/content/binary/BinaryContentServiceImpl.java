package com.sprint.mission.discodeit.content.binary;

import com.sprint.mission.discodeit.common.service.impl.BaseServiceImpl;
import com.sprint.mission.discodeit.config.enums.ContentOwner;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.List;
import java.util.UUID;

public class BinaryContentServiceImpl extends BaseServiceImpl<BinaryContent, UUID, BinaryContentRepository> implements BinaryContentService {
    public BinaryContentServiceImpl(BinaryContentRepository repository) {
        super(repository);
    }

    @Override
    public BinaryContentDTO uploadFile(UUID ownerId, ContentOwner owner, MultipartFile file) throws IOException {
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
            return BinaryContentDTO.from(save(content));
        } catch (IOException e){
            throw new IOException("파일 저장에 실패했습니다.",e);
        }

    }

    @Override
    public List<BinaryContentDTO> findAllByOwnerId(UUID ownerId) {
        return repository.findAllByOwnerId(ownerId).stream().map(BinaryContentDTO::from).toList();
    }

    @Override
    public List<BinaryContentDTO> findAllByFilePath(String filePath) {
        return repository.findAllByFilePath(filePath).stream().map(BinaryContentDTO::from).toList();
    }

    @Override
    public void deleteAllByOwnerId(UUID ownerId) {
        repository.deleteAllByOwnerId(ownerId);
    }
}
