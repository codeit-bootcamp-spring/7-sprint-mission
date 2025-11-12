package com.sprint.mission.discodeit.controller;

import com.sprint.mission.discodeit.dto.response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.media.Content;
import io.swagger.v3.oas.annotations.media.ExampleObject;
import io.swagger.v3.oas.annotations.media.Schema;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.List;
import java.util.UUID;
@Tag(name = "파일 관리(File Controller)",description = " 파일을 생성 및 조회하는 곳입니다. 놀랍게도 삭제와 변경은 하지 않습니다. 바지 사장 계층입니다.")
public interface FileControllerDocs {
    @Operation(summary = "파일 조회(Create File)", description = "파일을 id로 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일 조회 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BinaryContentResponseDto.class),
    examples = @ExampleObject(value = """
            파일을 조회합니다. byte로 돼 있으니 읽으려고 하지 마십시오
            """)
    )
    )
    @RequestMapping(value = "/{binaryContentId}", method = RequestMethod.GET)
    ResponseEntity<BinaryContent> read(@PathVariable UUID binaryContentId);
    @Operation(summary = "파일 다건 조회(Read File)", description = "파일을 조회합니다.")
    @ApiResponse(responseCode = "200", description = "파일 조회 성공",
    content = @Content(mediaType = "application/json", schema = @Schema(implementation = BinaryContentResponseDto.class),
    examples = @ExampleObject(value = """
            다건 조회입니다, 별 거는 없습니다. 생각할 거는 유저나 메세지 id가 아닌 binarycontent id로 조회하는 겁니다.
            이게 왜 있는지는 저도 모르겠습니다.
            """)
    )
    )
    @RequestMapping(value = "",method = RequestMethod.GET)
    ResponseEntity<List<BinaryContentResponseDto>> readByIdList(@RequestParam List<UUID> binaryContentIds);
}
