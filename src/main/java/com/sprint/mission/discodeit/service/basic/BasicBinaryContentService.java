package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.global.exception.custom.CustomException;
import com.sprint.mission.discodeit.global.exception.custom.ErrorCode;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.List;
import java.util.UUID;
import java.util.stream.Collectors;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {
    private final BinaryContentRepository binaryContentRepository;

    @Override
    public void create(CreateBinaryContentRequestDto request) {
        MultipartFile file = request.getFile();

        if(file != null && !file.isEmpty()){
            try {
                BinaryContent bc = new BinaryContent(file.getName(), file.getBytes());
                binaryContentRepository.save(bc);
            } catch (IOException e) {
                throw new CustomException(ErrorCode.FILE_UPLOAD_FAILED);
            }
        }
    }

    @Override
    public BinaryContent find(UUID binaryContentId) {
        return binaryContentRepository.findById(binaryContentId)
                .orElseThrow(() -> new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUND));
    }

    @Override
    public List<BinaryContent> findAllByIdIn(List<UUID> binaryContentIds) {
        return binaryContentRepository.findAll().stream().
                filter(bc -> binaryContentIds.contains(bc.getId()))
                .collect(Collectors.toList());
    }

    @Override
    public void delete(UUID binaryContentId) {
        if(!binaryContentRepository.isExist(binaryContentId)){
            throw new CustomException(ErrorCode.BINARYCONTENT_NOT_FOUND);
        }
        binaryContentRepository.delete(binaryContentId);
    }
}
