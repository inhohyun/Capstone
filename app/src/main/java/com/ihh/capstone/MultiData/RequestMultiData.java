package com.ihh.capstone.MultiData;

public class RequestMultiData {
    private String returnType;
    private String requestData;

    public RequestMultiData(String returnType, String requestData){
        this.returnType = returnType;
        this.requestData = requestData;

    }
    public String getReturnType(){
        return returnType;
    }
    public void setReturnType(String returnType){
        this.returnType = returnType;
    }
    public String getRequestData(){
        return requestData;
    }
    public void setRequestData(String requestData){
        this.requestData = requestData;
    }

}
