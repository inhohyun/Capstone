package com.ihh.capstone.OTP;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.POST;

public interface OTPService {
    @FormUrlEncoded
    @POST("OTPKey")
    Call<String> sendOTPKey(@Field("text") String text);

    @GET("OTPCode")
    Call<String> getOTPCode();
}
