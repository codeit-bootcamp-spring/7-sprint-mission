package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.request.message.MessageCreateRequestDto;
import com.sprint.mission.discodeit.dto.request.message.MessagePatchRequestDto;
import com.sprint.mission.discodeit.dto.response.MessageReadResponseDto;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import io.swagger.v3.oas.models.examples.Example;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;
@Tag(name = "메시지 관리 API(Message Controller)",description = "메세지를 조회 생성 관리 하는 메세지 Controller입니다. ")
public interface MessageControllerDocs {
    @Operation(summary = "메세지 생성(Create Message)", description = "메세지를 생성합니다.")
    @ApiResponse(responseCode = "200", description = "메세지 생성 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageReadResponseDto.class),
            examples = @ExampleObject(value = """
                    { "id":"550e8400-e29b-41d4-a716-446655440000",
                    "createdAt":"2025-11-13T10:30:00Z",
                    "updatedAt":"2025-11-13T10:30:00Z",
                    "content":"T1 is world champion",
                    "authorId": "550e8400-e29b-41d4-a716-446655440000",
                    "isMarkDown": true,
                    "channelId": "550e8400-e29b-41d4-a716-446655440000",
                    "attachmentIds":[
                    "550e8400-e29b-41d4-a716-446655440000","a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412"]
                    }
                    """)
    )
    )
    @RequestMapping(value = "", method = RequestMethod.POST,consumes = MediaType.MULTIPART_FORM_DATA_VALUE)
    ResponseEntity<MessageReadResponseDto> createMessage(@RequestPart("messageCreateRequest") MessageCreateRequestDto dto,
                                                         @RequestPart(value = "attachments", required = false) List<MultipartFile> attachments
    );

    @Operation(summary = "메세지 변경(Patch Message)", description = "메세지를 변경합니다.")
    @ApiResponse(responseCode = "200", description = "메세지 변경 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = MessageReadResponseDto.class),
    examples = @ExampleObject(value = """
           {"id":"550e8400-e29b-41d4-a716-446655440000",
                    "createdAt":"2025-11-13T10:30:00Z",
                    "updatedAt":"2025-11-13T10:30:00Z",
                    "content":"T1 is world champion",
                    "authorId": "550e8400-e29b-41d4-a716-446655440000",
                    "isMarkDown": true,
                    "channelId": "550e8400-e29b-41d4-a716-446655440000",
                    "attachmentIds":[
                    "550e8400-e29b-41d4-a716-446655440000","a9f3e1f1-22c3-4e50-9b12-38d5a0c0f412"]
                    }
           """)
    )
    )
    @RequestMapping(value = "/{messageId}", method = RequestMethod.PATCH)
    ResponseEntity<MessageReadResponseDto> patchMessage(@PathVariable UUID messageId, @RequestBody MessagePatchRequestDto dto);

    @Operation(summary = "메세지 삭제(Delete Message)", description = "메세지를 삭제합니다.")
    @ApiResponse(responseCode = "204", description = "메세지 삭제 성공",
    content = @Content(mediaType = "String", examples = @ExampleObject(value = """
            아무것도 반환하지 않습니다. 메세지가 삭제되면 메세지를 가지고 있던 채널들 파일에서 사라집니다.
            """))
    )
    @RequestMapping(value = "/{messageId}",method = RequestMethod.DELETE)
    void deleteMessage(@PathVariable UUID messageId);


    @RequestMapping(value = "/reset",method = RequestMethod.GET)
    void reset();

    @RequestMapping(value = "", method = RequestMethod.OPTIONS)
    ResponseEntity<List<MessageReadResponseDto>> readAll();
}
