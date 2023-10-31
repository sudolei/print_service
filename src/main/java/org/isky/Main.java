package org.isky;

import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.FillPatternType;
import org.apache.poi.ss.usermodel.HorizontalAlignment;
import org.apache.poi.ss.usermodel.IndexedColors;
import org.apache.poi.ss.util.CellRangeAddress;
import org.isky.domain.FileInfo;
import org.isky.util.PrintFileUitls;
import org.isky.util.StringUtils;
import org.isky.util.XlsTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.io.OutputStream;
import java.io.UnsupportedEncodingException;
import java.text.SimpleDateFormat;
import java.util.*;

public class Main {

    private static final Logger logger = LoggerFactory.getLogger(Main.class);

    public static void main(String[] args) {
        Map<String, List<FileInfo>> m = getFileInfo("D:\\chuanjian\\10.31", null);
        for (Map.Entry<String,List<FileInfo>> entry : m.entrySet()) {
            System.out.println(entry.getKey());
            List<FileInfo> l = entry.getValue();
            for (FileInfo info:l){
                System.out.println(info.getFileName());
                System.out.println(info.getMaterial());
            }
        }
    }

    /**
     * 数据分析
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
                File thisFile = new File(f);
                String key = thisFile.getName();
                System.out.println(f);
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
                m.put(key, fileInfos);
            }
        } catch (Exception e) {
            e.printStackTrace();
        }
        return m;
    }

    private static final String[] headerTitles= {"纸张", "4C+0C", "4C+1C", "4C+4C", "1C+1C", "1C+0", "合计"};

    public void export(FileInfo fileInfo) {
        //定义导出的excel名字
        String excelName = "纸张统计" + fileInfo.getFileDate();
        // 纸张材质对应信息
        Map<String, String> cellMap = XlsTools.getXlsCellMap();
        // 获取纸张类型（字典表信息）
//        SysDictData dictData = new SysDictData();
//        dictData.setDictType("dict_paper_type");
//        List<SysDictData> dictDataList = dictDataService.selectDictDataList(dictData);
        // 设置默认文件名为当前时间：年月日时分秒
        if (excelName == null || excelName == "") {
            excelName = new SimpleDateFormat("yyyyMMddhhmmss").format(new Date()).toString();
        }

        try {
            //创建一个WorkBook,对应一个Excel文件
            HSSFWorkbook wb = new HSSFWorkbook();
            //在Workbook中，创建一个sheet，对应Excel中的工作薄（sheet）
            HSSFSheet sheet = wb.createSheet(excelName);
            //创建表头样式
            HSSFCellStyle headStyle= wb.createCellStyle();
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
            // 填充内容
            for (String key : cellMap.keySet()) {
                row = sheet.createRow(index + 1);
                String material = cellMap.get(key);
                HSSFCell firstCell = row.createCell(0);
                firstCell.setCellValue(material);
                firstCell.setCellStyle(style);
                List<BetFileStatistics> betFileStatistics = betFileInfoService.selectBetFileStatistics(betFileInfo);

                for (int j = 1; j <= dictDataList.size() * 2; j++) {
                    HSSFCell cell = row.createCell(j);
                    cell.setCellValue(0);
                    cell.setCellStyle(style);
                }
                int allSum = 0;
                int cellIndex = 0;
                for (BetFileStatistics statistics : betFileStatistics) {
                    String fieldValue = String.valueOf(statistics.getSumPaper());
                    allSum += statistics.getSumPaper();
                    int cellNum = 0;
                    int ysCellNum = 0;
                    long ysVal = 0;
                    if (cellIndex == 0) {
                        cellNum = cellIndex + 1;
                        // 印数
                        ysCellNum = cellIndex + 2;
                        ysVal = statistics.getSumPaper() * 4;
                    } else {
                        int paperType = Integer.parseInt(statistics.getPaperType());
                        cellNum = (paperType + 1) * 2 - 1;
                        ysCellNum = (paperType + 1) * 2;
                        switch (paperType) {
                            case 0:
                                ysVal = statistics.getSumPaper() * 4;
                                break;
                            case 1:
                                ysVal = statistics.getSumPaper() * 5;
                                break;
                            case 2:
                                ysVal = statistics.getSumPaper() * 8;
                                break;
                            case 3:
                                ysVal = statistics.getSumPaper() * 1;
                                break;
                            case 4:
                                ysVal = statistics.getSumPaper() * 2;
                                break;
                        }
                    }
                    HSSFCell cell = row.createCell(cellNum);
                    cell.setCellStyle(style);
                    cell.setCellValue(fieldValue);

                    //印数
                    HSSFCell ysCell = row.createCell(ysCellNum);
                    ysCell.setCellStyle(style);
                    ysCell.setCellValue(ysVal);
                    cellIndex++;
                }
                HSSFCell endCell = row.createCell(dictDataList.size() * 2 + 1);
                endCell.setCellStyle(style);
                endCell.setCellValue(allSum);
                index++;
            }
            //将文件输出
            OutputStream ouputStream = response.getOutputStream();
            wb.write(ouputStream);
            ouputStream.flush();
            ouputStream.close();
        } catch (Exception e) {
            e.printStackTrace();
            logger.info("导出Excel失败！");
            logger.error(e.getMessage());
        }
    }
}