package com.example.frontend.response;

public class LoginResponse {
    public LoginResponse(String timeStamp, int statusCode, String status, String message, DataResponse data) {
        this.timeStamp = timeStamp;
        this.statusCode = statusCode;
        this.status = status;
        this.message = message;
        this.data = data;
    }

    public LoginResponse() {
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

    public DataResponse getData() {
        return data;
    }

    public void setData(DataResponse data) {
        this.data = data;
    }

    private String timeStamp;
    private int statusCode;
    private String status;
    private String message;
    private DataResponse data;
}
