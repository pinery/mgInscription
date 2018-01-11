package com.cimcitech.mginscription.utils;

import android.content.Context;

import com.cimcitech.mginscription.model.DeviceVo;
import com.cimcitech.mginscription.model.LoginVo;

import java.util.Comparator;
import java.util.Iterator;
import java.util.List;
import java.util.Map;
import java.util.Set;
import java.util.TreeMap;

/**
 * Created by cimcitech on 2017/12/29.
 */

public class ConfigUtil {

    public static Context CONTEXT;

    public static boolean isLogin = false; //登录状态

    public static boolean isOutLogin = false; //是否点击了退出登录按钮

    public static String deviceNum; //用户选中的设备

    public static List<DeviceVo.DataBean.InfoBean> infoBeans;//用户所有的设备信息

    public static String userName = ""; //用户名称

    public static String KEY_LOGIN_AUTO = "key_login_auto"; //保存登录账号和密码

    public static String IP = "http://www.aqlac.com/mgApi/Public/MinGong/";

    public static final String MD5_CODE = "sFxe6jew1n2JxkRT_SZYYGDMP";

    public static LoginVo.DataBean.InfoBean loginInfo; //登录的user_data

    //获取系统时间的10位的时间戳
    public static String GET_TIME() {
        long time = System.currentTimeMillis() / 1000;
        return String.valueOf(time);
    }

    //密码加密
    public static String GET_PASSWORD(String password) {
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
        return MD5Util.md5(sign + GET_TIME() + MD5_CODE).toUpperCase();
    }

    public static final String json1 = "{\n" +
            "  \"ret\": 200,\n" +
            "  \"data\": {\n" +
            "    \"code\": 1,\n" +
            "    \"returnmsg\": \"success\",\n" +
            "    \"info\": [\n" +
            "      {\n" +
            "        \"x\": [\n" +
            "          [\n" +
            "            \"压制下限\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"回程到位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"吸片升降气缸回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"压网回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"刮板回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"微提1回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"纵推到位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"纵推回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"纵推回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"横推到位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"横推回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"卸模回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"刮板禁止下降\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"底模打蜡升降回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"微提2回位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"底模打蜡红外感应器\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"重量1到位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"重量2到位\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"自动启动\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"手动卸模按钮\",\n" +
            "            \"0\"\n" +
            "          ]\n" +
            "        ],\n" +
            "        \"y\": [\n" +
            "          [\n" +
            "            \"微提1脉冲\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"微提2脉冲\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"机械手正转\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"机械手反转\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"卸模顶出阀\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"卸模下行阀\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"电磁阀3\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"预压压制\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"微提1方向\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"电磁阀4\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"仪表1清零\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"一次放网顶网气缸\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"微提2方向\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"仪表2清零\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"纵推阀\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"未知\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"烤网水泵\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"预压回程\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"压网\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"1号给料机停止\",\n" +
            "            \"0\"\n" +
            "          ]\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"msg\": \"\"\n" +
            "}";

    public static final String json2 = "{\n" +
            "  \"ret\": 200,\n" +
            "  \"data\": {\n" +
            "    \"code\": 1,\n" +
            "    \"returnmsg\": \"success\",\n" +
            "    \"info\": [\n" +
            "      {\n" +
            "        \"c\": [\n" +
            "          [\n" +
            "            \"压制时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"摊料时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"商标放时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"商标吸时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"顶铁圈时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"微提延时启动\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"卸模时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"预压时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"机械手1吸时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"机械手1放时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"考网时间\",\n" +
            "            \"0\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"一次粘网次数\",\n" +
            "            \"0CPS\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"一次粘网片数\",\n" +
            "            \"0CPS\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"卷胶布气缸1时间\",\n" +
            "            \"0S\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"二次粘网次数\",\n" +
            "            \"0CPS\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"二次粘网片数\",\n" +
            "            \"0CPS\"\n" +
            "          ],\n" +
            "          [\n" +
            "            \"卷胶布气缸2时间\",\n" +
            "            \"0S\"\n" +
            "          ]\n" +
            "        ],\n" +
            "        \"d\": [\n" +
            "          [\n" +
            "            \"设备产量\",\n" +
            "            \"56CPS\"\n" +
            "          ]\n" +
            "        ]\n" +
            "      }\n" +
            "    ]\n" +
            "  },\n" +
            "  \"msg\": \"\"\n" +
            "}";
}
