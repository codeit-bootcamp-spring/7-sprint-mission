package com.sprint.mission.discodeit.dto.request;

import java.util.List;
import java.util.UUID;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import org.springframework.web.multipart.MultipartFile;
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
public class MessageCreateRequest {

  private String content;
  private UUID channelId;
  private UUID authorId;
  private List<MultipartFile> attachments;


}
