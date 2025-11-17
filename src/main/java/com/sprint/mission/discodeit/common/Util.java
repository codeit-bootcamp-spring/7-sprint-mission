package com.sprint.mission.discodeit.common;

import static java.util.stream.Collectors.toList;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import java.util.List;
import lombok.extern.slf4j.Slf4j;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

@Slf4j
public class Util {
    public static Dto_BinaryContent parsingMultipartFile(MultipartFile file) {
        if (file == null) {
          return null;
        }

        Dto_BinaryContent dtoFile = null;
        try {
            dtoFile = Dto_BinaryContent.from(
                    file.getOriginalFilename(),
                    file.getContentType(),
                    file.getBytes(),
                    file.getSize());
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
        log.info("✅ file.getOriginalFilename() = [" + file.getOriginalFilename() + "]");
        return dtoFile;
    }

    public static List<Dto_BinaryContent> parsingMultipartFileList(List<MultipartFile> fileList) {

        List<Dto_BinaryContent> dtoFiles = null;

        if (fileList != null) {
            dtoFiles = fileList.stream()
                .map(Util::parsingMultipartFile)
                .toList();
        }

        return  dtoFiles;
    }
}
