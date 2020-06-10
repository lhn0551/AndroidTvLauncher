package com.zdkj.androidtvlauncher.models;

public class AfterPlayingBean {

    /**
     * code : 200
     * msg : 分成明细
     * data : {"delete":0,"video_name":"0002.mp4"}
     */

    private int code;
    private String msg;
    private DataBean data;

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

    public DataBean getData() {
        return data;
    }

    public void setData(DataBean data) {
        this.data = data;
    }

    public static class DataBean {
        /**
         * delete : 0
         * video_name : 0002.mp4
         */

        private String delete;
        private String video_name;

        public String getDelete() {
            return delete;
        }

        public void setDelete(String delete) {
            this.delete = delete;
        }

        public String getVideo_name() {
            return video_name;
        }

        public void setVideo_name(String video_name) {
            this.video_name = video_name;
        }
    }
}
