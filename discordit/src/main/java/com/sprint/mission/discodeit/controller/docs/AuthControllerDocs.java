package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.entity.auth.request.UserLoginRequest;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import org.springframework.web.bind.annotation.RequestBody;

@Tag(name = "인증 관리", description = "사용자 인증 관련 API입니다.")
public interface AuthControllerDocs {
}