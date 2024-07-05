package com.example.frontend.models;

import com.google.gson.annotations.SerializedName;

import java.sql.Date;
import java.util.List;

public class Book {
    private Long id;
    private String name;
    private String link;
    private int view;
    private int favorites;
    private String thumbnail;
    @SerializedName("create_time")
    private Date createTime;
    @SerializedName("update_time")
    private Date updateTime;
    private String introduce;
    private boolean status;
    private Author author;
    private List<Category> categories;

    public Book() {
    }

    public Book(Long id, String name, String link, int view, int favorites, String thumbnail, Date createTime, Date updateTime, String introduce, boolean status, Author author, List<Category> categories) {
        this.id = id;
        this.name = name;
        this.link = link;
        this.view = view;
        this.favorites = favorites;
        this.thumbnail = thumbnail;
        this.createTime = createTime;
        this.updateTime = updateTime;
        this.introduce = introduce;
        this.status = status;
        this.author = author;
        this.categories = categories;
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public String getName() {
        return name;
    }

    public void setName(String name) {
        this.name = name;
    }

    public String getLink() {
        return link;
    }

    public void setLink(String link) {
        this.link = link;
    }

    public int getView() {
        return view;
    }

    public void setView(int view) {
        this.view = view;
    }

    public int getFavorites() {
        return favorites;
    }

    public void setFavorites(int favorites) {
        this.favorites = favorites;
    }

    public String getThumbnail() {
        return thumbnail;
    }

    public void setThumbnail(String thumbnail) {
        this.thumbnail = thumbnail;
    }

    public Date getCreateTime() {
        return createTime;
    }

    public void setCreateTime(Date createTime) {
        this.createTime = createTime;
    }

    public Date getUpdateTime() {
        return updateTime;
    }

    public void setUpdateTime(Date updateTime) {
        this.updateTime = updateTime;
    }

    public String getIntroduce() {
        return introduce;
    }

    public void setIntroduce(String introduce) {
        this.introduce = introduce;
    }

    public boolean isStatus() {
        return status;
    }

    public void setStatus(boolean status) {
        this.status = status;
    }

    public Author getAuthor() {
        return author;
    }

    public void setAuthor(Author author) {
        this.author = author;
    }

    public List<Category> getCategories() {
        return categories;
    }

    public void setCategories(List<Category> categories) {
        this.categories = categories;
    }
}
