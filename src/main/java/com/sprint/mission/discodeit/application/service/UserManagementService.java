package com.sprint.mission.discodeit.application.service;

import com.sprint.mission.discodeit.application.dto.UserDetailInfoDTO;
import com.sprint.mission.discodeit.user.dto.UserRequestDTO;
import com.sprint.mission.discodeit.user.dto.UserResponseDTO;
import org.springframework.web.multipart.MultipartFile;

import java.util.UUID;

public interface UserManagementService {
    UserResponseDTO createUserWithRelatedData(UserRequestDTO requestDTO, MultipartFile multipartFile);
    void deleteUserWithRelatedData(UUID userId);
    UserDetailInfoDTO getUserDetailInfo(UUID userId);
}
