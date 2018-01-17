package com.cimcitech.mginscription.model;

import java.util.List;

/**
 * Created by dapineapple on 2018/1/17.
 */

public class StatisticsByDayVo {

    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":[{"id":"16","dev_num":"B07070099","countmakenum":"13","work_time":22,"sumMakeNum":22,"Productivity":"29%","sumTime":76},{"id":"17","dev_num":"B07070098","countmakenum":"10","work_time":21,"sumMakeNum":22,"Productivity":"29%","sumTime":76}]}
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
         * info : [{"id":"16","dev_num":"B07070099","countmakenum":"13","work_time":22,"sumMakeNum":22,"Productivity":"29%","sumTime":76},{"id":"17","dev_num":"B07070098","countmakenum":"10","work_time":21,"sumMakeNum":22,"Productivity":"29%","sumTime":76}]
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
            /**
             * id : 16
             * dev_num : B07070099
             * countmakenum : 13
             * work_time : 22
             * sumMakeNum : 22
             * Productivity : 29%
             * sumTime : 76
             */

            private String id;
            private String dev_num;
            private String countmakenum;
            private int work_time;
            private int sumMakeNum;
            private String Productivity;
            private int sumTime;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getDev_num() {
                return dev_num;
            }

            public void setDev_num(String dev_num) {
                this.dev_num = dev_num;
            }

            public String getCountmakenum() {
                return countmakenum;
            }

            public void setCountmakenum(String countmakenum) {
                this.countmakenum = countmakenum;
            }

            public int getWork_time() {
                return work_time;
            }

            public void setWork_time(int work_time) {
                this.work_time = work_time;
            }

            public int getSumMakeNum() {
                return sumMakeNum;
            }

            public void setSumMakeNum(int sumMakeNum) {
                this.sumMakeNum = sumMakeNum;
            }

            public String getProductivity() {
                return Productivity;
            }

            public void setProductivity(String Productivity) {
                this.Productivity = Productivity;
            }

            public int getSumTime() {
                return sumTime;
            }

            public void setSumTime(int sumTime) {
                this.sumTime = sumTime;
            }
        }
    }
}
