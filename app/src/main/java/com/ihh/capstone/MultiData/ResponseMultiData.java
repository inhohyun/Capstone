package com.ihh.capstone.MultiData;

public class ResponseMultiData {
    private String BASE64Data;

    ResponseMultiData(String ResponseData){
        this.BASE64Data = ResponseData;
    }

    public String getBASE64Data(){return BASE64Data;}

    public void setBASE64Data(String ResponseData){this.BASE64Data = ResponseData;}

}
