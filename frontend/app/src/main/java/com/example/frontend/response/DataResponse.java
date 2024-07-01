package com.example.frontend.response;

public class DataResponse {
    private String Token;

    public DataResponse(String token) {
        Token = token;
    }

    public DataResponse() {
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }
}
