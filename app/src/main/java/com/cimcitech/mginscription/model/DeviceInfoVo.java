package com.cimcitech.mginscription.model;

import java.util.List;

/**
 * Created by cimcitech on 2018/1/4.
 */

public class DeviceInfoVo {

    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":[{"id":"16","update_time":"2017-07-23 13:46:41","dev_num":"B07070099","work_time":3,"ambienttemperature":"14","ambienthumidity":"96","countmakenum":"13","sumTime":213,"countMakeNum":24,"startTime":"2017-07-23 11:39:11","maintenance_time":213}]}
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
         * info : [{"id":"16","update_time":"2017-07-23 13:46:41","dev_num":"B07070099","work_time":3,"ambienttemperature":"14","ambienthumidity":"96","countmakenum":"13","sumTime":213,"countMakeNum":24,"startTime":"2017-07-23 11:39:11","maintenance_time":213}]
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
             * update_time : 2017-07-23 13:46:41
             * dev_num : B07070099
             * work_time : 3
             * ambienttemperature : 14
             * ambienthumidity : 96
             * countmakenum : 13
             * sumTime : 213
             * countMakeNum : 24
             * startTime : 2017-07-23 11:39:11
             * maintenance_time : 213
             */

            private String id;
            private String update_time;
            private String dev_num;
            private int work_time;
            private String ambienttemperature;
            private String ambienthumidity;
            private String countmakenum;
            private int sumTime;
            private int countMakeNum;
            private String startTime;
            private int maintenance_time;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getUpdate_time() {
                return update_time;
            }

            public void setUpdate_time(String update_time) {
                this.update_time = update_time;
            }

            public String getDev_num() {
                return dev_num;
            }

            public void setDev_num(String dev_num) {
                this.dev_num = dev_num;
            }

            public int getWork_time() {
                return work_time;
            }

            public void setWork_time(int work_time) {
                this.work_time = work_time;
            }

            public String getAmbienttemperature() {
                return ambienttemperature;
            }

            public void setAmbienttemperature(String ambienttemperature) {
                this.ambienttemperature = ambienttemperature;
            }

            public String getAmbienthumidity() {
                return ambienthumidity;
            }

            public void setAmbienthumidity(String ambienthumidity) {
                this.ambienthumidity = ambienthumidity;
            }

            public String getCountmakenum() {
                return countmakenum;
            }

            public void setCountmakenum(String countmakenum) {
                this.countmakenum = countmakenum;
            }

            public int getSumTime() {
                return sumTime;
            }

            public void setSumTime(int sumTime) {
                this.sumTime = sumTime;
            }

            public int getCountMakeNum() {
                return countMakeNum;
            }

            public void setCountMakeNum(int countMakeNum) {
                this.countMakeNum = countMakeNum;
            }

            public String getStartTime() {
                return startTime;
            }

            public void setStartTime(String startTime) {
                this.startTime = startTime;
            }

            public int getMaintenance_time() {
                return maintenance_time;
            }

            public void setMaintenance_time(int maintenance_time) {
                this.maintenance_time = maintenance_time;
            }
        }
    }
}
