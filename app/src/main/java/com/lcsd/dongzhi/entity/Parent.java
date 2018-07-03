package com.lcsd.dongzhi.entity;

/**
 * Created by Administrator on 2018/2/24.
 */
public class Parent {
    private	String	uid;	/*0*/
    private	String	id;	/*54*/
    private	String	avatar;	/*avatar.gif*/
    private	String	user;	/*游客*/
    private	String	ip;	/*: : 1*/

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

    public void setAvatar(String value){
        this.avatar = value;
    }
    public String getAvatar(){
        return this.avatar;
    }

    public void setUser(String value){
        this.user = value;
    }
    public String getUser(){
        return this.user;
    }

    public void setIp(String value){
        this.ip = value;
    }
    public String getIp(){
        return this.ip;
    }
}
