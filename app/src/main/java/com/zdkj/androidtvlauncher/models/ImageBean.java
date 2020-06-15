package com.zdkj.androidtvlauncher.models;

import java.util.List;

public class ImageBean {
    /**
     * code : 200
     * msg : 图片广告列表
     * data : [{"image_url":"http://ad.zadtek.com/uploads/default/show.gif","image_name":"tupian","image_id":0,"timestr":5}]
     */

    private int code;
    private String msg;
    private List<DataBean> data;


    public int getCode() {
        return code;
    }

    public void setCode(int code) {
        this.code = code;
    }

    public String getMsg() {
        return msg;
    }

    public void setMsg(String msg) {
        this.msg = msg;
    }

    public List<DataBean> getData() {
        return data;
    }

    public void setData(List<DataBean> data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * image_url : http://ad.zadtek.com/uploads/default/show.gif
         * image_name : tupian
         * image_id : 0
         * timestr : 5
         */

        private String image_url;
        private String image_name;
        private String image_id;
        private int timestr;

        public String getImage_url() {
            return image_url;
        }

        public void setImage_url(String image_url) {
            this.image_url = image_url;
        }

        public String getImage_name() {
            return image_name;
        }

        public void setImage_name(String image_name) {
            this.image_name = image_name;
        }

        public String getImage_id() {
            return image_id;
        }

        public void setImage_id(String image_id) {
            this.image_id = image_id;
        }

        public int getTimestr() {
            return timestr;
        }

        public void setTimestr(int timestr) {
            this.timestr = timestr;
        }
    }
}
