package org.gun.common.utils;

import java.text.SimpleDateFormat;
import java.util.Date;

public class Utils {
    public static String getCurrentTimeString() {
        SimpleDateFormat df = new SimpleDateFormat("yyyyMMddHHmmss");
        return df.format(new Date());
    }
}
