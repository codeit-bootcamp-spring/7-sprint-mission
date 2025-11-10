package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.SimpleChannel;
import com.sprint.mission.discodeit.application.dto.UserDetailInfo;
import com.sprint.mission.discodeit.content.binary.BinaryContentResponse;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

public interface UserManagementService {
    UserResponseDTO createUserWithRelatedData(UserRequestDTO requestDTO, MultipartFile multipartFile);
    void deleteUserWithRelatedData(UUID userId);
    UserDetailInfo getUserDetailInfo(UUID userId);
    List<SimpleChannel> getSimpleChannels(UUID userId);
    BinaryContentResponse updateProfileImg(UUID userId, MultipartFile multipartFile);
}
