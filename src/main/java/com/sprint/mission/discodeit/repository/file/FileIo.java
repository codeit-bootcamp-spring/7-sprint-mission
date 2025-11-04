package com.sprint.mission.discodeit.repository.file;

import com.sprint.mission.discodeit.entity.common.Common;

import java.io.*;
import java.util.*;

public class FileIo {

    public static <T extends Common & Serializable> void save (String folder, T type)
    {
        //파일명 ->아래에 폴더명 아래에 UUID 객체저장
        String path = Path.RooT_PATH.getPath()+"/"+folder+"/"+type.getId()+".sav";

        //혹시나 저장폴더가 없으면 생성한다
        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();

        //IO로 파일로저장하고 경론느 위에 path 저장은 type
        try (ObjectOutputStream oos =
                        new ObjectOutputStream(new FileOutputStream(path))
        ) {
            oos.writeObject(type);
            System.out.println( path + "에 객체 저장 성공");
        } catch (Exception e) {
            e.printStackTrace();
        }
    }


    public static <T extends Common & Serializable> Optional<T> read(String folder, UUID uuid, Class<T> clazz) {
        String path = Path.RooT_PATH.getPath() + "/" + folder + "/" + uuid + ".sav";
        File file = new File(path);

        if (!file.exists()) {
            return Optional.empty();
        }

        try (ObjectInputStream ois = new ObjectInputStream(new FileInputStream(file))) {
            Object obj = ois.readObject();


            //제네릭으로 받아오면 장확히 모른다
            //클래스로 구분을 지을수있는지 판단해야한다
            if (clazz.isInstance(obj)) {
                return Optional.of(clazz.cast(obj));
            } else {
                System.out.println("타입 불일치: 기대=" + clazz.getName() + ", 실제=" + obj.getClass().getName());
                return Optional.empty();
            }

        } catch (Exception e) {
            e.printStackTrace();
            return Optional.empty();
        }
    }

    public static <T extends Common & Serializable> List<T> readAll(String folder, Class<T> clazz) {
        String dirPath = Path.RooT_PATH.getPath() + "/" + folder;
        File dir = new File(dirPath);

        if (!dir.exists() || !dir.isDirectory()) {
            return List.of();
        }

        File[] files = dir.listFiles((d, name) -> name.endsWith(".sav"));
        if (files == null || files.length == 0) {
            return List.of();
        }
        System.out.println("ㅇㅇ");
        return Arrays.stream(files)
                .map(file -> file.getName().replace(".sav", ""))
                .map(name -> {
                    try {
                        return UUID.fromString(name);
                    } catch (IllegalArgumentException e) {
                        return null;
                    }
                })
                .filter(Objects::nonNull)
                .map(uuid -> read(folder, uuid, clazz))
                .flatMap(Optional::stream)
                .toList();
    }








}
