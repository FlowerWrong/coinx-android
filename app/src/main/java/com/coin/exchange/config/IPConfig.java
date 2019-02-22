package com.coin.exchange.config;

import java.util.ArrayList;
import java.util.List;
import java.util.Random;

/**
 * @author Jiang zinc
 * @date 创建时间：2019/1/9
 * @description ip配置
 */
public class IPConfig {

    public static final Random RANDOM = new Random();

    public static final List<String> OK_EX_IP_MAP = new ArrayList<>();

    public static final List<String> BIT_MEX_IP_MAP = new ArrayList<>();

    public static final List<String> BIT_MEX_TEST_IP_MAP = new ArrayList<>();

    public static final List<String> OK_EX_WS_IP_MAP = new ArrayList<>();

    public static String OK_EX_IP;
    public static String BIT_MEX_IP;
    public static String BIT_MEX_TEST_IP;
    public static String OK_EX_WS_IP;

    static {
//        BIT_MEX_IP_MAP.add("www.bitmex.com");
//        BIT_MEX_TEST_IP_MAP.add("testnet.bitmex.com");
//        OK_EX_IP_MAP.add("www.okex.com");
//        OK_EX_WS_IP_MAP.add("real.okex.com");

        BIT_MEX_IP_MAP.add("52.51.111.111");
        BIT_MEX_TEST_IP_MAP.add("54.246.160.60");
        OK_EX_IP_MAP.add("104.19.212.87");
        OK_EX_WS_IP_MAP.add("149.129.81.70");

        OK_EX_IP = OK_EX_IP_MAP.get(RANDOM.nextInt(OK_EX_IP_MAP.size()));
        BIT_MEX_IP = BIT_MEX_IP_MAP.get(RANDOM.nextInt(BIT_MEX_IP_MAP.size()));
        BIT_MEX_TEST_IP = BIT_MEX_TEST_IP_MAP.get(RANDOM.nextInt(BIT_MEX_TEST_IP_MAP.size()));
        OK_EX_WS_IP = OK_EX_WS_IP_MAP.get(RANDOM.nextInt(OK_EX_WS_IP_MAP.size()));
    }

    public static String getBitMexIp(boolean isDebug) {
        if (isDebug) {
            return BIT_MEX_TEST_IP;
        } else {
            return BIT_MEX_IP;
        }

    }

    public static String getOkExIp() {
        return OK_EX_IP;
    }

    public static String getOkExWsIp() {
        return OK_EX_WS_IP;
    }

}
