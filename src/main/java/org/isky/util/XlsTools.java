package org.isky.util;

import java.util.HashMap;
import java.util.Map;

/**
 * 功能描述:XLS文件操作
 *
 * @author: sunlei
 * @date: 2023年06月15日 15:40
 */
public class XlsTools {


        public static Map<String, String> getXlsCellMap(){
            Map<String, String> cellMap = new HashMap<>();
            cellMap.put("300GC", "GC");
            cellMap.put("300YF", "YF");
            cellMap.put("300BK", "BK");
            cellMap.put("315XMG", "XGM");
            cellMap.put("300ZG", "ZG");
            cellMap.put("300GM", "GM");
            cellMap.put("300LQ", "LQ");
            cellMap.put("300XG", "XG");
            cellMap.put("250YF", "250YF");
            cellMap.put("140GM", "140GM");
            cellMap.put("145XC", "145XC");
            cellMap.put("115XM", "115XM");
            cellMap.put("200TB", "200TB");
            cellMap.put("157YF", "157YF");
            cellMap.put("157TB", "157TB");
            cellMap.put("128YF", "128YF");
            cellMap.put("100SJ", "100SJ");
            cellMap.put("高松", "高松");
            cellMap.put("250XMG", "250XMG");
            cellMap.put("200YF", "200YF");
            cellMap.put("300JZ", "300JZ");
            return cellMap;
        }
}
