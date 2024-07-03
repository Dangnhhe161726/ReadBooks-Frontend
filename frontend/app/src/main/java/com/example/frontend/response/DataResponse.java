package com.example.frontend.response;

public class DataResponse {
    private String Token;
    private UserResponse user;

    public DataResponse(String token, UserResponse user) {
        Token = token;
        this.user = user;
    }

    public DataResponse() {
    }

    public String getToken() {
        return Token;
    }

    public void setToken(String token) {
        Token = token;
    }

    public UserResponse getUser() {
        return user;
    }

    public void setUser(UserResponse user) {
        this.user = user;
    }
}
