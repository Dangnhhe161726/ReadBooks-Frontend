package com.example.frontend.responses;

import com.example.frontend.models.Data;

public class CategoryResponse {
    private String timeStamp;
    private int statusCode;
    private String status;
    private String message;
    private Data data;

    public CategoryResponse() {
    }

    public CategoryResponse(String timeStamp, int statusCode, String status, String message, Data data) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }

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
