package com.lcsd.dongzhi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2017/12/22.
 */
public class UserInfo implements Serializable {

    private	String	point;	/*0*/
    private	String	honor;	/*初学弟子*/
    private	String	alias;	/**/
    private	Integer	gender;	/*0*/
    private	String	user_id;	/*34*/
    private	String	avatar;	/*http://dongzhi.5kah.com/images/avatar.gif*/
    private	String	fullname;	/**/
    private	String	mobile;	/*18110574434*/

    public void setPoint(String value){
        this.point = value;
    }
    public String getPoint(){
        return this.point;
    }

    public void setHonor(String value){
        this.honor = value;
    }
    public String getHonor(){
        return this.honor;
    }

    public void setAlias(String value){
        this.alias = value;
    }
    public String getAlias(){
        return this.alias;
    }

    public void setGender(Integer value){
        this.gender = value;
    }
    public Integer getGender(){
        return this.gender;
    }

    public void setUser_id(String value){
        this.user_id = value;
    }
    public String getUser_id(){
        return this.user_id;
    }

    public void setAvatar(String value){
        this.avatar = value;
    }
    public String getAvatar(){
        return this.avatar;
    }

    public void setFullname(String value){
        this.fullname = value;
    }
    public String getFullname(){
        return this.fullname;
    }

    public void setMobile(String value){
        this.mobile = value;
    }
    public String getMobile(){
        return this.mobile;
    }
}
