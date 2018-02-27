package com.pphh.rpc.util;

import java.text.SimpleDateFormat;
import java.util.Date;

/**
 * Created by mh on 2018/1/14.
 */
public class LogUtil {

    public static void print(String msg) {
        String threadName = Thread.currentThread().getName();

        Date nowTime = new Date(System.currentTimeMillis());
        SimpleDateFormat sdFormatter = new SimpleDateFormat("yyyyMMdd HH:mm:ss-SSS");
        String strTime = sdFormatter.format(nowTime);

        String info = String.format("[%s][%s] %s", strTime, threadName, msg);
        System.out.println(info);
    }

}
