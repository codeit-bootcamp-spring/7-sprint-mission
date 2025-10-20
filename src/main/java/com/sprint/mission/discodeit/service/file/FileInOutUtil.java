package com.sprint.mission.discodeit.service.file;

import java.io.*;

public class FileInOutUtil {
    public static ObjectInputStream getInputStream(String fileName) throws Exception {
        File file = new File(fileName);
        FileInputStream fis = null;
        BufferedInputStream bis = null;
        ObjectInputStream ois = null;

        if (!file.exists()) {
            file.createNewFile();
        }

        fis = new FileInputStream(file);
        bis = new BufferedInputStream(fis);
        ois = new ObjectInputStream(bis);


        return ois;
    }

    public static ObjectOutputStream getOutputStream(String fileName) throws Exception {
        File file = new File(fileName);
        FileOutputStream fos = null;
        BufferedOutputStream bos = null;
        ObjectOutputStream oos = null;


        if (!file.exists()) {
            file.createNewFile();
        }

        fos = new FileOutputStream(file);
        bos = new BufferedOutputStream(fos);
        oos = new ObjectOutputStream(bos);


        return oos;
    }
}
