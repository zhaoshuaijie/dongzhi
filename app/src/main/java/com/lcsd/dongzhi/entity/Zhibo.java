package com.lcsd.dongzhi.entity;

/**
 * Created by Administrator on 2017/12/19.
 */
public class Zhibo {
    private	String	id;	/*168*/
    private	String	title;	/*综合频道*/
    private	String	fluent;	/*http://223.247.33.124:1935/live/zonghe/playlist.m3u8*/
    private	String	ico;	/**/
    private	String	ultra;	/**/
    private	String	high;	/**/
    private	String	thumb;	/*http://dongzhi.5kah.com/res/img/201712/18/e16b0d9a202f34d9.png*/
    private	String	identifier;	/*zonghe*/

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

    public void setFluent(String value){
        this.fluent = value;
    }
    public String getFluent(){
        return this.fluent;
    }

    public void setIco(String value){
        this.ico = value;
    }
    public String getIco(){
        return this.ico;
    }

    public void setUltra(String value){
        this.ultra = value;
    }
    public String getUltra(){
        return this.ultra;
    }

    public void setHigh(String value){
        this.high = value;
    }
    public String getHigh(){
        return this.high;
    }

    public void setThumb(String value){
        this.thumb = value;
    }
    public String getThumb(){
        return this.thumb;
    }

    public void setIdentifier(String value){
        this.identifier = value;
    }
    public String getIdentifier(){
        return this.identifier;
    }

}
