package com.sprint.mission.discodeit.constants;

import lombok.Getter;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
@Getter
public class DataPath {

    @Value("${data.path.project-dir}")
    private String projectDir;

    @Value("${data.path.base-dir}")
    private String baseDir;

    @Value("${data.path.file.channel}")
    private String channelFilePath;

    @Value("${data.path.file.user}")
    private String userFilePath;

    @Value("${data.path.file.message}")
    private String messageFilePath;

    @Value("${data.path.file.binary-content}")
    private String binaryContentPath;

    @Value("${data.path.file.read-status}")
    private String readStatusFilePath;

    @Value("${data.path.file.user-status}")
    private String userStatusFilePath;
}