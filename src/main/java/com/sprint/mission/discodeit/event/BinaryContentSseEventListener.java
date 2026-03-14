package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.dto.binaryContent.BinaryContentResponseDto;
import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.SseService;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.event.EventListener;
import org.springframework.scheduling.annotation.Async;
import org.springframework.stereotype.Component;

import java.util.List;

@Component
@RequiredArgsConstructor
@Slf4j
public class BinaryContentSseEventListener {
    private final BinaryContentService binaryContentService;
    private final SseService sseService;

    @Async("binaryExecutor")
    @EventListener
    public void handleUpdatedBinaryContent(BinaryContentUpdatedEvent event) {
        BinaryContentResponseDto responseDto =
                binaryContentService.getBinaryContent(event.getBinaryId());

        sseService.send(
                List.of(event.getUserId()),
                "binaryContents.updated",
                responseDto
        );
    }
}
