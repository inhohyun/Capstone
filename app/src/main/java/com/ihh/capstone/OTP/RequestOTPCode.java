package com.ihh.capstone.OTP;

public class RequestOTPCode {
    private String otpKey;

    public RequestOTPCode(String otpKey){
        this.otpKey = otpKey;
    }

    public String getOtpKey(){return otpKey;}
    public void setOtpKey(String otpKey){this.otpKey = otpKey;}
}

