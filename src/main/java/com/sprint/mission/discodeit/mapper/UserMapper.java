package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.response.binaryContent.BinaryContentDto;
import com.sprint.mission.discodeit.dto.response.user.UserDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.repository.UserRepository;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.List;
import java.util.UUID;
import java.util.stream.Stream;

@Component
@RequiredArgsConstructor
public class UserMapper {
    private final BinaryContentMapper binaryContentMapper;
    private final UserRepository userRepository;


    public UserDto toDto(User user){
        BinaryContentDto binaryContentDto = user.getProfile()==null ?null:binaryContentMapper.toDto(user.getProfile());
        UUID binaryContentId = binaryContentDto==null?null:binaryContentDto.id();
        return new UserDto(
                user.getId(),
                user.getUserName(),
                user.getEmail(),
                binaryContentDto,
                binaryContentId,
                user.getUserStatus().isUserOnline()
        );
    }
    public List<UserDto> idsToDto(List<UUID> ids){
        return userRepository.findOneUser(ids).stream().map(this::toDto).toList();
    }
}
