package com.ihh.capstone.login;

public class ResponseLogin {
    private String id;
    private String name;
    private String rank;
    private String phone;
    private String secret;

    //서버로부터 리턴받을 값들(로그인 성공 여부, 사용자 정보(아이디, 이름, 직무, 휴대폰 번호), otpKey(서버에 다시 보내서 otpCode를 받을 예정)
    public ResponseLogin(String id, String name, String rank, String phone, String secret) {

        this.id = id;
        this.name = name;
        this.rank = rank;
        this.phone = phone;
        this.secret = secret;
    }

    //getter & setter

    public String getId() {
        return id;
    }

    public void setId(String id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getRank() {
        return rank;
    }

    public void setRank(String rank) {
        this.rank = rank;
    }

    public String getPhone() {
        return phone;
    }

    public void setPhone(String phone) {
        this.phone = phone;
    }

    public String getSecret() {
        return secret;
    }

    public void setSecret(String secret) {
        this.secret = secret;
    }




}
