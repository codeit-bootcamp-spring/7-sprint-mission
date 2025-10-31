package com.sprint.mission.discodeit.service.facade.user;


import com.sprint.mission.discodeit.service.BinaryContentService;
import com.sprint.mission.discodeit.service.UserService;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Service;

@Service
@RequiredArgsConstructor
public class UploadUserProfileFacade {
    private final UserService userService;
    private final BinaryContentService binaryContentService;
}
