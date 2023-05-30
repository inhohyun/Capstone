package com.ihh.capstone.login;

public class ResponseLogin {
    private String isSuccess;
    private String userId;
    private String userName;
    private String userRank;
    private String userPhoneNumber;
    private String userOtpKey;

    //서버로부터 리턴받을 값들(로그인 성공 여부, 사용자 정보(아이디, 이름, 직무, 휴대폰 번호), otpKey(서버에 다시 보내서 otpCode를 받을 예정)
    public ResponseLogin(String isSuccess, String userId, String userName, String userRank, String userPhoneNumber, String userOtpKey) {

        this.isSuccess = isSuccess;
        this.userId = userId;
        this.userName = userName;
        this.userRank = userRank;
        this.userPhoneNumber = userPhoneNumber;
        this.userOtpKey = userOtpKey;
    }

    //getter & setter
    public String getIsSuccess() {
        return isSuccess;
    }

    public void setIsSuccess(String isSuccess) {
        this.isSuccess = isSuccess;
    }


    public String getUserId() {
        return userId;
    }

    public void setUserId(String userId) {
        this.userId = userId;
    }

    public String getUserName() {
        return userName;
    }

    public void setUserName(String userName) {
        this.userName = userName;
    }

    public String getUserRank() {
        return userRank;
    }

    public void setUserRank(String userRank) {
        this.userRank = userRank;
    }

    public String getUserPhoneNumber() {
        return userPhoneNumber;
    }

    public void setUserPhoneNumber(String userPhoneNumber) {
        this.userPhoneNumber = userPhoneNumber;
    }

    public String getUserOtpKey() {
        return userOtpKey;
    }

    public void setUserOtpKey(String userOtpKey) {
        this.userOtpKey = userOtpKey;
    }




}
