package com.lcsd.dongzhi.http;

/**
 * Created by Administrator on 2017/12/15.
 */
public class AppConfig {

    public static String Mainurl = "http://223.247.33.124/";

    public static String request_Data = Mainurl + "app/index.php";

    /**
     * 天气
     */
    public static String request_tianqi = "http://wx.weather.com.cn/mweather/101221702.shtml#1";

    /**
     * 聚合接口：微信精选
     * pno:当前页数，默认1
     * ps：每页返回数，默认20，最大50
     * dtype：返回数据的格式,xml或json，默认json
     */
    public static String Wxinjx = "http://v.juhe.cn/weixin/query";
    public static String WxjxKey = "5abe0ce45e7c3f2c28e7276eb80ad432";
    /**
     * 聚合接口：历史上的今天
     *  列表：上传date：	日期,格式:月/日 如:1/1,/10/1,12/12 如月或者日小于10,前面无需加0
     *  详情：e_id：	事件id
     */
    //列表
    public static String Lssdjt1 = "http://v.juhe.cn/todayOnhistory/queryEvent.php";
    //详情
    public static String Lssdjt2 = "http://v.juhe.cn/todayOnhistory/queryDetail.php";
    public static String Lskey = "e8ca5d5b5086224c853c9cf8cb39b924";
    /**
     * 聚合接口：新闻头条
     * type：类型,,top(头条，默认),shehui(社会),guonei(国内),guoji(国际),yule(娱乐),tiyu(体育)junshi(军事),keji(科技),caijing(财经),shishang(时尚)
     */
    public static String NewsTop = "http://v.juhe.cn/toutiao/index";
    public static String NewsTopKey = "024c2210a8cd57d6bdc92c7b55bc1c42";
    /**
     * 聚合：笑话大全（随机获取趣图或笑话）
     * type：	类型(pic:趣图,不传默认为笑话)
     */
    public static String Jokes = "http://v.juhe.cn/joke/randJoke.php";
    public static String JokesKey = "ccb0d24fe484b65e819e85c48c75d8fb";
    /**
     * 聚合：h5在线电影票
     */
    public static String Wepiao = "http://v.juhe.cn/wepiao/query";
    public static String WepiaoKey = "e5667aae72ab1fd9f6901d51ffb31bde";
}
