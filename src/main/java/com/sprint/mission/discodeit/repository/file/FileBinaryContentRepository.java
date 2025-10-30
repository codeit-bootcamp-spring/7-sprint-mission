package com.sprint.mission.discodeit.repository.file;

import ch.qos.logback.classic.pattern.PrefixCompositeConverter;
import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import com.sprint.mission.discodeit.service.util.StaticString;
import jakarta.annotation.PostConstruct;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.autoconfigure.condition.ConditionalOnExpression;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.core.env.Environment;
import org.springframework.stereotype.Repository;

import javax.naming.Name;
import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.service.util.StaticString.DATA_PATH;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = false
)

public class FileBinaryContentRepository implements BinaryContentRepository {


    private final String binaryContentRepositoryPath;
    private final File binaryContentRepositoryFile;

    public FileBinaryContentRepository(Environment env){
        binaryContentRepositoryPath = env.getProperty("discodeit.repository.file-directory")+"binaryContentRepository.ser";
        binaryContentRepositoryFile = new File(binaryContentRepositoryPath);
        repositoryCheck();
        resetBinaryContentRepository();
    }

    @Override
    public BinaryContent createBinaryContent(BinaryContent binaryContent) {
        Map<UUID, BinaryContent> binaryContentMap = loadAllBinaryContent();
        binaryContentMap.put(binaryContent.getId(),binaryContent);
        saveAllBinaryContent(binaryContentMap);
        return binaryContent;

    }

    @Override
    public Optional<BinaryContent> readBinaryContent(UUID binaryContentId) {
        return Optional.ofNullable(loadAllBinaryContent().get(binaryContentId));
    }

    @Override
    public List<BinaryContent> readAllBinaryContent() {
        return loadAllBinaryContent().values().stream().toList();
    }

    @Override
    public boolean isBinaryContentExist(UUID binaryContentId) {
        return loadAllBinaryContent().containsKey(binaryContentId);
    }

    @Override
    public void deleteBinaryContent(UUID binaryContentId) {
        Map<UUID, BinaryContent> binaryContentMap = loadAllBinaryContent();
        binaryContentMap.remove(binaryContentId);
        saveAllBinaryContent(binaryContentMap);

    }

    private void saveAllBinaryContent(Map<UUID, BinaryContent> binaryContentMap){
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryContentRepositoryFile,false));){
            oos.writeObject(binaryContentMap);
            oos.flush();
        }
        catch (Exception e){
            e.printStackTrace();
        }

    }

    private Map<UUID,BinaryContent> loadAllBinaryContent(){
        if(!binaryContentRepositoryFile.exists() || binaryContentRepositoryFile.length() == 0) {
            return new HashMap<>();
        }
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryContentRepositoryFile));){
            return (Map<UUID, BinaryContent>) ois.readObject();
        }
        catch (Exception e){
            e.printStackTrace();
        }
        return new HashMap<>();
    }

    private void repositoryCheck(){
        if(!binaryContentRepositoryFile.exists()){
            try{
                binaryContentRepositoryFile.createNewFile();
            } catch (IOException e) {
                e.printStackTrace();
            }
        }



    }
    @Override
    public void resetBinaryContentRepository(){
        saveAllBinaryContent(new HashMap<>());
    }
}
