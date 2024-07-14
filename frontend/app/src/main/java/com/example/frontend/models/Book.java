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
    private List<FeedBack> feedbacks;
    private boolean status;
    private Author author;
    private List<Category> categories;
    private List<BookMark> bookMarks;
    public List<FeedBack> getFeedbacks() {
        return feedbacks;
    }

    public void setFeedbacks(List<FeedBack> feedbacks) {
        this.feedbacks = feedbacks;
    }

    public Book() {
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

    public List<BookMark> getBookMarks() {
        return bookMarks;
    }

    public void setBookMarks(List<BookMark> bookMarks) {
        this.bookMarks = bookMarks;
    }

    @Override
    public String toString() {
        return "Book{" +
                "id=" + id +
                ", name='" + name + '\'' +
                ", link='" + link + '\'' +
                ", view=" + view +
                ", favorites=" + favorites +
                ", thumbnail='" + thumbnail + '\'' +
                ", createTime=" + createTime +
                ", updateTime=" + updateTime +
                ", introduce='" + introduce + '\'' +
                ", feedbacks=" + feedbacks +
                ", status=" + status +
                ", author=" + author +
                ", categories=" + categories +
                '}';
    }
}
