package org.isky;

import net.sf.json.JSONObject;
import org.apache.poi.hssf.usermodel.*;
import org.apache.poi.ss.usermodel.*;
import org.apache.poi.ss.util.CellRangeAddress;
import org.isky.domain.DictData;
import org.isky.domain.FileInfo;
import org.isky.task.MyTask;
import org.isky.util.DateUtils;
import org.isky.util.PrintFileUitls;
import org.isky.util.StringUtils;
import org.isky.util.XlsTools;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.io.*;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.*;
import java.util.function.BiFunction;
import java.util.stream.Collectors;

public class Main {


    public static void main(String[] args) {
        Timer timer = new Timer();
        timer.schedule(new MyTask(), 1000, 1000);
    }
}