package com.example.frontend.response;

import com.example.frontend.models.Data;
import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.List;

public class BookResponse {
    private String timeStamp;
    private int statusCode;
    private String status;
    private String message;

    private Data data;

    public String getTimeStamp() {
        return timeStamp;
    }

    public void setTimeStamp(String timeStamp) {
        this.timeStamp = timeStamp;
    }

    public int getStatusCode() {
        return statusCode;
    }

    public void setStatusCode(int statusCode) {
        this.statusCode = statusCode;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public String getMessage() {
        return message;
    }

    public void setMessage(String message) {
        this.message = message;
    }

    public Data getData() {
        return data;
    }

    public void setData(Data data) {
        this.data = data;
    }
}