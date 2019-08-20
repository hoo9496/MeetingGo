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
import android.widget.Button;

import java.util.HashMap;

// 소개 조건 선택 화면
public class FillterChoiceActivity extends AppCompatActivity implements NavigationView.OnNavigationItemSelectedListener {

    private Button btn_area, btn_job, btn_religion,  btn_no_smoking, btn_blood;
    private String myNickname, mySex;
    Session session;

    private DrawerLayout mDrawerlayout;
    private ActionBarDrawerToggle mToggle;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_fillter_choice);

        btn_area = (Button) findViewById(R.id.btn_fillter_area);
        btn_job = (Button) findViewById(R.id.btn_fillter_job);
        btn_religion = (Button) findViewById(R.id.btn_fillter_religion);
        btn_no_smoking = (Button) findViewById(R.id.btn_fillter_no_smoking);
        btn_blood = (Button) findViewById(R.id.btn_fillter_blood);

        session = new Session(this);
        HashMap<String, String> user = session.getMyInfo();
        myNickname = user.get(session.NICKNAME);
        mySex = user.get(session.SEX);

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

        btn_area.setOnClickListener(listener);
        btn_job.setOnClickListener(listener);
        btn_religion.setOnClickListener(listener);
        btn_no_smoking.setOnClickListener(listener);
        btn_blood.setOnClickListener(listener);
    }

    // sideBar
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
            startActivity(new Intent(FillterChoiceActivity.this, IntroductionActivity.class));
            finish();
        }
        if (id == R.id.navi_filltering) {
            startActivity(new Intent(FillterChoiceActivity.this, FillterChoiceActivity.class));
            finish();
        }
        if (id == R.id.navi_hartList) {
            startActivity(new Intent(FillterChoiceActivity.this, HartListActivity.class));
            finish();
        }
        if (id == R.id.navi_mypage) {
            startActivity(new Intent(FillterChoiceActivity.this, MyPageActivity.class));
            finish();
        }
        if (id==R.id.navi_home) {
            startActivity(new Intent(FillterChoiceActivity.this, MainActivity.class));
            finish();
        }

        return false;
    }

    Button.OnClickListener listener = new Button.OnClickListener() {
        public void onClick(View v) {
            switch (v.getId())  {
                case R.id.btn_fillter_area:
                    Intent intent1 = new Intent(FillterChoiceActivity.this, FillterIntroductionActivity.class);
                    intent1.putExtra("btnChoice", "area");
                    startActivity(intent1);
                    break;

                case R.id.btn_fillter_job:
                    Intent intent2 = new Intent(FillterChoiceActivity.this, FillterIntroductionActivity.class);
                    intent2.putExtra("btnChoice", "job");
                    startActivity(intent2);
                    break;

                case R.id.btn_fillter_religion:
                    Intent intent3 = new Intent(FillterChoiceActivity.this, FillterIntroductionActivity.class);
                    intent3.putExtra("btnChoice", "religion");
                    startActivity(intent3);
                    break;

                case R.id.btn_fillter_no_smoking:
                    Intent intent4 = new Intent(FillterChoiceActivity.this, FillterIntroductionActivity.class);
                    intent4.putExtra("btnChoice", "no_smoking");
                    startActivity(intent4);
                    break;

                case R.id.btn_fillter_blood:
                    Intent intent5 = new Intent(FillterChoiceActivity.this, FillterIntroductionActivity.class);
                    intent5.putExtra("btnChoice", "blood");
                    startActivity(intent5);
                    break;
            }
        }
    };

    @Override
    public void onBackPressed() {
        startActivity(new Intent(FillterChoiceActivity.this, MainActivity.class));
        finish();
    }

}
