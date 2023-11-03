package org.isky.util;

import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * 功能描述
 *
 * @author: sunlei
 * @date: 2023年07月24日 10:55
 */
public class DateUtils {

    public static String getPdfDateFold(String fileDate) throws ParseException {
        SimpleDateFormat sdf = new SimpleDateFormat("yyyy-MM-dd");
        SimpleDateFormat simpleDateFormat = new SimpleDateFormat("M.d");
        if (StringUtils.isEmpty(fileDate)){
            return simpleDateFormat.format(new Date());
        }else {
            Date d = sdf.parse(fileDate);
            return simpleDateFormat.format(d);
        }
    }
}
