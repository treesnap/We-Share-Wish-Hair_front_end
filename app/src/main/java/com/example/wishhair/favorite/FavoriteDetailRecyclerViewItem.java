package com.example.wishhair.favorite;

import android.widget.ImageView;

import java.util.ArrayList;

public class FavoriteDetailRecyclerViewItem {
    private String styleReviewPicture;
    private String styleReviewGrade, styleReviewHeartCount;
    private String styleReviewNickname;
    private int ReviewStyleID, itemReviewID;

    public FavoriteDetailRecyclerViewItem() {}

    public String getStyleReviewPicture() {
        return styleReviewPicture;
    }

    public void setStyleReviewPicture(String styleReviewPicture) {
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

    public int getItemReviewID() {
        return itemReviewID;
    }

    public void setItemReviewID(int itemReviewID) {
        this.itemReviewID = itemReviewID;
    }
}
