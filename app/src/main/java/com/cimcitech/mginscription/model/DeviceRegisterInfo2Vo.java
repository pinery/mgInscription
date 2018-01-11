package com.cimcitech.mginscription.model;

import java.util.List;

/**
 * Created by cimcitech on 2018/1/11.
 */

public class DeviceRegisterInfo2Vo {
    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":[{"c":[["压制时间","0S"],["摊料时间","0S"],["商标放时间","0S"],["商标吸时间","0S"],["顶铁圈时间","0S"],["微提延时启动","0"],["卸模时间","0S"],["预压时间","0S"],["机械手1吸时间","0S"],["机械手1放时间","0S"],["考网时间","0"],["一次粘网次数","0CPS"],["一次粘网片数","0CPS"],["卷胶布气缸1时间","0S"],["二次粘网次数","0CPS"],["二次粘网片数","0CPS"],["卷胶布气缸2时间","0S"]],"d":[["设备产量","56CPS"]]}]}
     * msg :
     */

    private int ret;
    private DataBean data;
    private String msg;

    public int getRet() {
        return ret;
    }

    public void setRet(int ret) {
        this.ret = ret;
    }

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public static class DataBean {
        /**
         * code : 1
         * returnmsg : success
         * info : [{"c":[["压制时间","0S"],["摊料时间","0S"],["商标放时间","0S"],["商标吸时间","0S"],["顶铁圈时间","0S"],["微提延时启动","0"],["卸模时间","0S"],["预压时间","0S"],["机械手1吸时间","0S"],["机械手1放时间","0S"],["考网时间","0"],["一次粘网次数","0CPS"],["一次粘网片数","0CPS"],["卷胶布气缸1时间","0S"],["二次粘网次数","0CPS"],["二次粘网片数","0CPS"],["卷胶布气缸2时间","0S"]],"d":[["设备产量","56CPS"]]}]
         */

        private int code;
        private String returnmsg;
        private List<InfoBean> info;

        public int getCode() {
            return code;
        }

        public void setCode(int code) {
            this.code = code;
        }

        public String getReturnmsg() {
            return returnmsg;
        }

        public void setReturnmsg(String returnmsg) {
            this.returnmsg = returnmsg;
        }

        public List<InfoBean> getInfo() {
            return info;
        }

        public void setInfo(List<InfoBean> info) {
            this.info = info;
        }

        public static class InfoBean {
            private List<List<String>> c;
            private List<List<String>> d;

            public List<List<String>> getC() {
                return c;
            }

            public void setC(List<List<String>> c) {
                this.c = c;
            }

            public List<List<String>> getD() {
                return d;
            }

            public void setD(List<List<String>> d) {
                this.d = d;
            }
        }
    }
}
