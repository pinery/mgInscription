package com.cimcitech.mginscription.model;

import java.util.List;

/**
 * Created by cimcitech on 2018/1/11.
 */

public class DeviceRegisterInfo1Vo {

    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":[{"x":[["压制下限","0"],["回程到位","0"],["吸片升降气缸回位","0"],["压网回位","0"],["刮板回位","0"],["微提1回位","0"],["纵推到位","0"],["纵推回位","0"],["纵推回位","0"],["横推到位","0"],["横推回位","0"],["卸模回位","0"],["刮板禁止下降","0"],["底模打蜡升降回位","0"],["微提2回位","0"],["底模打蜡红外感应器","0"],["重量1到位","0"],["重量2到位","0"],["自动启动","0"],["手动卸模按钮","0"]],"y":[["微提1脉冲","0"],["微提2脉冲","0"],["机械手正转","0"],["机械手反转","0"],["卸模顶出阀","0"],["卸模下行阀","0"],["电磁阀3","0"],["预压压制","0"],["微提1方向","0"],["电磁阀4","0"],["仪表1清零","0"],["一次放网顶网气缸","0"],["微提2方向","0"],["仪表2清零","0"],["纵推阀","0"],["未知","0"],["烤网水泵","0"],["预压回程","0"],["压网","0"],["1号给料机停止","0"]]}]}
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
         * info : [{"x":[["压制下限","0"],["回程到位","0"],["吸片升降气缸回位","0"],["压网回位","0"],["刮板回位","0"],["微提1回位","0"],["纵推到位","0"],["纵推回位","0"],["纵推回位","0"],["横推到位","0"],["横推回位","0"],["卸模回位","0"],["刮板禁止下降","0"],["底模打蜡升降回位","0"],["微提2回位","0"],["底模打蜡红外感应器","0"],["重量1到位","0"],["重量2到位","0"],["自动启动","0"],["手动卸模按钮","0"]],"y":[["微提1脉冲","0"],["微提2脉冲","0"],["机械手正转","0"],["机械手反转","0"],["卸模顶出阀","0"],["卸模下行阀","0"],["电磁阀3","0"],["预压压制","0"],["微提1方向","0"],["电磁阀4","0"],["仪表1清零","0"],["一次放网顶网气缸","0"],["微提2方向","0"],["仪表2清零","0"],["纵推阀","0"],["未知","0"],["烤网水泵","0"],["预压回程","0"],["压网","0"],["1号给料机停止","0"]]}]
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
            private List<List<String>> x;
            private List<List<String>> y;

            public List<List<String>> getX() {
                return x;
            }

            public void setX(List<List<String>> x) {
                this.x = x;
            }

            public List<List<String>> getY() {
                return y;
            }

            public void setY(List<List<String>> y) {
                this.y = y;
            }
        }
    }
}
