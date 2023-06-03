package com.ihh.capstone.OTP;

public class ResponseOTPCode {
    private String token;

    public ResponseOTPCode(String token){this.token = token;}

    public String getOTPCode(){return token;}

    public void setOTPCode(String token){this.token = token;}

}

