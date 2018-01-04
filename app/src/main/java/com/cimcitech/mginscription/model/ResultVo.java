package com.cimcitech.mginscription.model;

/**
 * Created by cimcitech on 2018/1/3.
 */

public class ResultVo {
    /**
     * ret : 200
     * data : {"code":0,"returnmsg":"密码错误","info":[]}
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
         * code : 0
         * returnmsg : 密码错误
         * info : []
         */

        private int code;
        private String returnmsg;

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
    }
}
