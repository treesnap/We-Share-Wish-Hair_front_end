package com.example.wishhair.favorite;

import android.widget.ImageView;
import android.widget.TextView;

public class FavoriteItem {
    String FavoritePicture;
    String FavoriteGrade, FavoriteHeartcount, FavoriteStyleName;
    int FavoriteStyleId;
    String[] FavoriteHashtags;

//    public FavoriteItem(ImageView imageView, TextView grade, TextView count) {
//        FavoritePicture = imageView;
//        FavoriteGrade = grade;
//        FavoriteHeartcount = count;
//    }
    public FavoriteItem() {}

    public String getFavoritePicture() {
        return FavoritePicture;
    }

    public void setFavoritePicture(String FavoritePicture) {
        FavoritePicture = FavoritePicture;
    }

    public String getFavoriteStyleName() {
        return FavoriteStyleName;
    }

    public void setFavoriteStyleName(String favoriteStyleName) {
        FavoriteStyleName = favoriteStyleName;
    }

    public String getFavoriteHeartcount() {
        return FavoriteHeartcount;
    }

    public void setFavoriteHeartcount(String FavoriteHeartcount) {
        FavoriteHeartcount = FavoriteHeartcount;
    }

    public String getFavoriteGrade() {
        return FavoriteGrade;
    }

    public void setFavoriteGrade(String FavoriteGrade) {
        FavoriteGrade = FavoriteGrade;
    }

    public int getFavoriteStyleId() {
        return FavoriteStyleId;
    }

    public void setFavoriteStyleId(int favoriteStyleId) {
        FavoriteStyleId = favoriteStyleId;
    }

    public String[] getFavoriteHashtags() {
        return FavoriteHashtags;
    }

    public void setFavoriteHashtags(String[] favoriteHashtags) {
        FavoriteHashtags = favoriteHashtags;
    }
}
