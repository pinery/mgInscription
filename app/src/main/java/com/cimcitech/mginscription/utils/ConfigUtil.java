package com.cimcitech.mginscription.utils;

import android.content.Context;

import com.cimcitech.mginscription.model.LoginVo;

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

    public static boolean isLogin;

    public static String KEY_LOGIN_AUTO = "key_login_auto"; //保存登录账号和密码

    public static String IP = "http://www.aqlac.com/mgApi/Public/MinGong/";

    public static final String MD5_CODE = "sFxe6jew1n2JxkRT_SZYYGDMP";

    public static LoginVo.DataBean.InfoBean info; //登录的user_data

    //获取系统时间的10位的时间戳
    public static String GET_TIME() {
        long time = System.currentTimeMillis() / 1000;
        return String.valueOf(time);
    }

    //密码加密
    public static String GET_PASSWORD(String password) {
        System.out.println("md5--password-->" + MD5Util.md5(password));
        System.out.println("md5--MD5_CODE-->" + MD5Util.md5(MD5_CODE));
        System.out.println("md5--password + MD5_CODE-->" + MD5Util.md5(password) + MD5Util.md5(MD5_CODE));
        return MD5Util.md5(MD5Util.md5(password) + MD5Util.md5(MD5_CODE));
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
            if (!map.get(key).equals(""))
                sign = sign + key + map.get(key);
        }
        System.out.println("sign--->" + sign);
        System.out.println("sign + GET_TIME() + MD5_CODE--->" + sign + GET_TIME() + MD5_CODE);
        System.out.println("sign + GET_TIME() + MD5_CODE---md5--->" + MD5Util.md5(sign + GET_TIME() + MD5_CODE));
        System.out.println("sign + GET_TIME() + MD5_CODE---md5---toUpperCase--->"
                + MD5Util.md5(sign + GET_TIME() + MD5_CODE).toUpperCase());
        return MD5Util.md5(sign + GET_TIME() + MD5_CODE).toUpperCase();
    }
}
