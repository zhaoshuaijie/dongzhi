package com.lcsd.dongzhi.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/6.
 */
public class Wxjx {
    public static class TList{

        private	String	id;	/*wechat_20180306006564*/
        private	String	title;	/*为什么现金贷监管会强调KYC？*/
        private	String	source;	/*金融混业观察*/
        private	String	firstImg;	/*http://zxpic.gtimg.com/infonew/0/wechat_pics_-62817740.jpg/640*/
        private	String	mark;	/**/
        private	String	url;	/*http://v.juhe.cn/weixin/redirect?wid=wechat_20180306006564*/

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

        public void setSource(String value){
            this.source = value;
        }
        public String getSource(){
            return this.source;
        }

        public void setFirstImg(String value){
            this.firstImg = value;
        }
        public String getFirstImg(){
            return this.firstImg;
        }

        public void setMark(String value){
            this.mark = value;
        }
        public String getMark(){
            return this.mark;
        }

        public void setUrl(String value){
            this.url = value;
        }
        public String getUrl(){
            return this.url;
        }

    }
    private List<TList> list;	/*List<TList>*/
    public void setList(List<TList> value){
        this.list = value;
    }
    public List<TList> getList(){
        return this.list;
    }

    private	Integer	pno;	/*1*/
    private	Integer	totalPage;	/*11311*/
    private	Integer	ps;	/*20*/

    public void setPno(Integer value){
        this.pno = value;
    }
    public Integer getPno(){
        return this.pno;
    }

    public void setTotalPage(Integer value){
        this.totalPage = value;
    }
    public Integer getTotalPage(){
        return this.totalPage;
    }

    public void setPs(Integer value){
        this.ps = value;
    }
    public Integer getPs(){
        return this.ps;
    }
}
