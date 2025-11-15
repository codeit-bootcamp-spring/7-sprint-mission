package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.exception.ErrorInfoRes;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import java.util.List;
import java.util.UUID;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@Tag(name = "BinaryContent", description = "첨부 파일 API")
public interface BinaryContentControllerDocs {

  @Operation(summary = "여러 첨부 파일 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "첨부 파일 목록 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BinaryContentInfoRes.class),
                  examples = @ExampleObject(
                      name = "첨부 파일 목록 조회 성공예시",
                      value = """
                          [
                                {
                                    "binaryContentId": "36150169-ad9a-40bb-9a15-ad3b6512825f",
                                    "data": "UklGR...중략...AAA==",
                                    "fileName": "cat.jpg",
                                    "fileType": "image/jpeg",
                                    "createdAt": "2025-11-15 20:46:38"
                                },
                                {
                                    "binaryContentId": "57c00f13-33c2-4dec-8633-3e2c77c59b7c",
                                    "data": "Ukl...중략...AAA==",
                                    "fileName": "cat.jpg",
                                    "fileType": "image/jpeg",
                                    "createdAt": "2025-11-15 20:48:19"
                                }
                            ]
                          """
                  )
              )
          )
      }
  )
  ResponseEntity<List<BinaryContentInfoRes>> getAllFilesInfo(
      @RequestParam List<UUID> binaryContentIdList);

  @Operation(summary = "첨부 파일 조회")
  @ApiResponses(
      value = {
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.OK,
              description = "첨부 파일 조회 성공",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = BinaryContentInfoRes.class),
                  examples = @ExampleObject(
                      name = "첨부 파일 조회 성공 예시",
                      value = """
                          {
                              "binaryContentId": "36150169-ad9a-40bb-9a15-ad3b6512825f",
                              "data": "UklGR...중략...AAA==",
                              "fileName": "cat.jpg",
                              "fileType": "image/jpeg",
                              "createdAt": "2025-11-15 20:46:38"
                          }
                          """
                  )
              )
          ),
          @io.swagger.v3.oas.annotations.responses.ApiResponse(
              responseCode = ResponseCode.NOT_FOUND,
              description = "해당 UUID 를 가진 파일이 없음",
              content = @Content(
                  mediaType = "application/json",
                  schema = @Schema(implementation = ErrorInfoRes.class),
                  examples = @ExampleObject(
                      name = "해당 UUID 를 가진 파일이 없음 예시",
                      value = """
                          {
                               "code": "BINARYCONTENT_001",
                               "message": "해당 UUID를 가진 바이너리 컨텐츠가 존재하지 않습니다.",
                               "httpStatus": 404
                           }
                          """
                  )
              )
          ),
      }
  )
  ResponseEntity<BinaryContentInfoRes> getFileInfo(@PathVariable UUID binaryContentId);
}
