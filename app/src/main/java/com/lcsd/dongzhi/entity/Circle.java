package com.lcsd.dongzhi.entity;

import java.io.Serializable;
import java.util.List;

/**
 * Created by Administrator on 2017/12/4.
 *
 * 拍客圈
 */
public class Circle implements Serializable {

    public static class TRslist {

        private List<String> pictures;	/*List<String>*/

        public void setPictures(List<String> value) {
            this.pictures = value;
        }

        public List<String> getPictures() {
            return this.pictures;
        }

        public List<PaikeCommentlist> getComment() {
            return comment;
        }

        public void setComment(List<PaikeCommentlist> comment) {
            this.comment = comment;
        }

        public String getVideo_cover() {
            return video_cover;
        }

        public void setVideo_cover(String video_cover) {
            this.video_cover = video_cover;
        }

        //点赞列表
        public static class TZan_list {

            private String id;	/*40*/
            private String avatar;	/*http://www.ahft.tv/res/app/c6cc22e78b1cb746.png*/
            private String user;	/**/

            public void setId(String value) {
                this.id = value;
            }

            public String getId() {
                return this.id;
            }

            public void setAvatar(String value) {
                this.avatar = value;
            }

            public String getAvatar() {
                return this.avatar;
            }

            public void setUser(String value) {
                this.user = value;
            }

            public String getUser() {
                return this.user;
            }

        }

        private List<TZan_list> zan_list;	/*List<TZan_list>*/

        public void setZan_list(List<TZan_list> value) {
            this.zan_list = value;
        }

        public List<TZan_list> getZan_list() {
            return this.zan_list;
        }

        private String alias;	/*郁先生*/
        private String cate_url;	/**/
        private String cate_id;	/*0*/
        private String dateline;	/*1518387724*/
        private String avatar;	/*http://www.ahft.tv/res/avatar/d9c46749906ccd77.jpg*/
        private String id;	/*4805*/
        private String hits;	/*0*/
        private String title;	/*请看这副春联*/
        private String shareurl;	/*http://www.ahft.tv/app/index.php?id=4805*/
        private Integer zan;	/*1*/
        private String address;	/**/
        private Integer is_zan;	/*1*/
        private String writer;	/*郁先生*/
        private String thumb;	/**/
        private String video;	/**/
        private String cate_name;	/**/
        private String video_cover; /*视频第一帧封面*/
        private List<PaikeCommentlist> comment;

        public void setAlias(String value) {
            this.alias = value;
        }

        public String getAlias() {
            return this.alias;
        }

        public void setCate_url(String value) {
            this.cate_url = value;
        }

        public String getCate_url() {
            return this.cate_url;
        }

        public void setCate_id(String value) {
            this.cate_id = value;
        }

        public String getCate_id() {
            return this.cate_id;
        }

        public void setDateline(String value) {
            this.dateline = value;
        }

        public String getDateline() {
            return this.dateline;
        }

        public void setAvatar(String value) {
            this.avatar = value;
        }

        public String getAvatar() {
            return this.avatar;
        }

        public void setId(String value) {
            this.id = value;
        }

        public String getId() {
            return this.id;
        }

        public void setHits(String value) {
            this.hits = value;
        }

        public String getHits() {
            return this.hits;
        }

        public void setTitle(String value) {
            this.title = value;
        }

        public String getTitle() {
            return this.title;
        }

        public void setShareurl(String value) {
            this.shareurl = value;
        }

        public String getShareurl() {
            return this.shareurl;
        }

        public void setZan(Integer value) {
            this.zan = value;
        }

        public Integer getZan() {
            return this.zan;
        }

        public void setAddress(String value) {
            this.address = value;
        }

        public String getAddress() {
            return this.address;
        }

        public void setIs_zan(Integer value) {
            this.is_zan = value;
        }

        public Integer getIs_zan() {
            return this.is_zan;
        }

        public void setWriter(String value) {
            this.writer = value;
        }

        public String getWriter() {
            return this.writer;
        }

        public void setThumb(String value) {
            this.thumb = value;
        }

        public String getThumb() {
            return this.thumb;
        }

        public void setVideo(String value) {
            this.video = value;
        }

        public String getVideo() {
            return this.video;
        }

        public void setCate_name(String value) {
            this.cate_name = value;
        }

        public String getCate_name() {
            return this.cate_name;
        }

    }

    private List<TRslist> rslist;	/*List<TRslist>*/

    public void setRslist(List<TRslist> value) {
        this.rslist = value;
    }

    public List<TRslist> getRslist() {
        return this.rslist;
    }

    private Integer total;	/*6*/
    private String psize;	/*10*/
    private Integer pageid;	/*1*/

    public void setTotal(Integer value) {
        this.total = value;
    }

    public Integer getTotal() {
        return this.total;
    }

    public void setPsize(String value) {
        this.psize = value;
    }

    public String getPsize() {
        return this.psize;
    }

    public void setPageid(Integer value) {
        this.pageid = value;
    }

    public Integer getPageid() {
        return this.pageid;
    }

}
