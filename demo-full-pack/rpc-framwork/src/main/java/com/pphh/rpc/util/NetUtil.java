package com.pphh.rpc.util;

import java.net.InetAddress;
import java.util.regex.Pattern;

/**
 * Created by mh on 2018/2/19.
 */
public class NetUtil {

    public static final String LOCALHOST = "127.0.0.1";
    public static final String ANYHOST = "0.0.0.0";
    private static final Pattern IP_PATTERN = Pattern.compile("\\d{1,3}(\\.\\d{1,3}){3,5}$");
    private static final Pattern LOCAL_IP_PATTERN = Pattern.compile("127(\\.\\d{1,3}){3}$");

    public static InetAddress getLocalAddress() {
        InetAddress localAddress = null;

        try {
            localAddress = InetAddress.getLocalHost();
            if (!isValidAddress(localAddress)) {
                localAddress = null;
            }
        } catch (Throwable e) {
            LogUtil.print("Failed to retriving local address by hostname:" + e);
        }

        return localAddress;
    }

    public static boolean isValidAddress(InetAddress address) {
        if (address == null || address.isLoopbackAddress()) return false;
        String name = address.getHostAddress();
        return (name != null && !ANYHOST.equals(name) && !LOCALHOST.equals(name) && IP_PATTERN.matcher(name).matches());
    }

}
