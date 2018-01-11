package com.cimcitech.mginscription.model;

/**
 * Created by cimcitech on 2018/1/11.
 */

public class DeviceDSumInfoVo {
    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":{"sumTime":0,"countMakeNum":null}}
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
         * info : {"sumTime":0,"countMakeNum":null}
         */

        private int code;
        private String returnmsg;
        private InfoBean info;

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

        public InfoBean getInfo() {
            return info;
        }

        public void setInfo(InfoBean info) {
            this.info = info;
        }

        public static class InfoBean {
            /**
             * sumTime : 0
             * countMakeNum : null
             */

            private int sumTime;
            private Object countMakeNum;

            public int getSumTime() {
                return sumTime;
            }

            public void setSumTime(int sumTime) {
                this.sumTime = sumTime;
            }

            public Object getCountMakeNum() {
                return countMakeNum;
            }

            public void setCountMakeNum(Object countMakeNum) {
                this.countMakeNum = countMakeNum;
            }
        }
    }
}
