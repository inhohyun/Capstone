package com.ihh.capstone.OTP;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;

import com.ihh.capstone.R;
import com.ihh.capstone.databinding.ActivityStartBinding;

public class StartActivity extends AppCompatActivity {
    public ActivityStartBinding binding;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_start);
        binding = ActivityStartBinding.inflate(getLayoutInflater());

        goLoginPage();
    }

    private void goLoginPage() {

    }
}