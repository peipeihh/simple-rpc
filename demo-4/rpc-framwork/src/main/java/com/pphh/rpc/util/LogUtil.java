/*
 * Copyright 2018 peipeihh
 *
 * Licensed under the Apache License, Version 2.0 (the "License");
 * you may not use this file except in compliance with the License.
 * You may obtain a copy of the License at
 *
 * http://www.apache.org/licenses/LICENSE-2.0
 *
 * Unless required by applicable law or agreed to in writing, software
 * distributed under the License is distributed on an "AS IS" BASIS,
 * WITHOUT WARRANTIES OR CONDITIONS OF ANY KIND, either express or implied.
 * limitations under the License.
 */
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
