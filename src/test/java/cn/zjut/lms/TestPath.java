package cn.zjut.lms;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.Map;

public class TestPath {
    public static void main(String[] args) {
//        ArrayList<String> paths=new ArrayList<String>();
//        paths = ["sys.role.list",""];
        String[] paths= {"sys.role.list","sys.user.list","book.list","sys.user.list2"};
        ArrayList<Map<String,Object>> listMap=new ArrayList<Map<String,Object>>();
        Map<String,Object> map = new HashMap<>();

        map.put("name","sys");
        listMap.add(map);



        System.out.println(listMap);

//        for (int i = 0;i<paths.length;i++){
//            String[] p = paths[i].split("\\.");
//
//            ArrayList<Map<String,Object>> datas=new ArrayList<Map<String,Object>>();
//            datas.add("name",p[0]);
//
//
//        }

//        System.out.println(path);

//        String[] paths = path.split("\\.");
////        for(int i=0;i<paths.length;i++){
////            System.out.println(paths[i]);
////        }
//        for (String p:paths){
////            System.out.println("eeeee");
//            System.out.println(p);
//        }

//        System.out.println(paths);


    }
}
