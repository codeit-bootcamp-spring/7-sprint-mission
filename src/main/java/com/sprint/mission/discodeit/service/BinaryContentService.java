package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;
import com.sprint.mission.discodeit.domain.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.dto.response.BinaryContentDto;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.nio.file.*;
import java.util.ArrayList;
import java.util.List;
import java.util.NoSuchElementException;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public final class BinaryContentService {


    private final FileManager fileManager;
    private final BinaryContentRepository binaryContentRepository;



    public BinaryContent setUserProfile(String userId, MultipartFile profile) {

        Path profilePath
                = fileManager.saveUserProfile(userId, profile);
        BinaryContent content = new BinaryContent("profile",
                profile.getContentType(),
                profilePath.toString(),
                profile.getSize()
        );
        return binaryContentRepository.save(content);
    }

    public BinaryContent saveMessageFile(String userId, MultipartFile file) {

        Path filePath = fileManager.saveMessageFile(userId, file);
        String fileName = makeFileName(file.getOriginalFilename() == null ? "" : file.getOriginalFilename());

        BinaryContent content = new BinaryContent(
                fileName,
                file.getContentType(),
                filePath.toString(),
                file.getSize()
        );
        return binaryContentRepository.save(content);
    }

    public void deleteMessageImage(String binaryContentId) {
        BinaryContent content = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new NoSuchElementException("해당 파일이 존재하지 않습니다."));
        fileManager.deleteMessageImage(content);
    }

    public void deleteUserFolder(String userId) {
        fileManager.deleteUserFolder(userId);
    }

    public List<BinaryContentDto> getBinaryContents(List<String> ids) {
        List<BinaryContentDto> result = new ArrayList<>();

        for (String id : ids) {
            BinaryContent content = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
            result.add(BinaryContentDto.from(content));
        }

        return result;
    }

    public BinaryContentDto getBinaryContent(String id) {
        BinaryContent content = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
        return BinaryContentDto.from(content);
    }


    private String makeProfileName(String originalName) {
        int pos = originalName.lastIndexOf(".");
        String ext = originalName.substring(pos + 1);
        return "profile" + "." + ext;
    }
    private String makeFileName(String originalName) {
        int pos = originalName.lastIndexOf(".");
        String Ext = originalName.substring(pos + 1);
        String uuid = UUID.randomUUID().toString();
        return "message_"+uuid + "." + Ext;
    }


}



