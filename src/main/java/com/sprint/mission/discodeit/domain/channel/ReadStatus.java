package com.sprint.mission.discodeit.domain.channel;

import lombok.Getter;

import java.io.Serializable;
import java.time.Instant;

//용자가 채널 별 마지막으로 메시지를 읽은 시간을 표현. 사용자별 각 채널에 읽지 않은 메시지를 확인하기 위해 활용합니다.
@Getter
public class ReadStatus implements Serializable {
    
    private Instant lastReadAt;
    
    public ReadStatus() {
        this.lastReadAt=Instant.now();
    }
    
    public void checkChannel(){
        
    }
}
