package org.isky;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.isky.domain.DictData;
import org.isky.domain.FileInfo;
import org.isky.util.PrintFileUitls;
import org.isky.util.StringUtils;
import org.isky.util.XlsTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.FileOutputStream;
import java.io.IOException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Print {
    private static final Logger logger = LoggerFactory.getLogger(Print.class);
    public static void createXls(Map<String, Long> data, String folderDate) {
        String path = "D:\\世纪开元" + File.separator;
        // 纸张材质对应信息
        Map<String, String> cellMap = XlsTools.getXlsCellMap();

        List<DictData> dataList = new ArrayList<>();
        dataList.add(new DictData("4C+0", "0"));
        dataList.add(new DictData("4C+1", "1"));
        dataList.add(new DictData("4C+4", "2"));
        dataList.add(new DictData("1C+0", "3"));
        dataList.add(new DictData("1C+1", "4"));
        try {
            HSSFWorkbook wb = new HSSFWorkbook();
            //在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
            HSSFSheet sheet = wb.createSheet(folderDate);
            //创建表头样式
            HSSFCellStyle headStyle = wb.createCellStyle();
            headStyle.setAlignment(HorizontalAlignment.CENTER);
            headStyle.setFillForegroundColor(IndexedColors.YELLOW.index);
            headStyle.setFillPattern(FillPatternType.SOLID_FOREGROUND);
            //创建单元格，并设置值表头 设置表头居中
            HSSFCellStyle style = wb.createCellStyle();
            //创建一个居中格式
            style.setAlignment(HorizontalAlignment.CENTER);
            // 填充工作表
            // 定义存放英文字段名和中文字段名的数组
            //在sheet中添加表头第0行,注意老版本poi对Excel的行数列数有限制short
            HSSFRow row = sheet.createRow((int) 0);

            CellRangeAddress region = new CellRangeAddress(0, 0, 1, 2);
            CellRangeAddress region1 = new CellRangeAddress(0, 0, 3, 4);
            CellRangeAddress region2 = new CellRangeAddress(0, 0, 5, 6);
            CellRangeAddress region3 = new CellRangeAddress(0, 0, 7, 8);
            CellRangeAddress region4 = new CellRangeAddress(0, 0, 9, 10);
            sheet.addMergedRegion(region);
            sheet.addMergedRegion(region1);
            sheet.addMergedRegion(region2);
            sheet.addMergedRegion(region3);
            sheet.addMergedRegion(region4);

            // 填充表头
            for (int i = 0; i < headerTitles.length; i++) {
                if (i == 0) {
                    HSSFCell cell = row.createCell(i);
                    cell.setCellValue(headerTitles[i]);
                    cell.setCellStyle(headStyle);
                } else {
                    HSSFCell cell = row.createCell(i * 2 - 1);
                    cell.setCellValue(headerTitles[i]);
                    cell.setCellStyle(headStyle);
                }
            }
            int index = 0;

            for (String key : cellMap.keySet()) {
                row = sheet.createRow(index + 1);
                String material = cellMap.get(key);
                HSSFCell firstCell = row.createCell(0);
                firstCell.setCellValue(material);
                firstCell.setCellStyle(style);
                for (int j = 1; j <= dataList.size() * 2; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(0);
                    cell.setCellStyle(style);
                }
                int allSum = 0;
                for (int i = 0; i < strArr.length; i++) {
                    String getKey = material + "_" + materials[i];
                    System.out.println("getKey: " + getKey);
                    if (data.containsKey(getKey)) {
                        Long thisVal = data.get(getKey);
                        int cellVal = Integer.parseInt(strArr[i]);
                        HSSFCell cell = row.createCell(cellVal);
                        cell.setCellValue(thisVal);
                        cell.setCellStyle(style);
                        long ysVal = 0;
                        switch (materials[i]) {
                            case "0":
                                ysVal = thisVal * 4;
                                break;
                            case "1":
                                ysVal = thisVal * 5;
                                break;
                            case "2":
                                ysVal = thisVal * 8;
                                break;
                            case "3":
                                ysVal = thisVal;
                                break;
                            case "4":
                                ysVal = thisVal * 2;
                                break;
                        }
                        //印数
                        HSSFCell ysCell = row.createCell(cellVal + 1);
                        ysCell.setCellStyle(style);
                        ysCell.setCellValue(ysVal);

                        allSum += thisVal;
                    }
                }

                index++;

                HSSFCell endCell = row.createCell(11);
                endCell.setCellStyle(style);
                endCell.setCellValue(allSum);
            }

            // 新建一输出文件流
            FileOutputStream out = new FileOutputStream(path + folderDate + ".xls");
            // 把相应的Excel工作蒲存盘
            wb.write(out);
            // 操作结束 关闭文件
            out.close();
        } catch (IOException e) {
            throw new RuntimeException(e);
        }
    }

    public static Map<String, Long> getData(List<FileInfo> l) {
        Map<String, Long> prodMap = l.stream().collect(Collectors.groupingBy(x -> x.getMaterial() + "_" + x.getPaperType(), Collectors.summingLong(x -> x.getPaper() * x.getCopies())));
        return prodMap;
    }

    /**
     * 数据分析
     *
     * @param m
     * @return
     */
    public static Map<String, Map<String, Long>> getData(Map<String, List<FileInfo>> m) {
        Map<String, Map<String, Long>> result = new HashMap<>();
        for (Map.Entry<String, List<FileInfo>> entry : m.entrySet()) {
//            System.out.println("key:" + entry.getKey());
            String key = entry.getKey();
            List<FileInfo> l = entry.getValue();
            Map<String, Long> prodMap = l.stream().collect(Collectors.groupingBy(x -> x.getMaterial() + "_" + x.getPaperType(), Collectors.summingLong(x -> x.getPaper() * x.getCopies())));
            result.put(key, prodMap);
        }
        return result;
    }

    /**
     * 数据分析
     *
     * @param fileFolder
     * @param fileDate
     * @return
     */
    public static Map<String, List<FileInfo>> getFileInfo(String fileFolder, String fileDate) {
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
        if (StringUtils.isEmpty(fileDate)) {
            fileDate = simpleDateFormat.format(new Date());
        }
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy");
        String year = sdf.format(new Date());
        Map<String, List<FileInfo>> m = new HashMap<>();
        try {
            String[] fileFolderArr = fileFolder.split("\\\\");
            String fileDay = PrintFileUitls.getPdfDate(fileFolderArr[fileFolderArr.length - 1]);
            String fileFoldFileDate = null;
            if (StringUtils.isNotEmpty(fileDay)) {
                fileFoldFileDate = year + "-" + fileDay;
            }


            // 获取文件夹
            List<String> folders = PrintFileUitls.getFolders(fileFolder);

            for (String f : folders) {
                if (fileFolder.equals(f)) {
                    continue;
                }
                String key = PrintFileUitls.getLevel3Name(f);
                // 获取文件夹下的所有文件
                List<File> files = PrintFileUitls.getPdfFiles(f);

                List<FileInfo> fileInfos = new ArrayList<>();
                for (File file : files) {
                    // 文件名全部设置成大写
                    String fileName = file.getName().toUpperCase();
                    // 校正
                    fileName = PrintFileUitls.regFileName(fileName);

                    // 验证是否文件名是否正确
                    boolean isGoodFile = PrintFileUitls.isGoodFile(fileName);
                    if (!isGoodFile) {
                        break;
                    }
                    // 分割文件名
                    String[] fileNameInfoArr = PrintFileUitls.getFileInfo(fileName);
                    int fileNameLen = fileNameInfoArr.length;
                    if (fileNameLen < 4) {
                        logger.info("file name len < 4:{}", fileName);
                        break;
                    }

                    if (fileNameLen == 5) {
                        logger.info("file name len 5:{}", fileName);
                    }

                    String pdfFileName = PrintFileUitls.adjustFileName(fileNameInfoArr[0]);

                    // 获取用纸数量
                    String paperStr = (fileNameInfoArr[fileNameLen - 1]).replaceAll("[^\\d]", "");
                    long paper = 0;
                    if (StringUtils.isNotEmpty(paperStr)) {
                        paper = Long.parseLong(paperStr);
                    }

                    // 多少份
                    int index = fileNameInfoArr[fileNameLen - 3].indexOf("FEN");
                    if (index == -1) {
                        index = fileNameInfoArr[fileNameLen - 3].indexOf("份");
                    }
                    long copies = (index != -1) ? Long.parseLong(fileNameInfoArr[fileNameLen - 3].replaceAll("[^\\d]", "")) : -1;
                    if (copies == -1) {
                        logger.info("copies  == 100");
                        logger.info("copies  is -1:{}", fileName);
                    }
                    //
                    long realPaper = paper * copies;
                    // 纸的材质
                    String pdfMaterial = PrintFileUitls.getPdfMaterial(pdfFileName, fileNameInfoArr[1], fileNameLen);
                    // 纸的类型
                    String paperType = PrintFileUitls.getPdfPaperType(pdfFileName);

                    // 保存数据
                    FileInfo info = new FileInfo();
                    info.setFileDate(fileFoldFileDate);
                    info.setFileName(pdfFileName);
                    info.setFilePath(file.getPath());
                    info.setPaper(paper);
                    info.setPaperType(paperType);
                    info.setRealPaper(realPaper);
                    info.setCopies(copies);
                    info.setMaterial(pdfMaterial);
                    fileInfos.add(info);
                }
                m.merge(key, fileInfos, new BiFunction<List<FileInfo>, List<FileInfo>, List<FileInfo>>() {
                    @Override
                    public List<FileInfo> apply(List<FileInfo> fileInfos, List<FileInfo> fileInfos2) {
                        List<FileInfo> result = new ArrayList<>(fileInfos);
                        result.addAll(fileInfos2);
                        return result;
                    }
                });
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    private static final String[] headerTitles = {"纸张", "4C+0C", "4C+1C", "4C+4C", "1C+1C", "1C+0", "合计"};


    private static final String[] materials = {"0", "1", "2", "3", "4"};

    private static final String[] strArr = {"1", "3", "5", "7", "9",};
}
