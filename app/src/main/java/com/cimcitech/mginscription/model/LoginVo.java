package com.cimcitech.mginscription.model;

/**
 * Created by cimcitech on 2018/1/3.
 */

public class LoginVo {
    /**
     * ret : 200
     * data : {"code":1,"returnmsg":"success","info":{"register_id":"5","register_phone":"18666660749","register_name":"18666660749","register_password":"c712ceb589d5dd72105a645927992dbc","register_icon_url":"","register_birthday":"2018-01-03","register_gender":"M","register_email":"","register_create_time":"2018-01-03 11:41:23","register_device_number":"0","register_status":"1","is_have_dev":"0","last_login_time":null,"token":"ECC9476D124AB9CDF3335AEF402F6CD1B1DA3AA52B3E021350C910E0F349AD7C"}}
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
         * info : {"register_id":"5","register_phone":"18666660749","register_name":"18666660749","register_password":"c712ceb589d5dd72105a645927992dbc","register_icon_url":"","register_birthday":"2018-01-03","register_gender":"M","register_email":"","register_create_time":"2018-01-03 11:41:23","register_device_number":"0","register_status":"1","is_have_dev":"0","last_login_time":null,"token":"ECC9476D124AB9CDF3335AEF402F6CD1B1DA3AA52B3E021350C910E0F349AD7C"}
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
             * register_id : 5
             * register_phone : 18666660749
             * register_name : 18666660749
             * register_password : c712ceb589d5dd72105a645927992dbc
             * register_icon_url :
             * register_birthday : 2018-01-03
             * register_gender : M
             * register_email :
             * register_create_time : 2018-01-03 11:41:23
             * register_device_number : 0
             * register_status : 1
             * is_have_dev : 0
             * last_login_time : null
             * token : ECC9476D124AB9CDF3335AEF402F6CD1B1DA3AA52B3E021350C910E0F349AD7C
             */

            private String register_id;
            private String register_phone;
            private String register_name;
            private String register_password;
            private String register_icon_url;
            private String register_birthday;
            private String register_gender;
            private String register_email;
            private String register_create_time;
            private String register_device_number;
            private String register_status;
            private String is_have_dev;
            private Object last_login_time;
            private String token;

            public String getRegister_id() {
                return register_id;
            }

            public void setRegister_id(String register_id) {
                this.register_id = register_id;
            }

            public String getRegister_phone() {
                return register_phone;
            }

            public void setRegister_phone(String register_phone) {
                this.register_phone = register_phone;
            }

            public String getRegister_name() {
                return register_name;
            }

            public void setRegister_name(String register_name) {
                this.register_name = register_name;
            }

            public String getRegister_password() {
                return register_password;
            }

            public void setRegister_password(String register_password) {
                this.register_password = register_password;
            }

            public String getRegister_icon_url() {
                return register_icon_url;
            }

            public void setRegister_icon_url(String register_icon_url) {
                this.register_icon_url = register_icon_url;
            }

            public String getRegister_birthday() {
                return register_birthday;
            }

            public void setRegister_birthday(String register_birthday) {
                this.register_birthday = register_birthday;
            }

            public String getRegister_gender() {
                return register_gender;
            }

            public void setRegister_gender(String register_gender) {
                this.register_gender = register_gender;
            }

            public String getRegister_email() {
                return register_email;
            }

            public void setRegister_email(String register_email) {
                this.register_email = register_email;
            }

            public String getRegister_create_time() {
                return register_create_time;
            }

            public void setRegister_create_time(String register_create_time) {
                this.register_create_time = register_create_time;
            }

            public String getRegister_device_number() {
                return register_device_number;
            }

            public void setRegister_device_number(String register_device_number) {
                this.register_device_number = register_device_number;
            }

            public String getRegister_status() {
                return register_status;
            }

            public void setRegister_status(String register_status) {
                this.register_status = register_status;
            }

            public String getIs_have_dev() {
                return is_have_dev;
            }

            public void setIs_have_dev(String is_have_dev) {
                this.is_have_dev = is_have_dev;
            }

            public Object getLast_login_time() {
                return last_login_time;
            }

            public void setLast_login_time(Object last_login_time) {
                this.last_login_time = last_login_time;
            }

            public String getToken() {
                return token;
            }

            public void setToken(String token) {
                this.token = token;
            }
        }
    }
}
