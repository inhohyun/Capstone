package com.ihh.capstone.MultiData;

public class RequestMultiData {

    private String BASE64Data;

    public RequestMultiData(String requestData){

        this.BASE64Data = requestData;

    }
    public String getBASE64Data(){
        return BASE64Data;
    }
    public void setBASE64Data(String BASE64Data){
        this.BASE64Data = BASE64Data;
    }

}
