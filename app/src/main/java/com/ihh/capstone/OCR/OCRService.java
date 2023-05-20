package com.ihh.capstone.OCR;

import okhttp3.MultipartBody;
import retrofit2.Call;
import retrofit2.http.GET;
import retrofit2.http.Multipart;
import retrofit2.http.POST;
import retrofit2.http.Part;

public interface OCRService {

    @Multipart
    @POST("upload")
    Call<String> uploadImage(@Part MultipartBody.Part image);

    @GET("text")
    Call<String> getText();

}

