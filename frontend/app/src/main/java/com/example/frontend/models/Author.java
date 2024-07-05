package com.example.frontend.models;

<<<<<<<< HEAD:frontend/app/src/main/java/com/example/frontend/response/CategoryResponse.java
import com.example.frontend.models.Category;
import com.example.frontend.models.Data;

import java.util.List;

public class CategoryResponse {
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
========
public class Author {
    private Long id;
    private String name;

    public Author() {
    }

    public Author(Long id, String name) {
        this.id = id;
        this.name = name;
>>>>>>>> HongDang:frontend/app/src/main/java/com/example/frontend/models/Author.java
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
