package com.ihh.capstone.login;

import retrofit2.Call;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;




public interface LoginService {
    // 실제 서버 주소는 retrofit 객체를 만들 때 사용
    // FormUrlEncoded: 서버에서 정확한 값을 얻어오기 위한 encode
    @FormUrlEncoded
    // post 방식으로 데이터를 보낼 건데, 어디로 보낼지
    @POST("서버의 엔드포인트 URL")
    // 서버에 id, pw 값 보내기
    Call<Login> requestLogin(
            @Field("userid") String userid,
            @Field("userpw") String userpw
    );
}
