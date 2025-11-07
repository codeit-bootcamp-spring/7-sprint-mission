package com.sprint.mission.discodeit.service.basic;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentGetRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.common.exceptions.ContentNotFoundException;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.service.BinaryContentService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BasicBinaryContentService implements BinaryContentService {

    private final BinaryContentRepository contentRepository;
    private final UserRepository userRepository;

    public BinaryContentResponse create(BinaryContentCreateRequest dto) {
        BinaryContent content = new BinaryContent(
                dto.fileUrl()
        );
        contentRepository.save(content);
        return BinaryContentResponse.toDto(content);
    }

    @Override
    public BinaryContentResponse get(UUID id) {
        return BinaryContentResponse.toDto(
                contentRepository.findById(id)
                        .orElseThrow(() -> new ContentNotFoundException(id)));
    }

    public List<BinaryContentResponse> getAllById(BinaryContentGetRequest dto) {
        return dto.ids().stream()
                .map(id -> contentRepository.findById(id)
                        .orElseThrow(() -> new ContentNotFoundException(id)))
                .map(BinaryContentResponse::toDto)
                .toList();
    }

    public void delete(UUID uuid) {
        contentRepository.deleteByID(uuid);
    }
}
