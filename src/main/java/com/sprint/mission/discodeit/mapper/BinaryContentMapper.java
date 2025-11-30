package com.sprint.mission.discodeit.mapper;

import com.sprint.mission.discodeit.dto.binarycontent.request.BinaryContentCreateReq;
import com.sprint.mission.discodeit.dto.binarycontent.response.BinaryContentInfoRes;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.exception.CustomException;
import com.sprint.mission.discodeit.exception.ErrorCode;
import com.sprint.mission.discodeit.util.DateTimeUtil;
import java.io.IOException;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {

  public static BinaryContentCreateReq toReqDto(MultipartFile file) {
    if (file == null || file.isEmpty()) {
      return null;
    }
    try {
      return new BinaryContentCreateReq(
          file.getBytes(),
          file.getOriginalFilename(),
          file.getContentType()
      );
    } catch (IOException e) {
      throw new CustomException(ErrorCode.FILE_CONVERSION_FAILED);
    }
  }

  public static BinaryContentInfoRes toResDto(BinaryContent binaryContent) {
    if (binaryContent == null) {
      return null;
    } else {
      return new BinaryContentInfoRes(
          binaryContent.getId(),
          binaryContent.getData(),
          binaryContent.getFileName(),
          binaryContent.getFileType(),
          DateTimeUtil.format(binaryContent.getCreatedAt())
      );
    }
  }
}
