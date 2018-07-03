package com.lcsd.dongzhi.entity;

/**
 * Created by Administrator on 2017/9/1.
 */
public class NewsTitle {
    private	String	id;	/*8*/
    private	String	title;	/*今日头条*/
    private	String	ico;	/**/
    private	String	identifier;	/*guoji*/

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

    public void setIco(String value){
        this.ico = value;
    }
    public String getIco(){
        return this.ico;
    }

    public void setIdentifier(String value){
        this.identifier = value;
    }
    public String getIdentifier(){
        return this.identifier;
    }
}
