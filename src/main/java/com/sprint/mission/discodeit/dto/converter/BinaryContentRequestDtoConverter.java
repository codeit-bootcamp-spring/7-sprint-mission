package com.sprint.mission.discodeit.dto.converter;


import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import lombok.experimental.UtilityClass;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@UtilityClass
public class BinaryContentRequestDtoConverter {

    // 단일 binaryContent dto 변환
    public CreateBinaryContentRequestDto from(MultipartFile attachment) {
        CreateBinaryContentRequestDto profileRequest = null;

        if(attachment != null && !attachment.isEmpty()) {
            try {
                profileRequest = new CreateBinaryContentRequestDto(
                        attachment.getOriginalFilename(),
                        attachment.getContentType(),
                        attachment.getBytes()
                );
            } catch (IOException e) {
                throw new RuntimeException(e);
            }
        }

        return profileRequest;
    }

    // 여러 binaryContents dto 변환
    public List<CreateBinaryContentRequestDto> from(List<MultipartFile> attachments) {
        List<CreateBinaryContentRequestDto> attachmentRequests;

        if(attachments != null && !attachments.isEmpty()) {
            attachmentRequests = attachments.stream()
                    .map(attachment -> {
                                try {
                                    return new CreateBinaryContentRequestDto(
                                            attachment.getOriginalFilename(),
                                            attachment.getContentType(),
                                            attachment.getBytes());
                                } catch(IOException e) {
                                    throw new RuntimeException(e);
                                }
                            }
                    ).collect(Collectors.toList());
        } else {
            attachmentRequests = new ArrayList<>();
        }

        return attachmentRequests;
    }
}
