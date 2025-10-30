package com.sprint.mission.discodeit.service;

import com.sprint.mission.discodeit.dto.binaryContent.request.BinaryContentCreateRequest;
import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponse;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.UUID;

@Service
@RequiredArgsConstructor
public class BinaryContentService {

    private final BinaryContentRepository contentRepository;
    private final UserRepository userRepository;

    public BinaryContentResponse create(BinaryContentCreateRequest dto) {
        BinaryContent content = new BinaryContent(
                userRepository.findByUserId(dto.userId()),
                dto.fileUrl()
        );
        contentRepository.save(content);
        return BinaryContentResponse.toDto(content);
    }

    public BinaryContentResponse get(UUID uuid) {
        return BinaryContentResponse.toDto(contentRepository.findById(uuid));
    }

    public List<BinaryContentResponse> getAllByUserID(String userId) {
        return contentRepository.findAllByUser(userRepository.findByUserId(userId)).stream()
                .map(BinaryContentResponse::toDto)
                .toList();
    }

    public void delete(UUID uuid) {
        contentRepository.deleteByID(uuid);
    }
}
