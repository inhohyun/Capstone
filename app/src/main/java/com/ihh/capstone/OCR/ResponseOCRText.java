package com.ihh.capstone.OCR;

import java.util.List;

public class ResponseOCRText {
    //응답 받을 값 다시 정하기 -> 그래프 그릴때 다시 조사
    private List<String> itemName;
    private List<Float> itemPrice;

    public ResponseOCRText(List<String> list1, List<Float> list2) {
        this.itemName = list1;
        this.itemPrice = list2;
    }

    public List<String> getItemName() {
        return itemName;
    }

    public void setItemName(List<String> itemName) {
        this.itemName = itemName;
    }

    public List<Float> getItemPrice() {
        return itemPrice;
    }

    public void setItemPrice(List<Float> itemPrice) {
        this.itemPrice = itemPrice;
    }


}
