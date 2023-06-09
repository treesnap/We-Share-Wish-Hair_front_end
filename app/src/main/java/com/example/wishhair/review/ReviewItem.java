package com.example.wishhair.review;

import java.io.IOException;
import java.io.Serializable;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.util.ArrayList;
import java.util.Date;
import java.util.Locale;

public class ReviewItem implements Serializable {

    // common
    private int reviewId;
    private String hairStyleName;
    private ArrayList<String> tags;
    private int likes;
    private String score;
    private String createdDate;
    private String content;

    // recent
    private ArrayList<String> imageUrls;
    private String userNickName;
    private boolean isHeart;

    private boolean isWriter;

    public ReviewItem() {}

    public ReviewItem(int reviewId, ArrayList<String> imageUrls, String hairStyleName, String userNickName, ArrayList<String> tags, String contents, String score, int likes, String createdDate, boolean isWriter) {
        this.reviewId = reviewId;
        this.imageUrls = imageUrls;
        this.hairStyleName = hairStyleName;
        this.userNickName = userNickName;
        this.tags = tags;
        this.score = score;
        this.likes = likes;
        this.createdDate = createdDate;
        this.content = contents;
        this.isWriter = isWriter;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getUserNickName() {
        return userNickName;
    }

    public void setUserNickName(String userNickName) {
        this.userNickName = userNickName;
    }

    public String getScore() {
        return score;
    }

    public void setScore(String score) {
        this.score = score;
    }

    public ArrayList<String> getImageUrls() {
        return imageUrls;
    }

    public void setImageUrls(ArrayList<String> imageUrls) {
        this.imageUrls = imageUrls;
    }

    public boolean getIsHeart() {
        return isHeart;
    }

    public void setHeart(boolean isHeart) {
        this.isHeart = isHeart;
    }

    public int getLikes() {
        return likes;
    }

    public void setLikes(int likes) {
        this.likes = likes;
    }

    public String getCreatedDate() {
        return createdDate;
    }

    public void setCreatedDate(String createdDate) {
        this.createdDate = createdDate;
    }

    public String getHairStyleName() {
        return hairStyleName;
    }

    public void setHairStyleName(String hairStyleName) {
        this.hairStyleName = hairStyleName;
    }

    public ArrayList<String> getTags() {
        return tags;
    }

    public void setTags(ArrayList<String> tags) {
        this.tags = tags;
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }

    public boolean isWriter() {
        return isWriter;
    }

    public void setIsWriter(boolean writer) {
        this.isWriter = writer;
    }

    private void writeObject(java.io.ObjectOutputStream out) throws IOException {
        out.writeObject(reviewId);
        out.writeObject(hairStyleName);
        out.writeObject(tags);
        out.writeObject(score);
        out.writeObject(likes);
        out.writeObject(createdDate);
        out.writeObject(content);
        out.writeObject(imageUrls);
        out.writeObject(isWriter);
    }

    private void readObject(java.io.ObjectInputStream in) throws IOException, ClassNotFoundException {
        reviewId = (int) in.readObject();
        hairStyleName = (String) in.readObject();
        tags = (ArrayList<String>) in.readObject();
        score = (String) in.readObject();
        likes = (int) in.readObject();
        createdDate = (String) in.readObject();
        content = (String) in.readObject();
        imageUrls = (ArrayList<String>) in.readObject();
        isWriter = (boolean) in.readObject();
    }
}
