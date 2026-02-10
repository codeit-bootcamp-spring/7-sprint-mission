package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.mapper.mapStruct.BinaryStruct;
import com.sprint.mission.discodeit.repository.UserRepository;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import com.sprint.mission.discodeit.security.jwt.JwtRegistry;
import lombok.RequiredArgsConstructor;
import org.springframework.security.core.session.SessionRegistry;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.Optional;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final UserRepository userRepository;
    private final JwtRegistry jwtRegistry;

    public UserDto toDto(User user){
        BinaryContentDto binaryContentDto = user.getProfile()==null ?null:BinaryStruct.INSTANCE.toDto(user.getProfile());
        UUID binaryContentId = binaryContentDto==null?null:binaryContentDto.id();
        boolean online = isUserOnline(user.getId());
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                binaryContentDto,
                binaryContentId,
                online,
                user.getRole()
        );
    }
    public List<UserDto> idsToDto(List<UUID> ids){
        return userRepository.findOneUser(ids).stream().map(this::toDto).toList();
    }

    private boolean isUserOnline(UUID userId) {

        return jwtRegistry.hasActiveJwtInformationByUserId(userId);

    }
}
