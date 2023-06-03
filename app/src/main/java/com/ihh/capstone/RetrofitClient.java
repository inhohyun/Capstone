package com.ihh.capstone;

import com.ihh.capstone.ApiService;

import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

public class RetrofitClient {
    // https://caps.thonlog.com/
    private static final String BASE_URL = "http://172.30.1.14:8080";

    private static Retrofit retrofit;
    //싱글톤 활용해서 retrofit 객체 생성
    public static ApiService getApiService() {
        if (retrofit == null) {
            retrofit = new Retrofit.Builder()
                    .baseUrl(BASE_URL)
                    .addConverterFactory(GsonConverterFactory.create())
                    .build();
        }
        return retrofit.create(ApiService.class);
    }
}
