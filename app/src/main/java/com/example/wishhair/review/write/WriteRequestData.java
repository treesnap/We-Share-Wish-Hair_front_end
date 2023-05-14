package com.example.wishhair.review.write;

public class WriteRequestData {
    int reviewId;
    int hairStyleId;
    String rating;
    String content;

    public WriteRequestData() {}

    public int getReviewId() {
        return reviewId;
    }

    public void setReviewId(int reviewId) {
        this.reviewId = reviewId;
    }

    public int getHairStyleId() {
        return hairStyleId;
    }

    public void setHairStyleId(int hairStyleId) {
        this.hairStyleId = hairStyleId;
    }

    public String getRating() {
        return rating;
    }

    public void setRating(Float rating) {
        this.rating = String.valueOf(rating);
    }

    public String getContent() {
        return content;
    }

    public void setContent(String content) {
        this.content = content;
    }
}
