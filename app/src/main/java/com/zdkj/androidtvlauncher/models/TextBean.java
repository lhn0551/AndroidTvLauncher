package com.zdkj.androidtvlauncher.models;

import java.util.List;

public class TextBean {

    /**
     * code : 200
     * msg : 文字广告列表
     * data : [{"txt":"6月11日0时-6月14日7时，北京共出现51例确诊病例，均与新发地市场有关联。14日晚11时许，上海复旦大学附属华山医院感染科主任张文宏就此事发表评论。他认为，精准防控不伤全局，将是中国抗疫常态。","txt_id":1}]
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
         * txt : 6月11日0时-6月14日7时，北京共出现51例确诊病例，均与新发地市场有关联。14日晚11时许，上海复旦大学附属华山医院感染科主任张文宏就此事发表评论。他认为，精准防控不伤全局，将是中国抗疫常态。
         * txt_id : 1
         */

        private String txt;
        private int txt_id;

        public String getTxt() {
            return txt;
        }

        public void setTxt(String txt) {
            this.txt = txt;
        }

        public int getTxt_id() {
            return txt_id;
        }

        public void setTxt_id(int txt_id) {
            this.txt_id = txt_id;
        }
    }
}
