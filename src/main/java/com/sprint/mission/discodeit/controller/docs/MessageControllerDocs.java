package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.message.request.MessageInfoReq;
import com.sprint.mission.discodeit.dto.message.response.MessageViewRes;
import com.sprint.mission.discodeit.exception.ErrorInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RequestPart;
import org.springframework.web.multipart.MultipartFile;

@Tag(name = "Message", description = "Message API")
public interface MessageControllerDocs {

  @Operation(summary = "Channel의 Message 목록 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "Message 목록 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageViewRes.class),
                  examples = @ExampleObject(
                      name = "Message 목록 조회 성공 예시",
                      value = """
                          [
                                {
                                    "messageId": "c031b401-47e7-4344-8bd3-a62b43c7539f",
                                    "speaker": "테스터3",
                                    "content": "이번엔 수정을 했습니다.",
                                    "attachmentDatas": [
                                        {
                                            "binaryContentId": "caeb7671-c6e1-4532-b50d-6eadc91bad37",
                                            "data": "iVBORw0K...중략...rduN5fXMkKFQ1wgAAAA==",
                                            "fileName": "cat.jpg",
                                            "fileType": "image/jpeg",
                                            "createdAt": "2025-11-15 20:48:19"
                                        }
                                    ],
                                    "createAt": "2025-11-15 20:13:42",
                                    "isModified": true
                                }
                            ]
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<List<MessageViewRes>> findAllByChannelId(@RequestParam UUID channelId);

  @Operation(summary = "Message 생성")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.CREATED,
              description = "Message가 성공적으로 생성됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageViewRes.class),
                  examples = @ExampleObject(
                      name = "Message가 성공적으로 생성됨 예시",
                      value = """
                          {
                                  "messageId": "c031b401-47e7-4344-8bd3-a62b43c7539f",
                                  "speaker": "테스터3",
                                  "content": "고양이 사진을 잔뜩~!넣었슴다",
                                  "attachmentDatas": [
                                      {
                                          "binaryContentId": "caeb7671-c6e1-4532-b50d-6eadc91bad37",
                                          "data": "iVBOR...중략....",
                                          "fileName": "D3343D40-5CF8-438E-92D0-4ED668688E43.jpg",
                                          "fileType": "image/jpeg",
                                          "createdAt": "2025-11-15 20:13:42"
                                      }
                                  ],
                                  "createAt": "2025-11-15 20:13:42",
                                  "isModified": false
                              }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "Channel를 찾을 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "Channel를 찾을 수 없음 예시",
                      value = """
                          {
                              "code": "CHANNEL_001",
                              "message": "해당 UUID를 가진 채널이 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<MessageViewRes> createMessage(
      @RequestHeader("X-LOGINUSER-ID") UUID speakerId,
      @RequestParam UUID channelId,
      @Valid @RequestPart("messageInfoReq") MessageInfoReq messageInfoReq,
      @RequestPart(value = "attachmentFiles", required = false) List<MultipartFile> attachmentFiles);

  @Operation(summary = "Message 내용 수정")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "Message가 성공적으로 수정됨",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = MessageViewRes.class),
                  examples = @ExampleObject(
                      name = "Message가 성공적으로 수정됨 예시",
                      value = """
                              {
                                  "messageId": "c031b401-47e7-4344-8bd3-a62b43c7539f",
                                  "speaker": "테스터3",
                                  "content": "이번엔 수정을 했습니다. 기존 첨부파일 2개중 1개를 삭제하고, 1개를 새로 업데이트 하였을 때, 실제 BinaryContentFile 이 교체 되는지 테스트",
                                  "attachmentDatas": [
                                      {
                                          "binaryContentId": "caeb7671-c6e1-4532-b50d-6eadc91bad37",
                                          "data": "iVBO...중략...A==",
                                          "fileName": "cat.jpg",
                                          "fileType": "image/jpeg",
                                          "createdAt": "2025-11-15 20:48:19"
                                      }
                                  ],
                                  "createAt": "2025-11-15 20:13:42",
                                  "isModified": true
                              }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "Message를 찾을 수 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "Message를 찾을 수 없음 예시",
                      value = """
                          {
                              "code": "MESSAGE_001",
                              "message": "해당 UUID를 가진 메세지가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<MessageViewRes> updateMessage(
      @PathVariable UUID messageId,
      @Valid @RequestPart("messageInfoReq") MessageInfoReq messageInfoReq,
      @RequestPart(value = "attachmentIds", required = false) List<UUID> keepAttachmentIds,
      @RequestPart(value = "attachmentFiles", required = false) List<MultipartFile> newAttachmentReqs);

  @Operation(summary = "Message 삭제")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NO_CONTENT,
              description = "Message가 성공적으로 삭제됨"
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 메세지가 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 메세지가 없음 예시",
                      value = """
                          {
                              "code": "MESSAGE_001",
                              "message": "해당 UUID를 가진 메세지가 존재하지 않습니다.",
                              "httpStatus": 404
                          }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<Void> deleteMessage(@PathVariable UUID messageId);
}
