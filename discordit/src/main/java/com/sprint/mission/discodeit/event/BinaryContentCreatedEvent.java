package com.sprint.mission.discodeit.event;

import com.sprint.mission.discodeit.entity.BinaryContent;
import lombok.AllArgsConstructor;
import lombok.extern.slf4j.Slf4j;

@AllArgsConstructor
@Slf4j
public class BinaryContentCreatedEvent {
    BinaryContent content;
    byte[] file;
}
