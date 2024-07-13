package com.example.frontend.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;

public class User {
    private long id;

    @SerializedName("full_name")

    private String fullName;

    private String email;
    @SerializedName("phone_number")

    private String phoneNumber;
    @SerializedName("date_of_birth")


    private Date dob;

    private String avatar;

    private String address;

    private boolean gender;

    private boolean status;

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

    public User(long id, String fullName, String email, String phoneNumber, Date dob, String avatar, String address, boolean gender, boolean status) {
        this.id = id;
        this.fullName = fullName;
        this.email = email;
        this.phoneNumber = phoneNumber;
        this.dob = dob;
        this.avatar = avatar;
        this.address = address;
        this.gender = gender;
        this.status = status;
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

    @Override
    public String toString() {
        return "User{" +
                "id=" + id +
                ", fullName='" + fullName + '\'' +
                ", email='" + email + '\'' +
                ", phoneNumber='" + phoneNumber + '\'' +
                ", dob=" + dob +
                ", avatar='" + avatar + '\'' +
                ", address='" + address + '\'' +
                ", gender=" + gender +
                ", status=" + status +
                '}';
    }
}
