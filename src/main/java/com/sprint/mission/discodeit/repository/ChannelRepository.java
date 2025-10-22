package com.sprint.mission.discodeit.repository;

import com.sprint.mission.discodeit.entity.Channel;
import java.util.List;

public interface ChannelRepository {
    void channelWrite(List<Channel> channel, String message);  // 생성                            // 삭제
}
