package org.isky.task;

import org.isky.Print;
import org.isky.domain.FileInfo;
import org.isky.util.DateUtils;
import org.isky.util.FileUtil;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.File;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;

public class MyTask extends TimerTask {

    private static final Logger logger = LoggerFactory.getLogger(TimerTask.class);
    /**
     * The action to be performed by this timer task.
     */
    @Override
    public void run() {
        SimpleDateFormat sdf = new SimpleDateFormat("HH:mm:ss");
        String runTime = sdf.format(new Date());
        logger.warn(runTime);
        if (runTime.equals("11:34:00")) {
            logger.warn("开始执行");
            SimpleDateFormat simpleDateFormat = new SimpleDateFormat("yyyy-MM-dd");
            String folderDate = null;
            try {
                folderDate = DateUtils.getPdfDateFold(simpleDateFormat.format(new Date()));
            } catch (ParseException e) {
                throw new RuntimeException(e);
            }
            String pdfFileFolder = "D:\\世纪开元" + File.separator + folderDate;
            // 获取解析文件夹数据
            Map<String, List<FileInfo>> m = Print.getFileInfo(pdfFileFolder, null);
            List<FileInfo> allList = new ArrayList<>();
            for (Map.Entry<String, List<FileInfo>> entry : m.entrySet()) {
                allList.addAll(entry.getValue());
            }
            // 获取分析后的数据
            Map<String, Long> data = Print.getData(allList);
            // 获取文件夹文件数量
            List<Map<String,Integer>> fileCounts = FileUtil.folderFiles(pdfFileFolder);
            //生成EXCEL
            Print.createXls(data,fileCounts, folderDate);
        }

    }
}
