package com.cimcitech.mginscription.utils;

import android.content.Context;

import java.util.Comparator;
import java.util.Iterator;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by cimcitech on 2017/12/29.
 */

public class ConfigUtil {

    public static Context CONTEXT;

    public static String IP = "http://www.aqlac.com/mgApi/Public/MinGong/";

    public static final String MD5_CODE = "sFxe6jew1n2JxkRT_SZYYGDMSP";

    //获取系统时间的10位的时间戳
    public static String GET_TIME() {
        long time = System.currentTimeMillis() / 1000;
        return String.valueOf(time);
    }

    //密码加密
    public static String GET_PASSWORD(String password) {
        try {
            ChangeCharsetUtil charsetUtil = new ChangeCharsetUtil();
            return MD5Util.md5(MD5Util.md5(password) + charsetUtil.toGB2312(MD5Util.md5(MD5_CODE)));
        } catch (Exception e) {
            e.printStackTrace();
            return "";
        }
    }

    //获取sign签名
    public static String GET_SIGN(Map maps) {
        Map<String, String> map = new TreeMap<>(
                new Comparator<String>() {
                    public int compare(String obj1, String obj2) {
                        return obj1.compareTo(obj2);
                    }
                });
        Iterator iterator = maps.entrySet().iterator();
        while (iterator.hasNext()) {
            Map.Entry entry = (Map.Entry) iterator.next();
            map.put(entry.getKey().toString(), entry.getValue().toString());
        }
        Set<String> keySet = map.keySet();
        Iterator<String> iter = keySet.iterator();
        String sign = "";
        while (iter.hasNext()) {
            String key = iter.next();
            System.out.println(key + ":" + map.get(key));
            sign = sign + key + map.get(key);
        }
        return MD5Util.md5(sign + GET_TIME() + MD5_CODE).toUpperCase();
    }
}
