package com.lcsd.dongzhi.entity;

/**
 * Created by Administrator on 2017/12/22.
 */
public class version {

    private	String	version_desc;	/*更新说明*/
    private	String	version_name;	/*1.5*/
    private	String	version_no;	/*1*/
    private	String	version_url;	/*http://app.ahft.tv/*/

    public void setVersion_desc(String value){
        this.version_desc = value;
    }
    public String getVersion_desc(){
        return this.version_desc;
    }

    public void setVersion_name(String value){
        this.version_name = value;
    }
    public String getVersion_name(){
        return this.version_name;
    }

    public void setVersion_no(String value){
        this.version_no = value;
    }
    public String getVersion_no(){
        return this.version_no;
    }

    public void setVersion_url(String value){
        this.version_url = value;
    }
    public String getVersion_url(){
        return this.version_url;
    }
}
