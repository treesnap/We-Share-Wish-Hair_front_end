package com.example.wishhair.favorite;

import android.widget.ImageView;

public class FavoriteDetailRecyclerViewItem {
    ImageView styleReviewPicture;
    String styleReviewGrade, styleReviewHeartCount;
    String styleReviewNickname;
    int ReviewStyleID;

    public FavoriteDetailRecyclerViewItem() {}

    public ImageView getStyleReviewPicture() {
        return styleReviewPicture;
    }

    public void setStyleReviewPicture(ImageView styleReviewPicture) {
        this.styleReviewPicture = styleReviewPicture;
    }

    public String getStyleReviewGrade() {
        return styleReviewGrade;
    }

    public void setStyleReviewGrade(String styleReviewGrade) {
        this.styleReviewGrade = styleReviewGrade;
    }

    public String getStyleReviewHeartCount() {
        return styleReviewHeartCount;
    }

    public void setStyleReviewHeartCount(String styleReviewHeartCount) {
        this.styleReviewHeartCount = styleReviewHeartCount;
    }

    public String getStyleReviewNickname() {
        return styleReviewNickname;
    }

    public void setStyleReviewNickname(String styleReviewNickname) {
        this.styleReviewNickname = styleReviewNickname;
    }

    public int getReviewStyleID() {
        return ReviewStyleID;
    }

    public void setReviewStyleID(int reviewStyleID) {
        ReviewStyleID = reviewStyleID;
    }
}
