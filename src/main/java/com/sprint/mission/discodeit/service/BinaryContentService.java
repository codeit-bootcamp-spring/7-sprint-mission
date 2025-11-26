package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.domain.BinaryContent;

import com.sprint.mission.discodeit.entity.BinaryContentEntity;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.mapper.BinaryContentMapper;
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
    private final BinaryContentMapper mapper;
    private final BinaryContentRepository binaryContentRepository;


    //우선은 파일 이름은 fileManager에서 UUID 랜덤으로 생성, 그래서 binaryContent의 아이디와는 다름
    public BinaryContent put(UUID userId, MultipartFile profile) {
        Path profilePath
                = fileManager.put(userId, profile);
        BinaryContent content = new BinaryContent("profile",
                profile.getContentType(),
                profilePath.toString(),
                profile.getSize()
        );
        BinaryContentEntity binaryContentEntity = mapper.toBinaryContentEntity(content);
        BinaryContentEntity savedEntity = binaryContentRepository.save(binaryContentEntity);

        return mapper.toBinaryContent(savedEntity);
    }


    public void deleteFile(UUID binaryContentId) {
        BinaryContentEntity binaryContentEntity = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new NoSuchElementException("해당 파일이 존재하지 않습니다."));
        binaryContentRepository.delete(binaryContentEntity);
        fileManager.delete(mapper.toBinaryContent(binaryContentEntity));
    }


    public List<BinaryContent> getBinaryContents(List<UUID> ids) {
        List<BinaryContent> result = new ArrayList<>();

        for (UUID id : ids) {
            BinaryContentEntity binaryContentEntity = binaryContentRepository.findById(id).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
            result.add(mapper.toBinaryContent(binaryContentEntity));
        }

        return result;
    }

    public BinaryContent getBinaryContent(UUID binaryContentId) {
        BinaryContentEntity binaryContentEntity = binaryContentRepository.findById(binaryContentId).orElseThrow(() -> new NoSuchElementException("파일이 존재하지 않습니다."));
        return mapper.toBinaryContent(binaryContentEntity);
    }


}



