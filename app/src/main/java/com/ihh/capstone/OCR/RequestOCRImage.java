package com.ihh.capstone.OCR;

import okhttp3.MultipartBody;

public class RequestOCRImage {
    private String BASE64Image;

    public RequestOCRImage(String BASE64Image) {
        this.BASE64Image = BASE64Image;
    }


    public String getBASE64Image() {
        return BASE64Image;
    }

    public void setBASE64Image(String BASE64Image) {
        this.BASE64Image = BASE64Image;
    }
}
