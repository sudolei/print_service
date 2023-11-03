package org.isky.util;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FilenameFilter;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

/**
 * 功能描述
 *
 * @author: sunlei
 * @date: 2023年06月15日 16:10
 */
public class PrintFileUitls {
    private static final Logger logger = LoggerFactory.getLogger("PrintFileUitls");

    /**
     * 获取所有pdf文件
     *
     * @param fileFolder
     * @return
     */
    public static List<File> getPdfFiles(String fileFolder) {
        File folder = new File(fileFolder);
        // 获取所有最底层文件夹列表
        File[] files = folder.listFiles(new FilenameFilter() {
            @Override
            public boolean accept(File dir, String name) {
                return name.endsWith(".pdf");
            }
        });
        List<File> list = Arrays.stream(files).collect(Collectors.toList());
        return list;
    }

    /**
     * 获取所有最底层文件夹列表
     *
     * @param fileFolder
     * @return
     */
    public static List<String> getFolders(String fileFolder) {
        ArrayList<String> bottomFolders = getAllFolders(fileFolder);
        return bottomFolders;
    }


    /**
     * 递归函数，获取指定文件夹下所有文件夹的路径列表
     *
     * @param folderPath 文件夹路径
     * @return 所有文件夹的路径列表
     */
    private static ArrayList<String> getAllFolders(String folderPath) {
        ArrayList<String> allFolders = new ArrayList<String>();
        File folder = new File(folderPath);
        // 判断是否是文件夹，如果是遍历子文件夹，将当前文件夹添加到路径列表中
        if (folder.isDirectory()) {
            allFolders.add(folder.getPath());
            File[] files = folder.listFiles();
            if (files != null && files.length > 0) {
                for (File file : files) {
                    allFolders.addAll(getAllFolders(file.getPath())); // 递归遍历子文件夹
                }
            }
        }
        return allFolders;
    }

    /**
     * 分割文件name
     *
     * @param fileName
     * @return
     */
    public static String[] getFileInfo(String fileName) {
        int dotIndex = fileName.lastIndexOf('.');
        String nameWithoutExtension = (dotIndex == -1) ? fileName : fileName.substring(0, dotIndex);
        String[] result = nameWithoutExtension.split("--");
        if (result.length == 1) {
            result = nameWithoutExtension.split("—");
        }
        return result;
    }

    /**
     * 校验
     *
     * @param fileName
     * @return
     */
    public static boolean isGoodFile(String fileName) {
        //是否包含--
        int index = fileName.indexOf("--");
        if (index == -1) {
            return false;
        }
        return true;
    }

    /**
     * 校正
     *
     * @param fileName
     */
    public static String regFileName(String fileName) {
        String newFileName = "";
        //校正
        String[] arr = getFileInfo(fileName);
        int i = 0;
        for (String str : arr) {
            if (str.indexOf("-") != -1) {
                str = str.replace("-", "--");
            }
            if (i != arr.length - 1) {
                newFileName += str + "--";
            } else {
                newFileName += str;
            }
            i++;
        }
        return newFileName;
    }

    /**
     * 获取纸张的材质
     *
     * @param fileName
     * @param material
     * @param originalLen SJTL/MXPCK/MXPCH/YKLTL /SYKLYL /TLBFM/ZTLBFM
     * @return
     */
    public static String getPdfMaterial(String fileName, String material, int originalLen) {
        String result = "";
        if (originalLen <= 5) {
            boolean flag = false;

            if (fileName.indexOf("SJTL") != -1) {
                flag = true;
            }
            if (fileName.indexOf("MXPCK") != -1) {
                flag = true;
            }
            if (fileName.indexOf("MXPCH") != -1) {
                flag = true;
            }
            if (fileName.indexOf("YKLTL") != -1) {
                flag = true;
            }
            if (fileName.indexOf("SYKLYL") != -1) {
                flag = true;
            }
            if (fileName.indexOf("TLBFM") != -1) {
                flag = true;
            }
            if (fileName.indexOf("ZTLBFM") != -1) {
                flag = true;
            }
            if (flag) {
                result = "YF";
            } else {
                int bkIndex = fileName.indexOf("BK");
                int gcIndex = fileName.indexOf("GC");
                int yfIndex = fileName.indexOf("YF");

                if (bkIndex != -1) {
                    result = "BK";
                }
                if (fileName.indexOf("PK") != -1) {
                    result = "BK";
                }
                if (gcIndex != -1) {
                    result = "GC";
                }
                if (yfIndex != -1) {
                    result = "YF";
                }
                if (fileName.indexOf("白卡纸") != -1) {
                    result = "BK";
                }
                if (fileName.indexOf("高松纸") != -1) {
                    result = "高松";
                }
            }


            if (StringUtils.isEmpty(result)) {
                //如果是本子  默认YF
                if (fileName.indexOf("BZ") != -1 || fileName.indexOf("SBZ") != -1) {
                    result = "YF";
                }
                if (fileName.indexOf("SQBMLB") != -1 || fileName.indexOf("QBMLB") != -1) {
                    result = "YF";
                }

                if (fileName.indexOf("GHGL") != -1) {
                    result = "YF";
                }

                if (fileName.indexOf("JINGZHUANG") != -1) {
                    result = "157YF";
                }
                //如果是LOMO卡片  默认YF
                if (fileName.indexOf("LM") != -1 || fileName.indexOf("SLM") != -1) {
                    result = "BK";
                }

                if (fileName.indexOf("SXKYJ") != -1 || fileName.indexOf("XKYJ") != -1) {
                    result = "BK";
                }

                if (fileName.indexOf("TL") != -1) {
                    //QQ台历  默认315XMG
                    if (fileName.indexOf("QQTL") != -1) {
                        result = "315XMG";
                    } else {
                        result = "250YF";
                    }
                }
                // 台历大部分都是250YF
                // MXP KP 如果写了YF就是300YF
            }
        } else if (originalLen >= 6) {
            result = material;
        }
        return result;
    }


    public static String getPdfDate(String fileDate) {
        String result = null;
        String[] fileDateArr = fileDate.split("\\.");
        if (fileDateArr.length == 2) {
            String m = fileDateArr[0];
            String d = fileDateArr[1];
            if (Integer.parseInt(m) < 10) {
                m = "0" + m;
            }

            if (Integer.parseInt(d) < 10) {
                d = "0" + d;
            }
            result = m + "-" + d;
        }

        return result;
    }


    public static String adjustFileName(String fileName) {
        String result = "";
        String[] fileNameArr = fileName.split(" ");
        for (String str : fileNameArr) {
            if (str.equals("YC") || str.equals("YCC")) {
                continue;
            }
            if (str.equals("传错") || str.equals("CC")) {
                continue;
            }
            if (StringUtils.isEmpty(str)) {
                continue;
            }
            result += str + " ";
        }
        return result;
    }

    /**
     * @return
     * @获取纸的类型(4C+0 4C+1C 4C+4C 1C+0 1C+1C)
     * @对应字典（0,1,2,3,4）
     */
    public static String getPdfPaperType(String fileName) {
        String result = "";
        boolean flag = false;
        if (fileName.indexOf("210D") != -1) {
            result = "0";
            flag = true;
        }
        if (fileName.indexOf("210S") != -1) {
            result = "2";
            flag = true;
        }

        if (fileName.indexOf("JINGZHUANG") != -1) {
            result = "1";
            flag = true;
        }

        if (!flag) {
            // MXP
            if (fileName.indexOf("MXP") != -1) {
                result = "1";
            }
            // 扑克牌
            if (fileName.indexOf("PK") != -1) {
                result = "0";
                if (fileName.indexOf("SM") != -1) {
                    result = "2";
                }
            }

            // 台历
            if (fileName.indexOf("TL") != -1) {
                result = "0";
            }

            // 本子  单面彩色
            if (fileName.indexOf("BZ") != -1 || fileName.indexOf("HBZ") != -1) {
                result = "0";
            }
            if (fileName.indexOf("BZCH") != -1 || fileName.indexOf("HBZCH") != -1) {
                result = "0";
            }
            // 背面留白
            if (fileName.indexOf("BMLB") != -1) {
                result = "0";
            }
            if (fileName.indexOf("KP") != -1) {
                result = "0";
                // 卡片自带背面
                if (fileName.indexOf("ZDBM") != -1) {
                    result = "2";
                }
            }
            // Lomo
            if (fileName.indexOf("LM") != -1 || fileName.indexOf("SLM") != -1) {
                result = "0";
            }
            // 本子自带背面
            if (fileName.indexOf("PKZDBM") != -1) {
                result = "2";
            }
        }

        return result;
    }

    public static String getLevel3Name(String fileName){
        Path path = Paths.get(fileName);
        String result = path.getName(2).toString();
        return result;
    }
}
