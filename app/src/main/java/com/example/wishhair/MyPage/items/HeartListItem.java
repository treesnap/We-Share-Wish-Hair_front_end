package com.example.wishhair.MyPage.items;

public class HeartListItem {

    private String HeartListPicture, HeartListGrade, HeartListHeartCount, HeartListStyleName, HeartListReviewerNickname;
    private String[] HashTags;
    private int HeartListReviewID;

    public HeartListItem() {}

    public String getHeartListHeartCount() {
        return HeartListHeartCount;
    }

    public void setHeartListHeartCount(String heartListHeartCount) {

        HeartListHeartCount = heartListHeartCount;
    }

    public String getHeartListGrade() {
        return HeartListGrade;
    }

    public void setHeartListGrade(String heartListGrade) {
        HeartListGrade = heartListGrade;
    }

    public void setHeartListStyleName(String s) {
        HeartListStyleName = s;
    }

    public String getHeartListStyleName() {
        return HeartListStyleName;
    }

    public String getHeartListReviewerNickname() {
        return HeartListReviewerNickname;
    }

    public void setHeartListReviewerNickname(String heartListReviewerNickname) {
        HeartListReviewerNickname = heartListReviewerNickname;
    }

    public int getHeartListReviewID() {
        return HeartListReviewID;
    }

    public void setHeartListReviewID(int heartListReviewID) {
        HeartListReviewID = heartListReviewID;
    }

    public String[] getHashTags() {
        return HashTags;
    }

    public void setHashTags(String[] hashTags) {
        HashTags = hashTags;
    }

    public String getHeartListPicture() {
        return HeartListPicture;
    }

    public void setHeartListPicture(String heartListPicture) {
        HeartListPicture = heartListPicture;
    }
}
