package com.ihh.capstone;

import androidx.lifecycle.LiveData;
import androidx.lifecycle.MutableLiveData;

//사용자의 데이터를 viewModel로 따로 관리 : fragment 생명주기상 데이터 초기화 방지
public class ViewModel extends androidx.lifecycle.ViewModel {
    private MutableLiveData<String> userId;
    private MutableLiveData<String> userName;
    private MutableLiveData<String> userRank;
    private MutableLiveData<String> userPhoneNumber;

    private MutableLiveData<String> userOtpKey;

    public ViewModel() {
        userId = new MutableLiveData<>("default");
        userName = new MutableLiveData<>("default");
        userRank = new MutableLiveData<>("default");
        userPhoneNumber = new MutableLiveData<>("default");
        userOtpKey = new MutableLiveData<>("default");

    }

    //view model에 사용자 정보 저장
    public void setUserInfo(String id, String name, String position, String phoneNumber, String otpKey) {
        userId.setValue(id);
        userName.setValue(name);
        userRank.setValue(position);
        userPhoneNumber.setValue(phoneNumber);
        userOtpKey.setValue(otpKey);

    }

    //get : 저장된 정보를 꺼내쓰는 메소드
    public LiveData<String> getUserId() {
        return userId;
    }

    public LiveData<String> getUserName() {
        return userName;
    }

    public LiveData<String> getUserRank() {
        return userRank;
    }

    public LiveData<String> getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public LiveData<String> getUserOtpKey() {
        return userOtpKey;
    }

}

