package com.sprint.mission.discodeit.common.constants;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

@Component
public record DataPath (
    String FILE_ROOT_PATH
){
    public DataPath(@Value("${discodeit.storage.local.root-path}") String FILE_ROOT_PATH) {
        this.FILE_ROOT_PATH = FILE_ROOT_PATH;
    }
}