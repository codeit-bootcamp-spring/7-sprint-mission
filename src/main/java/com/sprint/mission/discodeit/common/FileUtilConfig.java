package com.sprint.mission.discodeit.common;

import com.sprint.mission.discodeit.entity.ModelType;
import com.sprint.mission.discodeit.repository.file.FileUtil;
import lombok.extern.slf4j.Slf4j;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

@Slf4j
@Configuration
public class FileUtilConfig {

    @Bean
    public FileUtil userFileUtil() { return new FileUtil(ModelType.USER); }

    @Bean
    public FileUtil channelFileUtil() {
        return new FileUtil(ModelType.CHANNEL);
    }

    @Bean
    public FileUtil messageFileUtil() {
        return new FileUtil(ModelType.MESSAGE);
    }

    @Bean
    public FileUtil userStatusFileUtil()  { return new FileUtil(ModelType.USERSTATUS); }

    @Bean
    public FileUtil readStatusFileUtil()  { return new FileUtil(ModelType.READSTATUS); }

    @Bean
    public FileUtil binaryContentFileUtil()  { return new FileUtil(ModelType.BINARYCONTENT); }

//    public static Dto_BinaryContent parsingMultipartFile(MultipartFile file) {
//        Dto_BinaryContent dtoFile = null;
//        try {
//            dtoFile = Dto_BinaryContent.from(
//                    file.getOriginalFilename(),
//                    file.getContentType(),
//                    file.getBytes(),
//                    file.getSize());
//        } catch (IOException e) {
//            throw new RuntimeException(e);
//        }
//        log.info("✅ file.getOriginalFilename() = [" + file.getOriginalFilename() + "]");
//        return dtoFile;
//    }
}
