package com.ihh.capstone.OTP;

public class ResponseOTPCode {
    private String OTPCode;

    public ResponseOTPCode(String otp){this.OTPCode = otp;}

    public String getOTPCode(){return OTPCode;}

    public void setOTPCode(String otpCode){this.OTPCode = otpCode;}

}
