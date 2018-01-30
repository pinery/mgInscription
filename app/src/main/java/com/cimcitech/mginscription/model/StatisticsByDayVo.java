package com.cimcitech.mginscription.model;

import java.util.List;

/**
 * Created by dapineapple on 2018/1/17.
 */

public class StatisticsByDayVo {


    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":[{"id":"16","dev_num":"B07070099","countmakenum":"56","work_time":992,"run_time":2942,"countMakeNum":67,"Productivity":7,"sumTime":992}]}
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
         * info : [{"id":"16","dev_num":"B07070099","countmakenum":"56","work_time":992,"run_time":2942,"countMakeNum":67,"Productivity":7,"sumTime":992}]
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
             * countmakenum : 56
             * work_time : 992
             * run_time : 2942
             * countMakeNum : 67
             * Productivity : 7
             * sumTime : 992
             */

            private String id;
            private String dev_num;
            private String countmakenum;
            private int work_time;
            private int run_time;
            private int countMakeNum;
            private int Productivity;
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

            public int getRun_time() {
                return run_time;
            }

            public void setRun_time(int run_time) {
                this.run_time = run_time;
            }

            public int getCountMakeNum() {
                return countMakeNum;
            }

            public void setCountMakeNum(int countMakeNum) {
                this.countMakeNum = countMakeNum;
            }

            public int getProductivity() {
                return Productivity;
            }

            public void setProductivity(int Productivity) {
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
