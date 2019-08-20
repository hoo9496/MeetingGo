package com.example.administrator.meetingapp;

import android.content.Intent;

import android.graphics.drawable.ColorDrawable;
import android.support.annotation.NonNull;
import android.support.design.widget.NavigationView;
import android.support.v4.widget.DrawerLayout;
import android.support.v7.app.ActionBarDrawerToggle;
import android.support.v7.app.AppCompatActivity;
import android.os.Bundle;

import android.view.MenuItem;
import android.view.View;
import android.widget.ImageView;

// 로그인 성공 시 나오는 메인화면
public class MainActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private ImageView img_introduction, img_fillter, img_hartList, img_myPage, img_logout;
    Session session;

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_main);

        session = new Session(this);
        session.checkLogin();

        img_introduction = (ImageView) findViewById(R.id.img_introduction);
        img_fillter = (ImageView) findViewById(R.id.img_filltering);
        img_hartList = (ImageView) findViewById(R.id.img_hartList);
        img_myPage = (ImageView) findViewById(R.id.img_mypage);
        img_logout = (ImageView) findViewById(R.id.img_logout);

        // 액션바 설정하기
        getSupportActionBar().setTitle("MeetingGo"); // 액션바 타이틀 지정
        getSupportActionBar().setBackgroundDrawable(new ColorDrawable(0xFF999999)); // 액션바 배경색 변경

        // sideBar 만들기
        mDrawerlayout = (DrawerLayout) findViewById(R.id.drawer);
        mToggle = new ActionBarDrawerToggle(this, mDrawerlayout, R.string.open, R.string.close);
        mDrawerlayout.addDrawerListener(mToggle);
        mToggle.syncState();
        getSupportActionBar().setDisplayHomeAsUpEnabled(true);
        NavigationView navigationView = (NavigationView)findViewById(R.id.navigation_view);
        navigationView.setNavigationItemSelectedListener(this);

        // 소개받기 버튼
        img_introduction.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, IntroductionActivity.class));
                finish();
            }
        });

        // 필터링 소개 버튼
        img_fillter.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, FillterChoiceActivity.class));
                finish();
            }
        });

        // 하트리스트 버튼
        img_hartList.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, HartListActivity.class));
                finish();
            }
        });

        // 마이페이지 버튼
        img_myPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                startActivity(new Intent(MainActivity.this, MyPageActivity.class));
                finish();
            }
        });

        // 로그아웃 버튼
        img_logout.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                session.logout();
            }
        });
    }

    @Override
    public boolean onOptionsItemSelected(MenuItem item) {
        if (mToggle.onOptionsItemSelected(item)) {
            return true;
        }
        return super.onOptionsItemSelected(item);
    }

    @Override
    public boolean onNavigationItemSelected(@NonNull MenuItem item) {
        int id = item.getItemId();

        if (id == R.id.navi_introduction) {
            startActivity(new Intent(MainActivity.this, IntroductionActivity.class));
            finish();
        }
        if (id == R.id.navi_filltering) {
            startActivity(new Intent(MainActivity.this, FillterChoiceActivity.class));
            finish();
        }
        if (id == R.id.navi_hartList) {
            startActivity(new Intent(MainActivity.this, HartListActivity.class));
            finish();
        }
        if (id == R.id.navi_mypage) {
            startActivity(new Intent(MainActivity.this, MyPageActivity.class));
            finish();
        }
        if (id==R.id.navi_home) {
            startActivity(new Intent(MainActivity.this, MainActivity.class));
            finish();
        }

        return false;
    }


}
