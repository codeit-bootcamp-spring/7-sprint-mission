package com.sprint.mission.discodeit.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record DataPath (
    String CHANNEL_FILE_PATH,
    String USER_FILE_PATH,
    String MESSAGE_FILE_PATH,
    String BINARY_CONTENT_FILE_PATH,
    String READ_STATUS_FILE_PATH,
    String USER_STATUS_FILE_PATH
){
    public DataPath(
        @Value("${data.path.file.channel}") String CHANNEL_FILE_PATH,
        @Value("${data.path.file.user}") String USER_FILE_PATH,
        @Value("${data.path.file.message}") String MESSAGE_FILE_PATH,
        @Value("${data.path.file.binary-content}") String BINARY_CONTENT_FILE_PATH,
        @Value("${data.path.file.read-status}") String READ_STATUS_FILE_PATH,
        @Value("${data.path.file.user-status}") String USER_STATUS_FILE_PATH
    ) {
        this.CHANNEL_FILE_PATH = CHANNEL_FILE_PATH;
        this.USER_FILE_PATH = USER_FILE_PATH;
        this.MESSAGE_FILE_PATH = MESSAGE_FILE_PATH;
        this.BINARY_CONTENT_FILE_PATH = BINARY_CONTENT_FILE_PATH;
        this.READ_STATUS_FILE_PATH = READ_STATUS_FILE_PATH;
        this.USER_STATUS_FILE_PATH = USER_STATUS_FILE_PATH;
    }
}