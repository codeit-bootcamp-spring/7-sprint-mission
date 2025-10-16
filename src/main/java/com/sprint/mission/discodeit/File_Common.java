package com.sprint.mission.discodeit;

import java.io.File;
import java.io.FileOutputStream;
import java.io.ObjectOutputStream;

public class File_Common {
    public static final String ROOT_PATH = "/Users/my05030/Desktop/장미연/7-sprint-mission/src/main/java/com/sprint/mission/discodeit/FileEx";

    public static void fileCreate(File file, String filePath) {
        // 1. 경로내 폴더 확인
        File rootFolder = new File(filePath);
        if (!rootFolder.exists()) {
            rootFolder.mkdir(); // 생성해야 할 폴더 경로가 하나일 때
            // file. mkdir(); // 생성해야 할 폴더 경로가 여러개 일 때
        }

//        File_Common.okMessage("1. 경로내 폴더 확인 " + filePath); // 🚫

        // 2. 파일 생성
        if (!file.exists()) {
            String[] arrPath = file.getAbsolutePath().split("/"); //.split("/");
            String message = "파일 생성 : [" + arrPath[arrPath.length - 1]  + "]";

            try {
                file.createNewFile();
                File_Common.okMessage(message);
            } catch (Exception e) {
                File_Common.errMessage(message);
                e.printStackTrace();
            }
        }
    }
    public static <T> void fileWrite(T listT, String filePath, String message) {
        try (ObjectOutputStream oos = new ObjectOutputStream(new FileOutputStream(filePath));) {
            oos.writeObject(listT);
            File_Common.okMessage(message);
        } catch (Exception e) {
            File_Common.errMessage(message);
            e.printStackTrace();
        }
    }

    public static void okMessage(String message) {
        System.out.println("\uD83C\uDF3C " + message); // 🌼
    }
    public static void errMessage(String message) {
        System.out.println("🚨" + message + " Err!" + "🚨");
    }
}
