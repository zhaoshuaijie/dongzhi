package com.lcsd.dongzhi.entity;

import java.util.List;

/**
 * Created by Administrator on 2016/9/12.
 */
public class CommentInfoList {

    public static class TRslist{

        private	String	content;	/*很好*/
        private	String	uid;	/*35*/
        private	String	id;	/*58*/
        private	String	addtime;	/*1506476110*/
        private	String	adm_content;	/**/
        private	String	alias;	/*啦啦啦*/
        private	String	adm_time;	/*0*/
        private	String	admin_id;	/*0*/
        private	String	tid;	/*2466*/
        private	String	avatar;	/*http://www.ahft.tv/images/avatar.gif*/
        private	String	ip;	/*36.5.156.139*/

        public void setContent(String value){
            this.content = value;
        }
        public String getContent(){
            return this.content;
        }

        public void setUid(String value){
            this.uid = value;
        }
        public String getUid(){
            return this.uid;
        }

        public void setId(String value){
            this.id = value;
        }
        public String getId(){
            return this.id;
        }

        public void setAddtime(String value){
            this.addtime = value;
        }
        public String getAddtime(){
            return this.addtime;
        }

        public void setAdm_content(String value){
            this.adm_content = value;
        }
        public String getAdm_content(){
            return this.adm_content;
        }

        public void setAlias(String value){
            this.alias = value;
        }
        public String getAlias(){
            return this.alias;
        }

        public void setAdm_time(String value){
            this.adm_time = value;
        }
        public String getAdm_time(){
            return this.adm_time;
        }

        public void setAdmin_id(String value){
            this.admin_id = value;
        }
        public String getAdmin_id(){
            return this.admin_id;
        }

        public void setTid(String value){
            this.tid = value;
        }
        public String getTid(){
            return this.tid;
        }

        public void setAvatar(String value){
            this.avatar = value;
        }
        public String getAvatar(){
            return this.avatar;
        }

        public void setIp(String value){
            this.ip = value;
        }
        public String getIp(){
            return this.ip;
        }

    }
    private	List<TRslist>	rslist;	/*List<TRslist>*/
    public void setRslist(List<TRslist> value){
        this.rslist = value;
    }
    public List<TRslist> getRslist(){
        return this.rslist;
    }

    private	String	total;	/*2*/
    private	String	psize;	/*10*/
    private	Integer	totalpage;	/*1*/
    private	Integer	pageid;	/*1*/

    public void setTotal(String value){
        this.total = value;
    }
    public String getTotal(){
        return this.total;
    }

    public void setPsize(String value){
        this.psize = value;
    }
    public String getPsize(){
        return this.psize;
    }

    public void setTotalpage(Integer value){
        this.totalpage = value;
    }
    public Integer getTotalpage(){
        return this.totalpage;
    }

    public void setPageid(Integer value){
        this.pageid = value;
    }
    public Integer getPageid(){
        return this.pageid;
    }

}
