package com.ihh.capstone.OTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ihh.capstone.R;
public class StartActivity extends AppCompatActivity {
    Button btn_goLoginPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btn_goLoginPage = findViewById(R.id.btn_goToJoin);

        CheckOTP();

    }
    private void CheckOTP() {

        btn_goLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(StartActivity.this, CheckOTPActivity.class);
                startActivity(intent);
            }
        }
        );
    }
}