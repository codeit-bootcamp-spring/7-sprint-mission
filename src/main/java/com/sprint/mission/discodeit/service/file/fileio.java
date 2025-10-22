package com.sprint.mission.discodeit.service.file;

import com.sprint.mission.discodeit.entity.Common;

import java.io.*;
import java.util.ArrayList;
import java.util.List;
import java.util.stream.Collectors;

public class fileio {

    public static <T extends Common & Serializable> void save (String type, List<T> list)
    {
        String path = Path.RooT_PATH.getPath()+"/"+type+"List.sav";

        File file = new File(path);
        File parent = file.getParentFile();
        if (parent != null && !parent.exists()) parent.mkdirs();


        try (ObjectOutputStream oos =
                        new ObjectOutputStream(new FileOutputStream(path))
        ) {
            oos.writeObject(list);
            System.out.println( type + "리스트 객체 저장 성공");
        } catch (Exception e) {
            System.out.println("여기냐");
            e.printStackTrace();
        }
    }


    public static <T extends Common & Serializable> List<T> read (String type, Class<T> clas)
    {
        List<T> result = null;

        String path = Path.RooT_PATH.getPath()+"/"+type+"List.sav";
        File file = new File(path);

        if (!file.exists()) {
            System.out.println("그래서했다");
            return new ArrayList<>();
        }
        try(ObjectInputStream ois
                    = new ObjectInputStream(
                new FileInputStream(path )))
        {
            //오브젝트가 리스트인걸 알지만 T타입이라는건 자바컴파일이 모르나보다
            //그래서 와일드카드는 모든타입 가능이니  캐스팅은 성립된다
            //스트림으로 요소하나하나  캐스팅을 했다

            List<?> oblist = (List<?>) ois.readObject();
            result = oblist.stream()
                    .map(clas::cast)
                    .collect(Collectors.toCollection(ArrayList::new));

        }catch (EOFException er){
            System.out.println("파일은 있는데 내용이 없다");

            return new ArrayList<>();
        }catch (ClassCastException class_arr){
            System.out.println("아 이거 내용물이 T가 아닌가봐");
        }
        catch (Exception e) {
            System.out.println("못가지고온다 ");
            e.printStackTrace();
        }
        return result;
    }




}
