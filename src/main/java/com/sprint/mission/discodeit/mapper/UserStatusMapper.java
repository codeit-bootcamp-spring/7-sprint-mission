//package com.sprint.mission.discodeit.mapper;
//
//import com.sprint.mission.discodeit.entity.User;
//import com.sprint.mission.discodeit.mapper.dto.UserStatusDto;
//import com.sprint.mission.discodeit.entity.UserStatus;
//import com.sprint.mission.discodeit.security.DiscodeitUserDetails;
//import java.util.UUID;
//import org.springframework.security.core.session.SessionInformation;
//import org.springframework.stereotype.Component;
//
//@Component
//public class UserStatusMapper {
////    public UserStatusDto toDto(UserStatus userStatus) {
////        if (null == userStatus) {
////            return null;
////        }
////
////        return UserStatusDto.builder()
////            .id(userStatus.getId())
////            .userId(userStatus.getUser().getId())
////            .lastActiveAt(userStatus.getLastActiveAt())
////            .build();
////    }
//    public UserStatusDto from(SessionInformation sessionInformation) {
//        DiscodeitUserDetails userDetails =
//            (DiscodeitUserDetails) sessionInformation.getPrincipal();
//
//        return UserStatusDto.builder()
//            .id(UUID.fromString(sessionInformation.getSessionId())) //??
//            .userId(userDetails.getUser().id())
//            .lastActiveAt(sessionInformation.getLastRequest().toInstant())
//            .build();
//    }
//}
