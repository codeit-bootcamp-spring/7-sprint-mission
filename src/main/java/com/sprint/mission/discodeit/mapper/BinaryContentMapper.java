package com.sprint.mission.discodeit.mapper;


import com.sprint.mission.discodeit.dto.binarycontent.Response.BinaryContentResponseDto;
import com.sprint.mission.discodeit.dto.binarycontent.request.CreateBinaryContentRequestDto;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.storage.BinaryContentStorage;
import lombok.RequiredArgsConstructor;
import org.springframework.stereotype.Component;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

@Component
@RequiredArgsConstructor
public class BinaryContentMapper {
    private final BinaryContentStorage binaryContentStorage;

    // 단일 binaryContent dto 변환
    public CreateBinaryContentRequestDto toRequestDto(MultipartFile attachment) {
        CreateBinaryContentRequestDto profileRequest = null;

        if(attachment != null && !attachment.isEmpty()) {
            try {
                profileRequest = new CreateBinaryContentRequestDto(
                        attachment.getOriginalFilename(),
                        attachment.getSize(),
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
    public List<CreateBinaryContentRequestDto> toRequestDto(List<MultipartFile> attachments) {
        List<CreateBinaryContentRequestDto> attachmentRequests;

        if(attachments != null && !attachments.isEmpty()) {
            attachmentRequests = attachments.stream()
                    .map(attachment -> {
                                try {
                                    return new CreateBinaryContentRequestDto(
                                            attachment.getOriginalFilename(),
                                            attachment.getSize(),
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

    public BinaryContentResponseDto toResponseDto(BinaryContent attachment) {
        if (attachment == null) return null;

        try {
            byte[] bytes = binaryContentStorage.get(attachment.getId()).readAllBytes();

            return new BinaryContentResponseDto(
                    attachment.getId(),
                    attachment.getFileName(),
                    attachment.getSize(),
                    attachment.getContentType(),
                    bytes
            );

        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public List<BinaryContentResponseDto> toResponseDto(List<BinaryContent> attachments) {
        if (attachments == null || attachments.isEmpty()) {
            return new ArrayList<>();
        }

        return attachments.stream()
                .map(attachment -> {
                    try {
                        byte[] bytes = binaryContentStorage.get(attachment.getId()).readAllBytes();

                        return new BinaryContentResponseDto(
                                attachment.getId(),
                                attachment.getFileName(),
                                attachment.getSize(),
                                attachment.getContentType(),
                                bytes
                        );
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .toList();
    }
}
