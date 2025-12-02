package com.sprint.mission.discodeit.controller.openapi;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequestDto;
import com.sprint.mission.discodeit.dto.message.response.MessageResponseDto;
import com.sprint.mission.discodeit.dto.page.Response.PageResponseDto;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.web.PageableDefault;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "메시지 관리(Message Controller)", description = "메시지 생성/수정/삭제/조회를 관리하는 API입니다.")
public interface MessageControllerDocs {

    @Operation(
            summary = "메시지 생성",
            description = """
                    새로운 메시지를 생성합니다.
                    
                    ## 요청 데이터(JSON)
                    - messageCreateRequest(`@RequestPart` 이름)
                        - content : 메시지 내용
                        - channelId : 채널 고유 식별자(UUID)
                        - authorId : 작성한 사용자 고유 식별자(UUID)
                        - 모든 정보는 필수 값입니다.
                    
                    ## 요청 데이터(Multipart File)
                    - attachments(`@RequestPart` 이름) : 파일 (선택 가능, 다중 등록 가능)
                    
                    ## 응답 데이터
                    - 성공 시 저장된 메시지 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "201",
                    description = "메시지 생성 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "authorId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                "channelId": "1bb1b6a9-6c1b-4534-b595-c14bdfc0ca86",
                                                "receiveType": "CHANNEL",
                                                "content": "대답.",
                                                "id": "4aa4f743-5c3d-408c-acf7-6f81a5579591",
                                                "createdAt": "2025-11-12T05:17:12.641949500Z",
                                                "updatedAt": "2025-11-12T05:17:12.644448800Z",
                                                "attachmentIds": [
                                                    "714f4003-2f62-4463-94eb-998dedcbb805",
                                                    "b1d7b1fd-e9fe-4b2a-a27a-576177b6129e"
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메시지 생성 요청시 지정한 사용자 또는 채널을 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "해당 채널을 찾을 수 없습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<MessageResponseDto> createMessage(CreateMessageRequestDto requestDto, List<MultipartFile> attachments);

    @Operation(
            summary = "메시지 수정",
            description = """
                    기존 메시지를 수정합니다.
                    
                    ## 요청 경로(Path Variable)
                    - messageId : 메시지 고유 식별자(UUID)
                    
                    ## 요청 데이터(JSON)
                    - newContent : 메시지 내용
                    
                    ## 응답 데이터
                    - 성공 시 수정된 메시지 정보가 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "메시지 수정 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Message.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "authorId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                "channelId": "1bb1b6a9-6c1b-4534-b595-c14bdfc0ca86",
                                                "receiveType": "CHANNEL",
                                                "content": "안녕하신가",
                                                "id": "857bd8be-5987-47d7-a162-5e21410ffc02",
                                                "createdAt": "2025-11-11T09:36:33.711636300Z",
                                                "updatedAt": "2025-11-12T05:25:18.963685700Z",
                                                "attachmentIds": [
                                                    "6ddc517b-e6ff-462d-94e8-85730be2fadc"
                                                ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메시지 수정 요청시 지정한 메시지를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "메시지가 존재하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<MessageResponseDto> updateMessage(UUID messageId, UpdateMessageRequestDto requestDto);

    @Operation(
            summary = "메시지 삭제",
            description = """
                    기존 메시지를 삭제합니다.
                    
                    ## 요청데이터(Path Variable)
                    - messageId : 메시지 고유 식별자(UUID)
                    
                    ## 응답데이터
                    - 응답 본문은 없습니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "메시지 정보 삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = Void.class)
                    )
            ),
            @ApiResponse(
                    responseCode = "404",
                    description = "메시지 삭제 요청시 지정한 메시지를 찾을 수 없음",
                    content = @Content(
                            mediaType = "application/json",
                            schema = @Schema(implementation = com.sprint.mission.discodeit.global.dto.ApiResponse.class),
                            examples = @ExampleObject(
                                    value = """
                                            {
                                                "status": "NOT_FOUND",
                                                "message": "메시지가 존재하지 않습니다.",
                                                "data": null
                                            }
                                            """
                            )
                    )
            )
    })
    ResponseEntity<Void> deleteMessage(UUID messageId);

    @Operation(
            summary = "채널 메시지 목록 조회",
            description = """
                    특정 채널의 메시지 목록을 조회합니다.
                    
                    ## 요청 데이터((Request Param)
                    - channelId : 채널 고유 식별자(UUID)
                    
                    ## 응답 데이터
                    - 성공 시 메시지 정보를 리스트로 반환합니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "메시지 목록 조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(
                                    schema = @Schema(implementation = Message.class)
                            ),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                                {
                                                    "authorId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                    "channelId": "1bb1b6a9-6c1b-4534-b595-c14bdfc0ca86",
                                                    "receiveType": "CHANNEL",
                                                    "content": "안녕하신가",
                                                    "id": "857bd8be-5987-47d7-a162-5e21410ffc02",
                                                    "createdAt": "2025-11-11T09:36:33.711636300Z",
                                                    "updatedAt": "2025-11-12T05:25:18.963685700Z",
                                                    "attachmentIds": [
                                                        "6ddc517b-e6ff-462d-94e8-85730be2fadc"
                                                    ]
                                                },
                                                {
                                                    "authorId": "4f5f8a05-fdbc-41ad-b736-e6a2695e4e6d",
                                                    "channelId": "1bb1b6a9-6c1b-4534-b595-c14bdfc0ca86",
                                                    "receiveType": "CHANNEL",
                                                    "content": "안녕하세요",
                                                    "id": "dab051f5-9c8c-40db-9db1-b58cb243df8f",
                                                    "createdAt": "2025-11-11T09:59:17.245308600Z",
                                                    "updatedAt": "2025-11-11T09:59:17.247309Z",
                                                    "attachmentIds": [
                                                        "d942a7cd-e57c-4cb8-b367-279bd7e8815e",
                                                        "6e1a075f-9946-47b0-b6f7-d52e29e20511"
                                                    ]
                                                }
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<PageResponseDto<MessageResponseDto>> searchMessage(
            @RequestParam UUID channelId,
            @PageableDefault(size = 50, sort = "createdAt", direction = Sort.Direction.DESC)
            Pageable pageable
    );
}
