package com.lcsd.dongzhi.entity;

import java.util.List;

/**
 * Created by Administrator on 2017/11/7.
 */
public class Ls1 {
    public static class TResult{

        private String title;	/*唐朝开设律学，培养法律人才*/
        private String day;	/*2/29*/
        private String date;	/*632年02月29日*/
        private String e_id;	/*2719*/

        public void setTitle(String value){
            this.title = value;
        }
        public String getTitle(){
            return this.title;
        }

        public void setDay(String value){
            this.day = value;
        }
        public String getDay(){
            return this.day;
        }

        public void setDate(String value){
            this.date = value;
        }
        public String getDate(){
            return this.date;
        }

        public void setE_id(String value){
            this.e_id = value;
        }
        public String getE_id(){
            return this.e_id;
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
