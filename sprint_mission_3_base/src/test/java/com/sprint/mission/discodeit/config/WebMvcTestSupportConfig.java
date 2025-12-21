package com.sprint.mission.discodeit.config;

import com.sprint.mission.discodeit.exception.ErrorCodeStatusMapper;
import org.springframework.boot.test.context.TestConfiguration;
import org.springframework.context.annotation.Import;

@TestConfiguration
@Import(ErrorCodeStatusMapper.class)
public class WebMvcTestSupportConfig {
}
