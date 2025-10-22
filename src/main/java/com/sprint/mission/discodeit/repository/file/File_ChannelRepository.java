package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.File_Common;
import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.repository.ChannelRepository;
import com.sprint.mission.discodeit.service.file.File_ChannelService;
import java.util.List;

public class File_ChannelRepository implements ChannelRepository {
    @Override
    public void channelWrite(List<Channel> channelList, String message) {
        File_Common.fileWrite(channelList, File_ChannelService.FILE_PATH, message);
    }
}
