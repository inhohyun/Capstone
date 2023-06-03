package com.ihh.capstone.OCR;

import okhttp3.MultipartBody;

public class RequestOCRImage {
    private MultipartBody.Part imageUri;

    public RequestOCRImage(MultipartBody.Part imageUri) {
        this.imageUri = imageUri;
    }


    public MultipartBody.Part getImageUri() {
        return imageUri;
    }

    public void setImageUri(MultipartBody.Part imageUri) {
        this.imageUri = imageUri;
    }
}
