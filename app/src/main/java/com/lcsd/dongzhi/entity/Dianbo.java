package com.lcsd.dongzhi.entity;

/**
 * Created by Administrator on 2017/9/1.
 */
public class Dianbo {
    private	String	id;	/*211*/
    private	String	title;	/*电视新闻*/
    private	String	ico;	/*http://test.ahft.tv/lcsd/1251*/
    private	String	identifier;	/*dianshixinwen*/

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
