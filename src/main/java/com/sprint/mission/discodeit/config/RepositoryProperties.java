package com.sprint.mission.discodeit.config;

import lombok.Getter;
import lombok.Setter;
import org.springframework.boot.context.properties.ConfigurationProperties;
import org.springframework.stereotype.Component;


@ConfigurationProperties(prefix = "discodeit.repository")
@Getter
@Setter
@Component
public class RepositoryProperties {
    private String type = "jcf";  // 기본값: jcf
    private String fileDirectory = ".discodeit";  // 기본값: .discodeit
}