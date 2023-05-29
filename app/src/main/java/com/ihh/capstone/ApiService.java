package com.ihh.capstone;

import com.ihh.capstone.OCR.OCRText;
import com.ihh.capstone.OTP.OTP;
import com.ihh.capstone.login.Join;
import com.ihh.capstone.login.Login;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {



    @POST("register")
    Call<Void> requestJoin(@Body Join join);

    // post 방식으로 데이터를 보낼 건데, 어디로 보낼지
    @POST("/auth/login")
    // 서버에 id, pw 값 보내기
    Call<Login> requestLogin(
            @Field("userid") String userid,
            @Field("userpw") String userpw
    );


    @POST("/auth/otp")
    Call<OTP> sendOTPKey(@Field("OTP") String text);

//    @GET("OTPCode")
//    Call<OTP> getOTPCode();

    @Multipart
    @POST("OCR 이미지를 받아 text를 보내줄 서버의 엔드포인트")
    Call<OCRText> uploadImage(@Part MultipartBody.Part image);


//        @GET("text")
//        Call<String> getText();

}
