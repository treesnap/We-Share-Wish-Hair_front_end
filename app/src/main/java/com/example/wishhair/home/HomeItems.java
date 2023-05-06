package com.example.wishhair.home;

public class HomeItems {
//    hot review
    private String username;
    private String context_review;

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
    private String hairImage;
    private String hairStyle;
    private String likes;
    private boolean isLike;

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

    public boolean getIsLike() {
        return isLike;
    }

    public void setIsLike(boolean like) {
        isLike = like;
    }
}
