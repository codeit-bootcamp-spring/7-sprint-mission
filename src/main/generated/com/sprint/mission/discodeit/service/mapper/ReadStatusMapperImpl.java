package com.sprint.mission.discodeit.service.mapper;

import com.sprint.mission.discodeit.entity.Channel;
import com.sprint.mission.discodeit.entity.ReadStatus;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.service.dto.response.ReadStatusDto;
import java.util.UUID;
import javax.annotation.processing.Generated;
import org.springframework.stereotype.Component;

@Generated(
    value = "org.mapstruct.ap.MappingProcessor",
    date = "2026-02-25T16:24:39+0900",
    comments = "version: 1.6.3, compiler: javac, environment: Java 17.0.16 (Eclipse Adoptium)"
)
@Component
public class ReadStatusMapperImpl implements ReadStatusMapper {

    @Override
    public ReadStatusDto toDto(ReadStatus entity) {
        if ( entity == null ) {
            return null;
        }

        ReadStatusDto readStatusDto = new ReadStatusDto();

        readStatusDto.setUserId( entityUserId( entity ) );
        readStatusDto.setChannelId( entityChannelId( entity ) );
        readStatusDto.setId( entity.getId() );
        readStatusDto.setLastReadAt( entity.getLastReadAt() );
        readStatusDto.setNotificationEnabled( entity.isNotificationEnabled() );

        return readStatusDto;
    }

    private UUID entityUserId(ReadStatus readStatus) {
        User user = readStatus.getUser();
        if ( user == null ) {
            return null;
        }
        return user.getId();
    }

    private UUID entityChannelId(ReadStatus readStatus) {
        Channel channel = readStatus.getChannel();
        if ( channel == null ) {
            return null;
        }
        return channel.getId();
    }
}
