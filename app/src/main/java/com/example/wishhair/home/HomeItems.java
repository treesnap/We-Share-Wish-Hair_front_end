package com.example.wishhair.home;

import java.util.ArrayList;

public class HomeItems {
//    monthly review
    private int reviewId;
    private String username;
    private String context_review;

    public HomeItems(int reviewId, String username, String context_review) {
        this.reviewId = reviewId;
        this.username = username;
        this.context_review = context_review;
    }

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public String getUsername() {
        return username;
    }

    public void setUsername(String username) {
        this.username = username;
    }

    public String getContext_review() {
        return context_review;
    }

    public void setContext_review(String context_review) {
        this.context_review = context_review;
    }

//    recommend
    private int hairStyleId;
    private ArrayList<String> hairImages;
    private String hairStyleName;
    private ArrayList<String> tags;

    private String likes;

    public HomeItems(int hairStyleId, ArrayList<String> hairImages, String hairStyleName, ArrayList<String> tags) {
        this.hairStyleId = hairStyleId;
        this.hairImages = hairImages;
        this.hairStyleName = hairStyleName;
        this.tags = tags;
    }

    public int getHairStyleId() {
        return hairStyleId;
    }

    public void setHairStyleId(int hairStyleId) {
        this.hairStyleId = hairStyleId;
    }

    public ArrayList<String> getHairImages() {
        return hairImages;
    }

    public void setHairImages(ArrayList<String> hairImages) {
        this.hairImages = hairImages;
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
}
