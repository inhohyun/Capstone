package com.ihh.capstone.OTP;

public class RequestOTPCode {
    private String secret;

    public RequestOTPCode(String secret){
        this.secret = secret;
    }

    public String getOtpKey(){return secret;}
    public void setOtpKey(String secret){this.secret = secret;}
}

