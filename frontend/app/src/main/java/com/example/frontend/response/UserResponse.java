package com.example.frontend.response;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class UserResponse {
    public long getId() {
        return id;
    }

    public void setId(long id) {
        this.id = id;
    }

    public String getFullName() {
        return fullName;
    }

    public void setFullName(String fullName) {
        this.fullName = fullName;
    }

    public String getEmail() {
        return email;
    }

    public void setEmail(String email) {
        this.email = email;
    }

    public String getPhoneNumber() {
        return phoneNumber;
    }

    public void setPhoneNumber(String phoneNumber) {
        this.phoneNumber = phoneNumber;
    }

    public Date getDob() {
        return dob;
    }

    public void setDob(Date dob) {
        this.dob = dob;
    }

    public String getAvatar() {
        return avatar;
    }

    public void setAvatar(String avatar) {
        this.avatar = avatar;
    }

    public String getAddress() {
        return address;
    }

    public void setAddress(String address) {
        this.address = address;
    }

    public boolean isGender() {
        return gender;
    }

    public void setGender(boolean gender) {
        this.gender = gender;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    @SerializedName("id")
    private long id;

    @SerializedName("full_name")
    private String fullName;

    @SerializedName("email")
    private String email;

    @SerializedName("phone_number")
    private String phoneNumber;

    @SerializedName("date_of_birth")
    private Date dob;

    @SerializedName("avatar")
    private String avatar;

    @SerializedName("address")
    private String address;

    @SerializedName("gender")
    private boolean gender;

    @SerializedName("status")
    private boolean status;
}
