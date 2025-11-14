package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class Util {
    public static void okMessage(String message) {
        System.out.println("Ⓜ️ " + message);
    }
    public static void errMessage(String message) { System.out.println("🚨" + message +  "🚨");}

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
        Util.okMessage("file.getOriginalFilename() = [" + file.getOriginalFilename() + "]");
        return dtoFile;
    }
}
