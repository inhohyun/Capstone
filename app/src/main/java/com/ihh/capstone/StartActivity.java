package com.ihh.capstone;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ihh.capstone.login.JoinActivity;
import com.ihh.capstone.login.LoginActivity;

public class StartActivity extends AppCompatActivity {
    Button btn_goLoginPage;
    Button btn_goToJoin;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btn_goLoginPage = findViewById(R.id.btn_goToJoin);
        btn_goToJoin = findViewById(R.id.btn_goToJoin);
        goToLogin();
        goToJoin();

    }



    //로그인 버튼 클릭시 로그인 페이지로 이동
    private void goToLogin() {
        btn_goLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, LoginActivity.class);
                startActivity(intent);
            }
        });
    }
    //회원가입 버튼 클릭시 회원가입 페이지로 이동
    private void goToJoin() {
        btn_goToJoin.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, JoinActivity.class);
                startActivity(intent);
            }
        });
    }

}