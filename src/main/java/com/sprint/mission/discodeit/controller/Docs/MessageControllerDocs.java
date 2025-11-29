package com.sprint.mission.discodeit.controller.Docs;

import com.sprint.mission.discodeit.dto.message.request.CreateMessageRequest;
import com.sprint.mission.discodeit.dto.message.request.UpdateMessageRequest;
import com.sprint.mission.discodeit.dto.message.response.MessageDto;
import com.sprint.mission.discodeit.entity.Message;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.ArraySchema;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "메시지 관리 (MessageController)", description = "메시지 생성, 수정, 삭제, 채널별 메시지 조회")
public interface MessageControllerDocs {

    @Operation(
            summary = "메시지 생성",
            description = """
                    새로운 메시지를 생성합니다.
                    
                    **요청 데이터**
                    - content (메시지 본문)
                    - channelId (메시지가 속한 채널)
                    - profiles (첨부 이미지/파일, 선택)
                    
                    **응답**
                    - 생성된 메시지 정보가 반환됩니다.
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
                                              "id": "c8c61e02-5b6a-4b13-a8f2-47f7ef78f032",
                                              "createdAt": "2025-11-12T09:45:00.000Z",
                                              "updatedAt": null,
                                              "userId": "f1a4bdee-7c53-4e8c-9127-13f51e3a93ce",
                                              "channelId": "c77f14de-6e23-4221-bdd2-f9a8bc71fca3",
                                              "content": "안녕하세요, 새 메시지입니다!",
                                              "attachments": [
                                                "profile1.png",
                                                "profile2.jpg"
                                              ]
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (필드 누락, 형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "content는 필수입니다.",
                                              "존재하지 않는 channelId입니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<MessageDto> createMessage(CreateMessageRequest request,
                                             List<MultipartFile> profiles);


    @Operation(
            summary = "메시지 수정",
            description = """
                    기존 메시지를 수정합니다.
                    
                    **요청 데이터**
                    - messageId (Path 또는 Query Param)
                    - content (수정할 본문)
                    
                    **응답**
                    - 수정된 메시지 정보가 반환됩니다.
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
                                              "id": "c8c61e02-5b6a-4b13-a8f2-47f7ef78f032",
                                              "createdAt": "2025-11-12T09:45:00.000Z",
                                              "updatedAt": "2025-11-12T10:00:15.000Z",
                                              "userId": "f1a4bdee-7c53-4e8c-9127-13f51e3a93ce",
                                              "channelId": "c77f14de-6e23-4221-bdd2-f9a8bc71fca3",
                                              "content": "수정된 메시지 내용입니다!",
                                              "attachments": []
                                            }
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (존재하지 않는 ID, 형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              "해당 messageId를 찾을 수 없습니다.",
                                              "수정할 content가 비어 있습니다."
                                            ]
                                            """
                            )
                    )
            )
    })
    ResponseEntity<MessageDto> updateMessage(UUID messageId,
                                             UpdateMessageRequest request);


    @Operation(
            summary = "메시지 삭제",
            description = """
                    메시지를 삭제합니다.
                    
                    **요청 데이터**
                    - messageId (삭제할 메시지 UUID)
                    
                    **응답**
                    - 성공 시 내용 없는 204 응답 반환
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "204",
                    description = "삭제 성공",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "삭제 완료 (본문 없음)")
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "존재하지 않는 messageId입니다.")
                    )
            )
    })
    ResponseEntity<Void> deleteMessage(UUID messageId);


    @Operation(
            summary = "채널별 메시지 전체 조회",
            description = """
                    특정 채널(channelId)에 속한 모든 메시지를 조회합니다.
                    
                    **요청 데이터**
                    - channelId (Query Param)
                    
                    **응답**
                    - 메시지 배열(List<Message>)이 반환됩니다.
                    """
    )
    @ApiResponses(value = {
            @ApiResponse(
                    responseCode = "200",
                    description = "조회 성공",
                    content = @Content(
                            mediaType = "application/json",
                            array = @ArraySchema(schema = @Schema(implementation = Message.class)),
                            examples = @ExampleObject(
                                    value = """
                                            [
                                              {
                                                "id": "b1f9a8b2-3d0f-4a1b-8a2f-7c0b2f2c9a11",
                                                "createdAt": "2025-11-12T09:00:00.000Z",
                                                "userId": "6f01be3c-e8f9-4597-a5b1-02e348db7d36",
                                                "channelId": "c77f14de-6e23-4221-bdd2-f9a8bc71fca3",
                                                "content": "첫 번째 메시지!",
                                                "attachments": []
                                              },
                                              {
                                                "id": "b2f9a8b2-3d0f-4a1b-8a2f-7c0b2f2c9a22",
                                                "createdAt": "2025-11-12T09:10:00.000Z",
                                                "userId": "f1a4bdee-7c53-4e8c-9127-13f51e3a93ce",
                                                "channelId": "c77f14de-6e23-4221-bdd2-f9a8bc71fca3",
                                                "content": "두 번째 메시지입니다!",
                                                "attachments": ["file.png"]
                                              }
                                            ]
                                            """
                            )
                    )
            ),
            @ApiResponse(
                    responseCode = "400",
                    description = "잘못된 요청 (UUID 형식 오류 등)",
                    content = @Content(
                            mediaType = "application/json",
                            examples = @ExampleObject(value = "channelId 형식이 잘못되었습니다.")
                    )
            )
    })
    ResponseEntity<List<MessageDto>> findAllByChannelId(UUID channelId, int page);
}
