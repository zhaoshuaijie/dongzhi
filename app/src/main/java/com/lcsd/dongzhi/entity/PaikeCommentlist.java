package com.lcsd.dongzhi.entity;

import java.io.Serializable;

/**
 * Created by Administrator on 2018/2/24.
 */
public class PaikeCommentlist implements Serializable{
    private String content;	/*厉害了哦*/
    private String uid;	/*40*/
    private String id;	/*238*/
    private String addtime;	/*1519367662*/
    private String adm_content;	/**/
    private String alias;	/*我得*/
    private String adm_time;	/*0*/
    private String admin_id;	/*0*/
    private String tid;	/*4820*/
    private String avatar;	/*http://www.ahft.tv/res/app/c6cc22e78b1cb746.png*/
    private String ip;	/*36.5.152.197*/
    private Parent parent;

    public void setContent(String value) {
        this.content = value;
    }

    public String getContent() {
        return this.content;
    }

    public void setUid(String value) {
        this.uid = value;
    }

    public String getUid() {
        return this.uid;
    }

    public void setId(String value) {
        this.id = value;
    }

    public String getId() {
        return this.id;
    }

    public void setAddtime(String value) {
        this.addtime = value;
    }

    public String getAddtime() {
        return this.addtime;
    }

    public void setAdm_content(String value) {
        this.adm_content = value;
    }

    public String getAdm_content() {
        return this.adm_content;
    }

    public void setAlias(String value) {
        this.alias = value;
    }

    public String getAlias() {
        return this.alias;
    }

    public void setAdm_time(String value) {
        this.adm_time = value;
    }

    public String getAdm_time() {
        return this.adm_time;
    }

    public void setAdmin_id(String value) {
        this.admin_id = value;
    }

    public String getAdmin_id() {
        return this.admin_id;
    }

    public void setTid(String value) {
        this.tid = value;
    }

    public String getTid() {
        return this.tid;
    }

    public void setAvatar(String value) {
        this.avatar = value;
    }

    public String getAvatar() {
        return this.avatar;
    }

    public void setIp(String value) {
        this.ip = value;
    }

    public String getIp() {
        return this.ip;
    }

    public Parent getParent() {
        return parent;
    }

    public void setParent(Parent parent) {
        this.parent = parent;
    }
}
