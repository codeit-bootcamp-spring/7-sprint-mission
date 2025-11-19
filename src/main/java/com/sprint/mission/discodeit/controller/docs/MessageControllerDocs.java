package com.sprint.mission.discodeit.controller.docs;

import com.sprint.mission.discodeit.service.dto.request.MessageForm;
import com.sprint.mission.discodeit.service.dto.request.MessageUpdate;
import com.sprint.mission.discodeit.service.dto.response.MessageResponse;
import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.tags.Tag;
import org.springframework.web.multipart.MultipartFile;

import java.util.List;
import java.util.UUID;

@Tag(name = "Message")
public interface MessageControllerDocs {

    @Operation(summary = "Message 보내기")
    MessageResponse sendMessage(MessageForm messageCreateRequest,
                                List<MultipartFile> attachments);

    @Operation(summary = "메세지 수정")
    MessageResponse updateMessage(UUID messageId, MessageUpdate messageUpdate);

    @Operation(summary = "Message 삭제")
    String deleteMessage(UUID messageId);

    @Operation(summary = "Channel의 Message 조회")
    List<MessageResponse> getAllMessageByChannelId(UUID channelId);


}
