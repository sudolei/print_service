package org.isky.util;

import java.io.File;
import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

public class FileUtil {


    public static void main(String[] args) {
        File folder = new File("D:\\世纪开元\\11.8\\");
        List<File> directorys = folders(folder);
        List<Map<String, Integer>> list = new ArrayList<>();
        for (File f : directorys) {
            Map<String, Integer> m = countFilesByFolder(f);
            list.add(m);
        }

        for (Map<String, Integer> map : list) {
            for (Map.Entry<String, Integer> entry : map.entrySet()) {
                System.out.println(entry.getKey() + "," + entry.getValue());
            }
        }
    }


    public static List<Map<String, Integer>> folderFiles(String filePath) {
        File folder = new File(filePath);
        List<File> directorys = folders(folder);
        List<Map<String, Integer>> list = new ArrayList<>();
        for (File f : directorys) {
            Map<String, Integer> m = countFilesByFolder(f);
            list.add(m);
        }
        return list;
    }

    public static int countFiles(File folder) {
        int count = 0;
        File[] files = folder.listFiles();
        for (File file : files) {
            if (file.isFile()) {
                count++;
            } else {
                count += countFiles(file);
            }
        }
        return count;
    }

    public static List<File> folders(File folder) {
        List<File> list = new ArrayList<>();
        for (File file : folder.listFiles()) {
            if (file.isDirectory()) {
                list.add(file);
            }
        }
        return list;
    }


    public static HashMap<String, Integer> countFilesByFolder(File folder) {
        HashMap<String, Integer> result = new HashMap<>();
        if (folder.isDirectory()) {
            for (File file : folder.listFiles()) {
                result.put(file.getPath(), countFiles(file));
            }
        }
        return result;
    }

//    public static HashMap<String, Integer> countFiles(File folder) {
//        HashMap<String, Integer> result = new HashMap<>();
//        if (folder.isDirectory()) {
//            for (File file : folder.listFiles()) {
//                if (file.isDirectory()) {
//                    result.put(file.getPath(), countFiles(file).size());
//                } else {
//                    String parentPath = file.getParent();
//                    if (result.containsKey(parentPath)) {
//                        result.put(parentPath, result.get(parentPath) + 1);
//                    } else {
//                        result.put(parentPath, 1);
//                    }
//                }
//            }
//        }
//        return result;
//    }
}




