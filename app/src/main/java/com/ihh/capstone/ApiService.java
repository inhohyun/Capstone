package com.ihh.capstone;


import com.ihh.capstone.MultiData.RequestMultiData;
import com.ihh.capstone.MultiData.ResponseMultiData;
import com.ihh.capstone.OCR.RequestOCRImage;
import com.ihh.capstone.OCR.ResponseOCRText;
import com.ihh.capstone.OTP.RequestOTPCode;
import com.ihh.capstone.OTP.ResponseOTPCode;
import com.ihh.capstone.login.Join;
import com.ihh.capstone.login.RequestFirstLogin;
import com.ihh.capstone.login.ResponseLogin;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {


    @POST("/auth/register")
    Call<Void> requestJoin(@Body Join join);

    // post 방식으로 데이터를 보낼 건데, 어디로 보낼지
    @POST("/auth/login")
    // 서버에 id, pw 값 보내기
    Call<ResponseLogin> requestLogin(@Body RequestFirstLogin login);


    @POST("/auth/otp")
    Call<ResponseOTPCode> sendOTPKey(@Body RequestOTPCode secret);

    @Multipart
    @POST("/image")
    Call<ResponseOCRText> uploadImage(@Body RequestOCRImage BASE64image);


    @POST("정해지지 않음")
    Call<ResponseMultiData> requestMultiData(@Body RequestMultiData requestData);
}
