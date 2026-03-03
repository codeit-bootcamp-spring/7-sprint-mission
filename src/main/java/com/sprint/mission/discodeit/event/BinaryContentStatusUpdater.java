package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.transaction.annotation.Propagation;
import org.springframework.transaction.annotation.Transactional;

import java.util.UUID;

@Component
@RequiredArgsConstructor
public class BinaryContentStatusUpdater {

    private final BinaryContentRepository binaryContentRepository;

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markSuccess(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow();
        binaryContent.markSuccess();
    }

    @Transactional(propagation = Propagation.REQUIRES_NEW)
    public void markFail(UUID binaryContentId) {
        BinaryContent binaryContent = binaryContentRepository.findById(binaryContentId).orElseThrow();
        binaryContent.markFail();
    }
}