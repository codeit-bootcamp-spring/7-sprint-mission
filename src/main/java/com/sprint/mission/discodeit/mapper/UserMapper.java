package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.user.UserResponseDto;
import com.sprint.mission.discodeit.entity.User;
import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
import org.mapstruct.Context;
import org.mapstruct.Mapper;
import org.mapstruct.Mapping;
import org.mapstruct.Named;
import org.springframework.security.core.session.SessionRegistry;

@Mapper( // NOTE: 스프링빈 등록, @Component 불필요
        componentModel = "spring",
        uses = { BinaryContentMapper.class}
)
public interface UserMapper {

    @Mapping(target = "profile", source = "profile")
    @Mapping(target = "online", source = "user", qualifiedByName = "mapOnline")
    UserResponseDto toDto(User user, @Context SessionRegistry sessionRegistry);


    @Named("mapOnline")
    default boolean mapOnline(User user, @Context SessionRegistry sessionRegistry) {
        if (user == null) return false;

        for (Object principal : sessionRegistry.getAllPrincipals()) {
            if (principal instanceof DiscodeitUserDetails dud
                    && dud.getUserResponseDto().id().equals(user.getId())) {
                return !sessionRegistry.getAllSessions(principal, false).isEmpty();
            }
        }
        return false;
    }
}
