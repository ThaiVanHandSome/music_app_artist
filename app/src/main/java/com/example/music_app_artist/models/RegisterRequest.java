package com.example.music_app_artist.models;

public class RegisterRequest {

    private String firstName;
    private String lastName;
    private String nickName;
    private String phoneNumber;
    private String email;
    private String password;
    private int gender;

    public RegisterRequest() {
        firstName = "";
        lastName = "";
        nickName = "";
        phoneNumber = "";
        email = "";
        password = "";
        gender = 1;
    }

    public String getNickName() {
        return nickName;
    }

    public void setNickName(String nickName) {
        this.nickName = nickName;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPassword() {
        return password;
    }

    public void setPassword(String password) {
        this.password = password;
    }

    public int getGender() {
        return gender;
    }

    public void setGender(int gender) {
        this.gender = gender;
    }
}
