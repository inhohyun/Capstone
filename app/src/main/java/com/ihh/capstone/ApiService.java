package com.ihh.capstone;

import com.ihh.capstone.OCR.OCRText;
import com.ihh.capstone.OTP.OTP;
import com.ihh.capstone.login.Login;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface ApiService {

    @FormUrlEncoded
    // post 방식으로 데이터를 보낼 주소
    @POST("사용자 정보를 저장할 서버의 엔드포인트")
        //보내기만 할것 이므로 void
    Call<Void> requestJoin(
            @Field("id") String textId,
            @Field("pw") String textPw1,
            @Field("name") String textName,
            @Field("rank") String textRank,
            @Field("phone") String textPhoneNumber
    );

    @FormUrlEncoded
    // post 방식으로 데이터를 보낼 건데, 어디로 보낼지
    @POST("로그인 정보를 확인할 서버의 엔드포인트")
        // 서버에 id, pw 값 보내기
    Call<Login> requestLogin(
            @Field("userid") String userid,
            @Field("userpw") String userpw
    );

    @FormUrlEncoded
    @POST("otpcode를 보내줄 서버의 엔드포인트")
    Call<OTP> sendOTPKey(@Field("OTP") String text);

//    @GET("OTPCode")
//    Call<OTP> getOTPCode();

    @Multipart
    @POST("OCR 이미지를 받아 text를 보내줄 서버의 엔드포인트")
    Call<OCRText> uploadImage(@Part MultipartBody.Part image);

//        @GET("text")
//        Call<String> getText();

}
