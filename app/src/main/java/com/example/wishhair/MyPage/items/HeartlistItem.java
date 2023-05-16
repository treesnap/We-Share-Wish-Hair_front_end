package com.example.wishhair.MyPage.items;

import android.widget.ImageView;
import android.widget.TextView;

public class HeartlistItem {
    String HeartlistPicture;
    String HeartlistGrade, HeartlistHeartcount;
    String HeartlistStyleName, HeartlistReviewerNickname;
    String[] HashTags;
    int HeartlistReviewID;

//    public HeartlistItem(ImageView imageView, TextView grade, TextView count, TextView StyleName) {
//        HeartlistPicture = imageView;
//        HeartlistGrade = grade;
//        HeartlistHeartcount = count;
//        HeartlistStyleName = StyleName;
//    }
    public HeartlistItem() {}

    public String getHeartlistHeartcount() {
        return HeartlistHeartcount;
    }

    public void setHeartlistHeartcount(String heartlistHeartcount) {
        HeartlistHeartcount = heartlistHeartcount;
    }

    public String getHeartlistGrade() {
        return HeartlistGrade;
    }

    public void setHeartlistGrade(String heartlistGrade) {
        HeartlistGrade = heartlistGrade;
    }

    public void setHeartlistStyleName(String s) {
        HeartlistStyleName = s;
    }

    public String getHeartlistStyleName() {
        return HeartlistStyleName;
    }

    public String getHeartlistReviewerNickname() {
        return HeartlistReviewerNickname;
    }

    public void setHeartlistReviewerNickname(String heartlistReviewerNickname) {
        HeartlistReviewerNickname = heartlistReviewerNickname;
    }

    public int getHeartlistReviewID() {
        return HeartlistReviewID;
    }

    public void setHeartlistReviewID(int heartlistReviewID) {
        HeartlistReviewID = heartlistReviewID;
    }

    public String[] getHashTags() {
        return HashTags;
    }

    public void setHashTags(String[] hashTags) {
        HashTags = hashTags;
    }

    public String getHeartlistPicture() {
        return HeartlistPicture;
    }

    public void setHeartlistPicture(String heartlistPicture) {
        HeartlistPicture = heartlistPicture;
    }
}
