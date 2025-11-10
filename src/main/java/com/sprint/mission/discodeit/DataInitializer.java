package com.sprint.mission.discodeit;


import com.sprint.mission.discodeit.application.BasicChannelService;
import com.sprint.mission.discodeit.application.BasicServerService;
import com.sprint.mission.discodeit.application.dto.request.ChannelCreateRequestDto;
import com.sprint.mission.discodeit.domain.Channel;
import com.sprint.mission.discodeit.domain.Server;
import com.sprint.mission.discodeit.domain.repository.ChannelRepository;
import com.sprint.mission.discodeit.domain.repository.ServerRepository;
import jakarta.annotation.PostConstruct;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.ArrayList;
import java.util.List;
import java.util.UUID;

@Component
@RequiredArgsConstructor
public class DataInitializer {

    @PostConstruct
    public void init(){



    }

    
}
