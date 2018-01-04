package com.cimcitech.mginscription.model;

import java.util.List;

/**
 * Created by cimcitech on 2017/12/28.
 */

public class DeviceVo {
    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":[{"id":"33","register_id":"4","device_num":"B07070098","device_state":"1","device_reg_time":"2017-12-27 00:56:05","device_last_time":"1545843365","addtime":"2017-12-28 00:56:05","upkeep_mileage":null},{"id":"34","register_id":"4","device_num":"B07070099","device_state":"1","device_reg_time":"2017-12-27 00:56:05","device_last_time":"1545843365","addtime":"2017-12-28 00:56:05","upkeep_mileage":null}]}
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
         * info : [{"id":"33","register_id":"4","device_num":"B07070098","device_state":"1","device_reg_time":"2017-12-27 00:56:05","device_last_time":"1545843365","addtime":"2017-12-28 00:56:05","upkeep_mileage":null},{"id":"34","register_id":"4","device_num":"B07070099","device_state":"1","device_reg_time":"2017-12-27 00:56:05","device_last_time":"1545843365","addtime":"2017-12-28 00:56:05","upkeep_mileage":null}]
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
             * id : 33
             * register_id : 4
             * device_num : B07070098
             * device_state : 1
             * device_reg_time : 2017-12-27 00:56:05
             * device_last_time : 1545843365
             * addtime : 2017-12-28 00:56:05
             * upkeep_mileage : null
             */

            private String id;
            private String register_id;
            private String device_num;
            private String device_state;
            private String device_reg_time;
            private String device_last_time;
            private String addtime;
            private Object upkeep_mileage;

            public String getId() {
                return id;
            }

            public void setId(String id) {
                this.id = id;
            }

            public String getRegister_id() {
                return register_id;
            }

            public void setRegister_id(String register_id) {
                this.register_id = register_id;
            }

            public String getDevice_num() {
                return device_num;
            }

            public void setDevice_num(String device_num) {
                this.device_num = device_num;
            }

            public String getDevice_state() {
                return device_state;
            }

            public void setDevice_state(String device_state) {
                this.device_state = device_state;
            }

            public String getDevice_reg_time() {
                return device_reg_time;
            }

            public void setDevice_reg_time(String device_reg_time) {
                this.device_reg_time = device_reg_time;
            }

            public String getDevice_last_time() {
                return device_last_time;
            }

            public void setDevice_last_time(String device_last_time) {
                this.device_last_time = device_last_time;
            }

            public String getAddtime() {
                return addtime;
            }

            public void setAddtime(String addtime) {
                this.addtime = addtime;
            }

            public Object getUpkeep_mileage() {
                return upkeep_mileage;
            }

            public void setUpkeep_mileage(Object upkeep_mileage) {
                this.upkeep_mileage = upkeep_mileage;
            }
        }
    }
}
