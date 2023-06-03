package com.ihh.capstone.OCR;

public class ResponseOCRText {
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
