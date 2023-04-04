package com.ihh.capstone.OTP;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Context;
import android.content.Intent;
import android.os.Bundle;
import android.view.View;
import android.widget.Toast;

import com.ihh.capstone.R;
import com.ihh.capstone.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    public ActivityStartBinding binding;
    Context context;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        binding = ActivityStartBinding.inflate(getLayoutInflater());

        goLoginPage();
    }


    private void goLoginPage() {
        binding.btnGoLoginPage.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                Intent intent = new Intent(context, LoginActivity.class);
                startActivity(intent);
                Toast.makeText(context, "로그인 화면으로 이동합니다.", Toast.LENGTH_LONG).show();
            }
        }
        );
    }
}