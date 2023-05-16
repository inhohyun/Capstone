package com.ihh.capstone.login;

import androidx.appcompat.app.AppCompatActivity;

import android.os.Bundle;
import android.widget.Button;
import android.widget.EditText;

import com.ihh.capstone.R;

public class SecondLoginActivity extends AppCompatActivity {
    EditText phoneNum;
    Button btnSendSMS;

    EditText inputCheckNum;
    Button btnCheck;

    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);
        init();
    }

    private void init(){
        phoneNum = findViewById(R.id.phoneNumberArea);
        btnSendSMS = findViewById(R.id.btn_sendSMS);

        inputCheckNum = findViewById(R.id.checkNumberArea);
        btnCheck = findViewById(R.id.secondLoginBtn);
    }
}