package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.entity.Common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;

public class ReadService {
  /*  public static <T extends Common & Serializable> List<T> read (String type)
    {


        List<T> savList = null;
        try(ObjectInputStream ois
                    = new ObjectInputStream(
                new FileInputStream(Path.RooT_PATH.getPath()+"/"+type+"List.sav")))
        {

             savList  = (List<T>) ois.readObject();

         }catch (EOFException er){
            System.out.println("파일은 있는데 내용이 없다");

            return new ArrayList<>();
        }
        catch (Exception e) {
            System.out.println("못가지고온다 ");
            e.printStackTrace();
        }
        return savList;
    }*/

    public static <T extends Common & Serializable> List<T> read(String type, Class<T> clazz) {
        String path = Path.RooT_PATH.getPath()+"/"+type+"List.sav";
        File file = new File(path);

        if (!file.exists()) {
            return new ArrayList<>();
        }

        try (ObjectInputStream ois =
                     new ObjectInputStream(new BufferedInputStream(new FileInputStream(file)))) {

            Object obj = ois.readObject();

            if (!(obj instanceof List<?> raw)) {
                System.out.println("List가 아님: " + obj.getClass().getName());
                return new ArrayList<>();
            }


            List<T> out = new ArrayList<>(raw.size());
            for (Object e : raw) {
                out.add(clazz.cast(e));
            }
            return out;

        } catch (EOFException empty) {
            return new ArrayList<>();
        } catch (IOException e) {
            throw new UncheckedIOException("읽기 실패: " + path, e);
        } catch (ClassNotFoundException e) {
            throw new RuntimeException("클래스 로딩 실패: " + e.getMessage(), e);
        }
    }

}
