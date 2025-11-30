package com.sprint.mission.discodeit.storage;

import com.sprint.mission.discodeit.dto.binaryContent.response.BinaryContentResponseDto;
import java.io.InputStream;
import java.util.UUID;
import org.springframework.http.ResponseEntity;

/***
 * 바이너리 데이터의 저장/로드를 담당하는 컴포넌트
 */
public interface BinaryContentStorage {

  /***
   * UUID 키 정보를 바탕으로 byte[] 데이터를 저장합니다.
   * UUID는 BinaryContent의 id입니다.
   * @param binaryContentId
   * @param bytes
   * @return UUID
   */
  UUID put(UUID binaryContentId, byte[] bytes);

  /***
   * 키 정보를 바탕으로 byte[] 데이터를 읽어 InputStream 타입으로 반환합니다.
   * UUID는 BinaryContentId 입니다.
   * @param binaryContentId
   * @return InputStream
   */
  InputStream get(UUID binaryContentId);

  /***
   * HTTP ATPI로 다운로드 기능을 제공합니다.
   * BinaryContentDto 정보를 바탕으로 파일을 다운로드 할 수 있는 응답을 반환합니다.
   * @param dto
   * @return
   */
  ResponseEntity<?> download(BinaryContentResponseDto dto);
}
