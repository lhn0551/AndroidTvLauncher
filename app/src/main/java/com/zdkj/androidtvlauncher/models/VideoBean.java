package com.zdkj.androidtvlauncher.models;

import java.io.Serializable;
import java.util.List;

public class VideoBean implements Serializable {

    /**
     * code : 200
     * msg : 视频列表
     * data : [{"video_id":1,"video_url":"http://ad.zadtek.com/uploads/video/0001.mp4","is_update":1},{"video_id":2,"video_url":"http://ad.zadtek.com/uploads/video/0002.mp4","is_update":1},{"video_id":3,"video_url":"http://ad.zadtek.com/uploads/video/0003.mp4","is_update":1},{"video_id":4,"video_url":"http://ad.zadtek.com/uploads/video/0004.mp4","is_update":1}]
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

    public static class DataBean implements Serializable {
        /**
         * video_id : 1
         * video_url : http://ad.zadtek.com/uploads/video/0001.mp4
         * is_update : 1
         */

        private String video_id;
        private String video_url;

        private String video_name;
        private String local_path;

        public String getLocal_path() {
            return local_path;
        }

        public void setLocal_path(String local_path) {
            this.local_path = local_path;
        }

        public String getVideo_name() {
            return video_name;
        }

        public void setVideo_name(String video_name) {
            this.video_name = video_name;
        }

        public String getVideo_id() {
            return video_id;
        }

        public void setVideo_id(String video_id) {
            this.video_id = video_id;
        }

        public String getVideo_url() {
            return video_url;
        }

        public void setVideo_url(String video_url) {
            this.video_url = video_url;
        }


    }
}
