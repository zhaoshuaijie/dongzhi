package com.lcsd.dongzhi.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/14.
 */
public class DianBoInfo {
    public static class TCate{

        private	String	id;	/*211*/
        private	String	title;	/*电视新闻*/
        private	String	url;	/*http://dongzhi.5kah.com/app/index.php?id=demand&cate=dianshixinwen&pageid=1&psize=10*/

        public void setId(String value){
            this.id = value;
        }
        public String getId(){
            return this.id;
        }

        public void setTitle(String value){
            this.title = value;
        }
        public String getTitle(){
            return this.title;
        }

        public void setUrl(String value){
            this.url = value;
        }
        public String getUrl(){
            return this.url;
        }

    }
    public static class TRs_lists{

        private	String	cate_url;	/*http://dongzhi.5kah.com/app/index.php?id=demand&cate=dianshixinwen*/
        private	String	cate_id;	/*211*/
        private	String	attr;	/**/
        private	String	dateline;	/*1513304639*/
        private	String	url;	/*http://dongzhi.5kah.com/app/index.php?id=1886*/
        private	String	id;	/*1886*/
        private	String	title;	/*东至新闻2017年12月12日*/
        private	String	hits;	/*11*/
        private	String	source;	/**/
        private	Integer	zan;	/*0*/
        private	Integer	is_zan;	/*0*/
        private	String	writer;	/**/
        private	String	thumb;	/*http://dongzhi.5kah.com/res/img/201712/14/4e2194f3dbdc891b.jpg*/
        private	String	note;	/**/
        private	String	video;	/*http://dongzhi.5kah.com/res/videonews/201712/15/8d24f200a7fd815e.mpg*/
        private String ovideo;
        private	String	cate_name;	/*电视新闻*/

        public void setCate_url(String value){
            this.cate_url = value;
        }
        public String getCate_url(){
            return this.cate_url;
        }

        public void setCate_id(String value){
            this.cate_id = value;
        }
        public String getCate_id(){
            return this.cate_id;
        }

        public void setAttr(String value){
            this.attr = value;
        }
        public String getAttr(){
            return this.attr;
        }

        public void setDateline(String value){
            this.dateline = value;
        }
        public String getDateline(){
            return this.dateline;
        }

        public void setUrl(String value){
            this.url = value;
        }
        public String getUrl(){
            return this.url;
        }

        public void setId(String value){
            this.id = value;
        }
        public String getId(){
            return this.id;
        }

        public void setTitle(String value){
            this.title = value;
        }
        public String getTitle(){
            return this.title;
        }

        public void setHits(String value){
            this.hits = value;
        }
        public String getHits(){
            return this.hits;
        }

        public void setSource(String value){
            this.source = value;
        }
        public String getSource(){
            return this.source;
        }

        public void setZan(Integer value){
            this.zan = value;
        }
        public Integer getZan(){
            return this.zan;
        }

        public void setIs_zan(Integer value){
            this.is_zan = value;
        }
        public Integer getIs_zan(){
            return this.is_zan;
        }

        public void setWriter(String value){
            this.writer = value;
        }
        public String getWriter(){
            return this.writer;
        }

        public void setThumb(String value){
            this.thumb = value;
        }
        public String getThumb(){
            return this.thumb;
        }

        public void setNote(String value){
            this.note = value;
        }
        public String getNote(){
            return this.note;
        }

        public void setVideo(String value){
            this.video = value;
        }
        public String getVideo(){
            return this.video;
        }

        public void setCate_name(String value){
            this.cate_name = value;
        }
        public String getCate_name(){
            return this.cate_name;
        }

        public String getOvideo() {
            return ovideo;
        }

        public void setOvideo(String ovideo) {
            this.ovideo = ovideo;
        }
    }
    private	List<TRs_lists>	rs_lists;	/*List<TRs_lists>*/
    public void setRs_lists(List<TRs_lists> value){
        this.rs_lists = value;
    }
    public List<TRs_lists> getRs_lists(){
        return this.rs_lists;
    }

    private	Integer	total;	/*1*/
    private	TCate	cate;	/*TCate*/
    private	Integer	psize;	/*10*/
    private	String	pageid;	/*1*/

    public void setTotal(Integer value){
        this.total = value;
    }
    public Integer getTotal(){
        return this.total;
    }

    public void setCate(TCate value){
        this.cate = value;
    }
    public TCate getCate(){
        return this.cate;
    }

    public void setPsize(Integer value){
        this.psize = value;
    }
    public Integer getPsize(){
        return this.psize;
    }

    public void setPageid(String value){
        this.pageid = value;
    }
    public String getPageid(){
        return this.pageid;
    }

}
