package com.ihh.capstone.login;

public class Join {
    private String id;
    private String pw;
    private String name;
    private String rank;
    private String phone;

    public Join(String id, String pw, String name, String rank, String phone){
        this.id = id;
        this.pw = pw;
        this.name = name;
        this.rank = rank;
        this.phone = phone;
    }

    public String getId(){
        return id;
    }
    public void setId(String id){
        this.id = id;
    }
    public String getPw(){
        return pw;
    }
    public void setPw(String pw){
        this.pw = pw;
    }
    public String getName(){
        return name;
    }
    public void setName(String name){
        this.name = name;
    }
    public String getRank(){
        return rank;
    }
    public void setRank(String rank){
        this.rank = rank;
    }
    public String getPhone(){
        return phone;
    }
    public void setPhone(String phone){
        this.phone = phone;
    }
}
