package com.example.frontend.models;

import java.util.ArrayList;
import java.util.List;

public class FeedBack {

    private Long id;


    private String content;


    private int numLike;

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public int getNumLike() {
        return numLike;
    }

    public void setNumLike(int numLike) {
        this.numLike = numLike;
    }

    public User getUserEntity() {
        return userEntity;
    }

    public FeedBack(Long id, String content, int numLike, User userEntity, Book book) {
        this.id = id;
        this.content = content;
        this.numLike = numLike;
        this.userEntity = userEntity;
    }

    public void setUserEntity(User userEntity) {
        this.userEntity = userEntity;
    }


    private User userEntity;


    @Override
    public String toString() {
        return "FeedBack{" +
                "id=" + id +
                ", content='" + content + '\'' +
                ", numLike=" + numLike +
                ", userEntity=" + userEntity +
                ", book="  +
                '}';
    }
//    private FeedBack feedback;
//    private List<Feedback> feedbacks = new ArrayList<>();
}
