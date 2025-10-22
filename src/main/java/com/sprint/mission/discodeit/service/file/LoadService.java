package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Common;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;
import java.io.Serializable;
import java.util.List;


public class LoadService {

 public static <T extends Common & Serializable> void load (String type,List<T> list)
    {
        String path = Path.RooT_PATH.getPath()+"/"+type+"List.sav";

       File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();


        try (
                ObjectOutputStream oos =
                        new ObjectOutputStream(new FileOutputStream(path))


        ) {
            oos.writeObject(list);
            System.out.println( type + "리스트 객체 저장 성공");
        } catch (Exception e) {
            System.out.println("여기냐");
            e.printStackTrace();
        }
    }
}
