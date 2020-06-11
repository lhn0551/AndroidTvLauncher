package com.zdkj.androidtvlauncher.models;

import java.util.List;

public class LiveSourceBean {

    /**
     * code : 200
     * msg : 直播源列表
     * data : {"live":[{"name":"CCTV1综合","url":"http://liveplay-kk.rtxapp.com/live/program/live/cctv1hd/4000000/mnf.m3u8"},{"name":"CCTV-2财经","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=14&amp;FvSeid=1&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;Provider_id=&amp;Pcontent_id=.m3u8"},{"name":"CCTV-3综艺","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=28&amp;FvSeid=1&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;Provider_id=&amp;Pcontent_id=.m3u8"},{"name":"CCTV-4中文国际","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=30&amp;FvSeid=1&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;Provider_id=&amp;Pcontent_id=.m3u8"},{"name":"CCTV-5体育","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=29&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-6电影","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=31&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-7国防军事","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=32&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-8电视剧","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=34&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-9记录","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=35&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-10科教","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=36&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-11戏曲","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=33&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-13新闻","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=38&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-14少儿","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=16&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"CCTV-15蓝光","url":"http://111.13.111.167/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226333/1.m3u8"},{"name":"CCTV-17蓝光","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226318/1.m3u8"},{"name":"CCTV-4K超高清","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=13&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"湖南卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=1&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"北京卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=4&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"上海东方","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=6&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"浙江卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=7&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"江苏卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=8&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"深圳卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=22&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"安徽卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=23&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"湖北卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=37&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"江西卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=41&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"东南卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=42&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"四川卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=43&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"贵州卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=44&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"山东卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=46&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"天津卫视蓝光","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226246/1.m3u8"},{"name":"河北卫视蓝光","url":"http://liveplay-kk.rtxapp.com/live/program/live/hbwshd/4000000/mnf.m3u8"},{"name":"广东卫视","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=5&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"深圳卫视蓝光","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226245/1.m3u8"},{"name":"辽宁卫视蓝光","url":"http://ott.fj.chinamobile.com/PLTV/88888888/224/3221225947/1.m3u8"},{"name":"黑龙江卫视蓝光","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226239/1.m3u8"},{"name":"吉林卫视蓝光","url":"http://116.199.5.52:8114/00000000/index.m3u8?http://116.199.5.52:8114/00000000/index.m3u8?&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;FvSeid=5abd1660af1babb4&amp;Fsv_filetype=1&amp;Fsv_ctype=LIVES&amp;Fsv_cid=0&amp;Fsv_chan_hls_se_idx=25&amp;Fsv_rate_id=0&amp;Fsv_SV_PARAM1=0&amp;Fsv_ShiftEnable=0&amp;Fsv_ShiftTsp=0&amp;Provider_id=&amp;Pcontent_id=&amp;Fsv_CMSID=&amp;Fsv_otype=1"},{"name":"厦门卫视蓝光","url":"http://ott.fj.chinamobile.com/PLTV/88888888/224/3221226781/1.m3u8"},{"name":"河南卫视蓝光","url":"http://116.199.5.52:8114/00000000/index.m3u8?&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;FvSeid=5abd1660af1babb4&amp;Fsv_filetype=1&amp;Fsv_ctype=LIVES&amp;Fsv_cid=0&amp;Fsv_chan_hls_se_idx=19&amp;Fsv_rate_id=0&amp;Fsv_SV_PARAM1=0&amp;Fsv_ShiftEnable=0&amp;Fsv_ShiftTsp=0&amp;Provider_id=&amp;Pcontent_id=&amp;Fsv_CMSID=&amp;Fsv_otype=1"},{"name":"贵州卫视蓝光","url":"http://116.199.5.52:8114/00000000/index.m3u8?&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;FvSeid=5abd1660af1babb4&amp;Fsv_filetype=1&amp;Fsv_ctype=LIVES&amp;Fsv_cid=0&amp;Fsv_chan_hls_se_idx=41&amp;Fsv_rate_id=0&amp;Fsv_SV_PARAM1=0&amp;Fsv_ShiftEnable=0&amp;Fsv_ShiftTsp=0&amp;Provider_id=&amp;Pcontent_id=&amp;Fsv_CMSID=&amp;Fsv_otype=1"},{"name":"海南卫视高清","url":"http://112.50.243.8/PLTV/88888888/224/3221225855/1.m3u8"},{"name":"甘肃卫视超清","url":"http://116.199.5.52:8114/00000000/index.m3u8?&amp;Fsv_ctype=LIVES&amp;Fsv_otype=1&amp;FvSeid=5abd1660af1babb4&amp;Fsv_filetype=1&amp;Fsv_ctype=LIVES&amp;Fsv_cid=0&amp;Fsv_chan_hls_se_idx=142&amp;Fsv_rate_id=0&amp;Fsv_SV_PARAM1=0&amp;Fsv_ShiftEnable=0&amp;Fsv_ShiftTsp=0&amp;Provider_id=&amp;Pcontent_id=&amp;Fsv_CMSID=&amp;Fsv_otype=1"},{"name":"云南卫视超清","url":"http://223.110.245.159/ott.js.chinamobile.com/PLTV/3/224/3221225838/index.m3u8"},{"name":"陕西卫视高清","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221225896/1.m3u8"},{"name":"宁夏卫视超清","url":"http://223.110.245.151/ott.js.chinamobile.com/PLTV/3/224/3221225842/index.m3u8"},{"name":"内蒙卫视超清","url":"http://223.110.245.161/ott.js.chinamobile.com/PLTV/3/224/3221225836/index.m3u8"},{"name":"西藏卫视高清","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221225900/1.m3u8"},{"name":"山西卫视高清","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221225895/1.m3u8"},{"name":"广西卫视高清","url":"http://112.50.243.8/PLTV/88888888/224/3221225836/1.m3u8"},{"name":"新疆卫视高清","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221225901/1.m3u8"},{"name":"TVB 翡翠台","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=10&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"TVB 明珠台","url":"http://116.199.5.52:8114/00000000/index.m3u8?&Fsv_ctype=LIVES&Fsv_otype=1&FvSeid=5abd1660af1babb4&Fsv_filetype=1&Fsv_ctype=LIVES&Fsv_cid=0&Fsv_chan_hls_se_idx=12&Fsv_rate_id=0&Fsv_SV_PARAM1=0&Fsv_ShiftEnable=0&Fsv_ShiftTsp=0&Provider_id=&Pcontent_id=&Fsv_CMSID=&Fsv_otype=1"},{"name":"HKS卫视 高清","url":"http://zhibo.hkstv.tv/livestream/mutfysrq/playlist.m3u8#http://zhibo.hkstv.tv/livestream/mutfysrq.flv"},{"name":"香港卫视","url":"http://zhibo.hkstv.tv/livestream/mutfysrq/playlist.m3u8"},{"name":"凤凰卫视中文台","url":"http://112.17.40.140/PLTV/88888888/224/3221226368/index.m3u8"},{"name":"无线新闻","url":"http://www.liveviptv.xyz/tv/tvb.php?vid=wxxw"},{"name":"亚旅卫视","url":"http://hls.jingchangkan.tv/jingchangkan/156722438_0HaM/index.m3u8"},{"name":"赛马频道","url":"http://tvbilive11-i.akamaihd.net/hls/live/494672/CH88/CH88-01.m3u8"},{"name":"世纪综合","url":"http://61.58.60.230:9319/live/49.m3u8"},{"name":"年代新聞","url":"http://192.154.103.75:8080/ZZ_niandaixinwen/ZZ_niandianxinwen.m3u8"},{"name":"八大綜合","url":"http://192.154.103.75:8080//ZZ_zhongtianyazhou/ZZ_zhongtianyazhou.m3u8"},{"name":"华视综合","url":"http://192.154.103.75:8080//ZZ_huashi/ZZ_huashi.m3u8"},{"name":"国家地理","url":"http://104.250.154.42:8080/ZZ_hbo/ZZ_hbo.m3u8"},{"name":"百事通台","url":"http://112.17.40.145/PLTV/88888888/224/3221226596/index.m3u8"},{"name":"MTV音乐","url":"http://116.199.5.52:8114/00000000/index.m3u8?&Fsv_ctype=LIVES&Fsv_otype=1&FvSeid=5abd1660af1babb4&Fsv_filetype=1&Fsv_ctype=LIVES&Fsv_cid=0&Fsv_chan_hls_se_idx=202&Fsv_rate_id=0&Fsv_SV_PARAM1=0&Fsv_ShiftEnable=0&Fsv_ShiftTsp=0&Provider_id=&Pcontent_id=&Fsv_CMSID=&Fsv_otype=1"},{"name":"HBO2台","url":"http://161.0.157.5/PLTV/88888888/224/3221227026/03.m3u8？fluxustv.m3u8"},{"name":"动物星球","url":"http://192.200.120.82:8080/ZZ_dongwuxingqiu/ZZ_dongwuxingqiu.m3u8"},{"name":"动物世界","url":"http://hls-ott-zhibo.wasu.tv/live/394/index.m3u8"},{"name":"好莱坞电影","url":"http://192.200.120.82:8080/ZZ_haolaiwu/ZZ_haolaiwu.m3u8"},{"name":"北京卫视","url":"http://httpdvb.slave.homed.hrtn.net/playurl?playtype=live&protocol=hls&accesstoken=R5D22D2B7U309E0093K7735BBEDIAC2DC601PBM3187915V10453Z6B7EDWE3620470C71&&playtoken=&programid=4200000149.m3u8"},{"name":"北京文艺","url":"http://ivi.bupt.edu.cn/hls/btv2hd.m3u8"},{"name":"北京新闻","url":"http://ivi.bupt.edu.cn/hls/btv9.m3u8"},{"name":"北京影视","url":"http://ivi.bupt.edu.cn/hls/btv4.m3u8"},{"name":"广州影视","url":"http://aplay.gztv.com/sec/yingshi.flv"},{"name":"珠江电影","url":"http://116.199.5.58:8114/index.m3u8?3Fsv_chan_hls_se_idx=39&FvSeid=1&Fsv_ctype=LIVES&Fsv_otype=1&Provider_id=&Pcontent_id=.m3u8"},{"name":"动作电影","url":"http://111.13.111.242/otttv.bj.chinamobile.com/PLTV/88888888/224/3221226288/1.m3u8#http://baiducdnct.inter.iqiyi.com/tslive/c16_lb_dongzuodianying_1080p_t10/c16_lb_dongzuodianying_1080p_t10.m3u8"},{"name":"辽宁家政","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000215.m3u8"},{"name":"辽宁公共","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000064.m3u8"},{"name":"辽宁都市","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000026.m3u8"},{"name":"辽宁北方","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000030.m3u8"},{"name":"辽宁教育青少","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000191.m3u8"},{"name":"辽宁经济","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000188.m3u8"},{"name":"辽宁生活","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000190.m3u8"},{"name":"辽宁体育","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000028.m3u8"},{"name":"辽宁瓦房店经济生活","url":"http://pili-live-hls.livewafangd.sobeycache.com/livewafangd/dszb002.m3u8"},{"name":"辽宁瓦房店新闻综合","url":"http://pili-live-hls.livewafangd.sobeycache.com/livewafangd/dszb003.m3u8"},{"name":"辽宁卫视","url":"http://httpdvb.slave.homed.hrtn.net/playurl?playtype=live&protocol=hls&accesstoken=R5D22D2B7U309E0093K7735BBEDIAC2DC601PBM3187915V10453Z6B7EDWE3620470C71&&playtoken=&programid=4200000159.m3u8"},{"name":"辽宁影视剧","url":"http://httpdvb.slave.ttcatv.tv:13164/playurl?playtype=live&protocol=hls&accesstoken=R5D2C1A9EU30985010K775FB186I76305A2FPBM3226877V1044EZ33519WE378ACFBBE1&playtoken=ABCDEFGHI&programid=4200000027.m3u8"},{"name":"辽源新闻综合","url":"http://stream2.jlntv.cn/liaoyuan1/sd/live.m3u8"}]}
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
        private List<LiveBean> live;

        public List<LiveBean> getLive() {
            return live;
        }

        public void setLive(List<LiveBean> live) {
            this.live = live;
        }

        public static class LiveBean {
            /**
             * name : CCTV1综合
             * url : http://liveplay-kk.rtxapp.com/live/program/live/cctv1hd/4000000/mnf.m3u8
             */

            private String name;
            private String url;

            public String getName() {
                return name;
            }

            public void setName(String name) {
                this.name = name;
            }

            public String getUrl() {
                return url;
            }

            public void setUrl(String url) {
                this.url = url;
            }
        }
    }
}
