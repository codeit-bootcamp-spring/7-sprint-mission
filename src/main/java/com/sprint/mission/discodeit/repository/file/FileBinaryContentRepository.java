package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.BinaryContent;
import com.sprint.mission.discodeit.repository.BinaryContentRepository;
import org.springframework.boot.autoconfigure.condition.ConditionalOnProperty;
import org.springframework.stereotype.Repository;

import java.io.*;
import java.util.*;

import static com.sprint.mission.discodeit.service.util.StaticString.DATA_PATH;

@Repository
@ConditionalOnProperty(
        prefix = "discodeit.repository",
        name = "type",
        havingValue = "file",
        matchIfMissing = true
)
public class FileBinaryContentRepository implements BinaryContentRepository {

    private final String binaryContentRepositoryPath = DATA_PATH +"binaryContentRepository.ser";
    private File binaryContentRepositoryFile = new File(binaryContentRepositoryPath);

    public FileBinaryContentRepository() {
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
        try(ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(binaryContentRepositoryPath,false));){
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
        try(ObjectInputStream ois = new ObjectInputStream(new FileInputStream(binaryContentRepositoryPath));){
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
