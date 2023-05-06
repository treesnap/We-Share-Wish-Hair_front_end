package com.example.wishhair.home;

public class HomeItems {
//    hot review
    String username;
    String context_review;

    public HomeItems(String username, String context_review) {
        this.username = username;
        this.context_review = context_review;
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

    //    TODO: set recommend item
//    recommend
    String hairImage;
    String hairStyle;
    String likes;
    Boolean isLike;

    public HomeItems(String hairImage, String hairStyle, String likes, boolean isLike) {
        this.hairImage = hairImage;
        this.hairStyle = hairStyle;
        this.likes = likes;
        this.isLike = isLike;
    }

    public String getHairImage() {
        return hairImage;
    }

    public void setHairImage(String hairImage) {
        this.hairImage = hairImage;
    }

    public String getHairStyle() {
        return hairStyle;
    }

    public void setHairStyle(String hairStyle) {
        this.hairStyle = hairStyle;
    }

    public String getLikes() {
        return likes;
    }

    public void setLikes(String likes) {
        this.likes = likes;
    }

    public Boolean getLike() {
        return isLike;
    }

    public void setLike(Boolean like) {
        isLike = like;
    }
}
