package com.example.barcodescanner;

public class User {
    private String name, email, mobileNum;
    private int userType=0;

    public User(){

    }
    public User(String name, String email, String mob){
        this.email = email;
        this.name = name;
        this.mobileNum = mob;
        this.userType = userType;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getMobileNum() {
        return mobileNum;
    }

    public void setMobileNum(String mobileNum) {
        this.mobileNum = mobileNum;
    }

    public int getUserType() {
        return userType;
    }

    public void setUserType(int userType) {
        this.userType = userType;
    }
}
