package com.example.wishhair;

import android.annotation.SuppressLint;
import android.content.Context;
import android.os.Bundle;
import android.util.Log;
import android.view.MenuItem;
import android.widget.Toast;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.fragment.app.Fragment;
import androidx.fragment.app.FragmentManager;
import androidx.fragment.app.FragmentTransaction;


import com.example.wishhair.MyPage.ConfigFragment;
import com.example.wishhair.MyPage.ConfigPasswordFragment;
import com.example.wishhair.MyPage.MyPageFragment;
import com.example.wishhair.MyPage.MyPointList;

import com.example.wishhair.MyPage.RefundFragment;
import com.example.wishhair.favorite.FavoriteDetail;
import com.example.wishhair.favorite.FavoriteFragment;
import com.example.wishhair.home.HomeFragment;
import com.example.wishhair.review.ReviewFragment;
import com.example.wishhair.sign.UrlConst;
import com.google.android.material.bottomnavigation.BottomNavigationView;
import com.google.android.material.navigation.NavigationBarView;

public class MainActivity extends AppCompatActivity {

    private final FragmentManager fragmentManager = getSupportFragmentManager();
    private static FragmentTransaction fragmentTransaction;
    private HomeFragment homeFragment;
    private final ReviewFragment reviewFragment = new ReviewFragment();
    private final MyPageFragment myPageFragment = new MyPageFragment();
    private final ConfigFragment configFragment = new ConfigFragment();
    private final MyPointList myPointList = new MyPointList();
    private final FavoriteFragment favoriteFragment = new FavoriteFragment();
    private final RefundFragment refundFragment = new RefundFragment();
    private final FavoriteDetail favoriteDetail = new FavoriteDetail();
    private final ConfigPasswordFragment configPasswordFragment = new ConfigPasswordFragment();
    public static Context context;

    private boolean hasFaceShape;
    private String userNickName, faceShapeTag;

    final static private String url = UrlConst.URL + "/api/my_page";

    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        context = getApplicationContext();
        setContentView(R.layout.activity_main);

        userNickName = getIntent().getStringExtra("nickname");
        hasFaceShape = getIntent().getBooleanExtra("hasFaceShape", false);
        faceShapeTag = getIntent().getStringExtra("faceShapeTag");

        homeFragment = HomeFragment.newInstance(userNickName, hasFaceShape, faceShapeTag);
        fragmentTransaction = fragmentManager.beginTransaction();
        fragmentTransaction.replace(R.id.MainLayout, homeFragment).commitAllowingStateLoss();
        getSupportFragmentManager().executePendingTransactions();

        BottomNavigationView BottomNavigation = findViewById(R.id.BottomNavigation);
        BottomNavigation.setOnItemSelectedListener(new BottomNavigationItemSelectedListener());
        

    }

    class BottomNavigationItemSelectedListener implements NavigationBarView.OnItemSelectedListener{

        @SuppressLint("NonConstantResourceId")
        @Override
        public boolean onNavigationItemSelected(@NonNull MenuItem item) {
            FragmentTransaction transaction = getSupportFragmentManager().beginTransaction();
            backPressFlag = false;
            switch(item.getItemId())
            {
                case R.id.bot_nav_home:
                    transaction.replace(R.id.MainLayout, homeFragment).commitAllowingStateLoss();
                    break;
                case R.id.bot_nav_favorite:
                    transaction.replace(R.id.MainLayout, favoriteFragment).commitAllowingStateLoss();
                    break;
                case R.id.bot_nav_review:
                    transaction.replace(R.id.MainLayout, reviewFragment).commitAllowingStateLoss();
                    break;
                case R.id.bot_nav_myPage:
                    transaction.replace(R.id.MainLayout, myPageFragment).commitAllowingStateLoss();
                    break;
            }
            return true;
        }
    }

    public void ChangeFragment(int index) {
        FragmentTransaction transaction = fragmentManager.beginTransaction();
        switch(index)
        {
            case 1:
                transaction.replace(R.id.MainLayout, homeFragment).commitAllowingStateLoss();
                break;
            case 2:
                transaction.replace(R.id.MainLayout, myPageFragment).commitAllowingStateLoss();
                break;
            case 3:
                break;
            case 4:
                transaction.replace(R.id.MainLayout, favoriteDetail).commitAllowingStateLoss();
                break;
            case 5:
                transaction.replace(R.id.MainLayout, favoriteFragment).commitAllowingStateLoss();
                break;
            case 6:
                transaction.replace(R.id.MainLayout, configPasswordFragment).commitAllowingStateLoss();
                break;
            case 7:
                transaction.replace(R.id.MainLayout, myPointList).commitAllowingStateLoss();
                break;
            case 8:
                transaction.replace(R.id.MainLayout, configFragment).commitAllowingStateLoss();
                break;
            case 9:
                transaction.replace(R.id.MainLayout, refundFragment).commitAllowingStateLoss();
                break;
        }

    }

    // 특정 프래그먼트에서 뒤로 두번 누를 시 앱 종료 및 프래그먼트 전환
    private long backPressedTime = 0;
    private boolean backPressFlag = false;
    public void  onBackPressed() {
        FragmentManager fm = getSupportFragmentManager();
        Fragment currentFragment = fm.findFragmentById(R.id.MainLayout);


        // 현재 프래그먼트가 navigation bar 4개 프래그먼트일 때 2회 누를 시 종료
        if (currentFragment instanceof HomeFragment || currentFragment instanceof  MyPageFragment ||
            currentFragment instanceof FavoriteFragment || currentFragment instanceof ReviewFragment) {
            if (System.currentTimeMillis() > backPressedTime + 1000) {
                backPressedTime = System.currentTimeMillis();
                Toast.makeText(this, "한번 더 누르시면 종료합니다.", Toast.LENGTH_SHORT).show();
            } else if (System.currentTimeMillis() <= backPressedTime + 2000) {
                finish();
            }
        }
        // 그 외 프래그먼트 일 때 뒤로 가기
        else {
            switch (currentFragment.getClass().getSimpleName()) {
                case "ConfigFragment":
                    ChangeFragment(2);
                    break;
                case "ConfigPasswordFragment":
                    ChangeFragment(8);
                    break;
                case "MyPointList":
                    ChangeFragment(2);
                    break;
                case "RefundFragment":
                    ChangeFragment(7);
                    break;
                case "FavoriteDetail":
                    // HomeItem -> Detail -> Home
                    if (backPressFlag) {
                        ChangeFragment(1);
                        backPressFlag = false;
                    } else {
                        ChangeFragment(5);
                    }
                    break;
                default:
                    break;
            }
        }

    }
    public void setBackPressFlag(boolean f) {
        backPressFlag = f;
    }
}