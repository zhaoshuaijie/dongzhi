package com.lcsd.dongzhi.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class Ls2 {
    public static class TResult{

        public static class TPicUrl{

            private Integer id;	/*1*/
            private String pic_title;	/**/
            private String url;	/*http://images.juheapi.com/history/2727_1.jpg*/

            public void setId(Integer value){
                this.id = value;
            }
            public Integer getId(){
                return this.id;
            }

            public void setPic_title(String value){
                this.pic_title = value;
            }
            public String getPic_title(){
                return this.pic_title;
            }

            public void setUrl(String value){
                this.url = value;
            }
            public String getUrl(){
                return this.url;
            }

        }
        private List<TPicUrl> picUrl;	/*List<TPicUrl>*/
        public void setPicUrl(List<TPicUrl> value){
            this.picUrl = value;
        }
        public List<TPicUrl> getPicUrl(){
            return this.picUrl;
        }

        private String content;	/*    在104年前的今天，1912年2月29日 (农历正月十二)，袁世凯指使曹锟的部下制造了“北京兵变”。
    1912年2月临时参议院选举袁世凯为临时大总统，孙中山提出临时政府应设于南京 ，新总统必须亲到南京受任，遵守《临时约法》三个条件，借以约束袁世凯，并排专使蔡元培，宋教仁等赴北京迎袁南下就职。29日，袁世凯指使第三镇统制曹锟发动兵变，纵兵在北京焚烧抢掠，商民受害四千余家，专使寓舍亦被围攻，蔡，宋等人避入六国饭店。次日兵变又波及西城，并蔓延于保定，天津一带。各帝国主义国家借口保护侨民纷纷调兵入京。史称“北京兵变”。袁世凯以此为借口公然拒绝南下，于3月10日在北京就任临时大总统。

*/
        private String title;	/*袁世凯指使曹锟的部下制造了“北京兵变”*/
        private String e_id;	/*2727*/
        private String picNo;	/*1*/

        public void setContent(String value){
            this.content = value;
        }
        public String getContent(){
            return this.content;
        }

        public void setTitle(String value){
            this.title = value;
        }
        public String getTitle(){
            return this.title;
        }

        public void setE_id(String value){
            this.e_id = value;
        }
        public String getE_id(){
            return this.e_id;
        }

        public void setPicNo(String value){
            this.picNo = value;
        }
        public String getPicNo(){
            return this.picNo;
        }

    }
    private List<TResult> result;	/*List<TResult>*/
    public void setResult(List<TResult> value){
        this.result = value;
    }
    public List<TResult> getResult(){
        return this.result;
    }

    private String reason;	/*success*/
    private Integer error_code;	/*0*/

    public void setReason(String value){
        this.reason = value;
    }
    public String getReason(){
        return this.reason;
    }

    public void setError_code(Integer value){
        this.error_code = value;
    }
    public Integer getError_code(){
        return this.error_code;
    }
}
