package com.lcsd.dongzhi.entity;

/**
 * Created by Administrator on 2018/3/12.
 */
public class Jokes {
    private String content;	/*打球是个高危险的行业*/
    private Integer unixtime;	/*1421353105*/
    private String hashId;	/*ECA2E853965E471FF10B10C083C07C41*/
    private String url;	/*http://juheimg.oss-cn-hangzhou.aliyuncs.com/joke/201501/16/ECA2E853965E471FF10B10C083C07C41.jpg*/

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public Integer getUnixtime() {
        return unixtime;
    }

    public void setUnixtime(Integer unixtime) {
        this.unixtime = unixtime;
    }

    public String getHashId() {
        return hashId;
    }

    public void setHashId(String hashId) {
        this.hashId = hashId;
    }

    public String getUrl() {
        return url;
    }

    public void setUrl(String url) {
        this.url = url;
    }
}
