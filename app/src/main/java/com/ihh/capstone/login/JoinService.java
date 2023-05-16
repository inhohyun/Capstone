package com.ihh.capstone.login;

import retrofit2.Call;
import retrofit2.http.Body;
import retrofit2.http.Field;
import retrofit2.http.FormUrlEncoded;
import retrofit2.http.POST;

public interface JoinService {
    @FormUrlEncoded
    // post 방식으로 데이터를 보낼 건데, 어디로 보낼지
    @POST("서버의 엔드포인트 URL")
        //보내기만 할것 이므로 void
    Call<Void> requestJoin(
            @Field("id") String textId,
            @Field("pw") String textPw1,
            @Field("name") String textName,
            @Field("rank") String textRank,
            @Field("phone") String textPhoneNumber
    );
}
