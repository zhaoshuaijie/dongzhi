package com.lcsd.dongzhi.entity;

import java.util.List;

/**
 * Created by Administrator on 2018/3/7.
 */
public class NewsTop {
    public static class TData{

        private	String	category;	/*娱乐*/
        private	String	title;	/*戚薇妆容精致对着镜头大摆pose，像一个魅惑而灵动的暗夜精灵*/
        private	String	thumbnail_pic_s03;	/*http://06.imgmini.eastday.com/mobile/20180307/20180307101547_5ad5bcff477b111bae911a241c6a9855_3_mwpm_03200403.jpg*/
        private	String	thumbnail_pic_s02;	/*http://06.imgmini.eastday.com/mobile/20180307/20180307101547_5ad5bcff477b111bae911a241c6a9855_2_mwpm_03200403.jpg*/
        private	String	thumbnail_pic_s;	/*http://06.imgmini.eastday.com/mobile/20180307/20180307101547_5ad5bcff477b111bae911a241c6a9855_1_mwpm_03200403.jpg*/
        private	String	uniquekey;	/*5f213fbd44ebf657ecb817acca9a0a8f*/
        private	String	author_name;	/*明星星动态*/
        private	String	date;	/*2018-03-07 10:15*/
        private	String	url;	/*http://mini.eastday.com/mobile/180307101547036.html*/

        public void setCategory(String value){
            this.category = value;
        }
        public String getCategory(){
            return this.category;
        }

        public void setTitle(String value){
            this.title = value;
        }
        public String getTitle(){
            return this.title;
        }

        public void setThumbnail_pic_s03(String value){
            this.thumbnail_pic_s03 = value;
        }
        public String getThumbnail_pic_s03(){
            return this.thumbnail_pic_s03;
        }

        public void setThumbnail_pic_s02(String value){
            this.thumbnail_pic_s02 = value;
        }
        public String getThumbnail_pic_s02(){
            return this.thumbnail_pic_s02;
        }

        public void setUniquekey(String value){
            this.uniquekey = value;
        }
        public String getUniquekey(){
            return this.uniquekey;
        }

        public void setAuthor_name(String value){
            this.author_name = value;
        }
        public String getAuthor_name(){
            return this.author_name;
        }

        public void setThumbnail_pic_s(String value){
            this.thumbnail_pic_s = value;
        }
        public String getThumbnail_pic_s(){
            return this.thumbnail_pic_s;
        }

        public void setDate(String value){
            this.date = value;
        }
        public String getDate(){
            return this.date;
        }

        public void setUrl(String value){
            this.url = value;
        }
        public String getUrl(){
            return this.url;
        }

    }
    private	List<TData>	data;	/*List<TData>*/
    public void setData(List<TData> value){
        this.data = value;
    }
    public List<TData> getData(){
        return this.data;
    }

    private	String	stat;	/*1*/

    public void setStat(String value){
        this.stat = value;
    }
    public String getStat(){
        return this.stat;
    }
}
