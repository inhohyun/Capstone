package com.ihh.capstone.OTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Button;

import com.ihh.capstone.R;
public class StartActivity extends AppCompatActivity {
    Context context;
    Button btn_goLoginPage;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        btn_goLoginPage = findViewById(R.id.btn_goLoginPage);

        goLoginPage();

    }
    private void goLoginPage() {
        btn_goLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
            }
        }
        );
    }
}