package com.ihh.capstone.OCR;

public class ResponseOCRText {
    //응답 받을 값 다시 정하기 -> 그래프 그릴때 다시 조사
    private String OCRText;

    public ResponseOCRText(String ocrText){
        this.OCRText = ocrText;
    }

    public String getOCRText(){
        return OCRText;
    }
    public void setOCRText(String ocrText){
        this.OCRText = ocrText;
    }
}
