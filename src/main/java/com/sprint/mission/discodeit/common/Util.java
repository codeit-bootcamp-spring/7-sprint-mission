package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.entity.dto.Dto_BinaryContent;
import org.springframework.web.multipart.MultipartFile;

import java.io.IOException;

public class Util {
    public static void okMessage(String message) {
        System.out.println("Ⓜ️ " + message);
    }
    public static void errMessage(String message) { System.out.println("🚨" + message +  "🚨");}
//    public static UUID uuidTest_1 = UUID.fromString("ae3d209c-b2be-41d7-895e-30bb4192498c"); //❎ for test
//    public static UUID uuidTest_2 = UUID.fromString("abc5103d-d916-4ea2-8b45-22be27f66b2f");
//    public static UUID uuidTest_3 = UUID.fromString("ceb51b81-cb4a-41b1-b8f8-3dd15bdbedd9");
//    public static UUID uuidTest_4 = UUID.fromString("f84daca8-d311-4858-bd01-7693adf65c02");
//    public static UUID uuidTest_5 = UUID.fromString("496859e7-1536-4ad7-bb64-ba895f756288");

    public static Dto_BinaryContent parsingMultipartFile(MultipartFile file) {
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
