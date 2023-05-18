package com.ihh.capstone.login;

import androidx.appcompat.app.AppCompatActivity;

import android.content.Intent;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihh.capstone.MainActivity;
import com.ihh.capstone.R;

import java.util.Random;

public class SecondLoginActivity extends AppCompatActivity {
    EditText phoneNum;
    Button btnSendSMS;

    EditText inputCheckNum;
    Button btnCheck;
    //인증번호 코드, 원래 안전을 위해 모델에 저장해야 하지만 일단 액티비티에 둠(유지보수때 수정)
    String verifiedCode;
    @Override
    protected void onCreate(Bundle savedInstanceState) {
        super.onCreate(savedInstanceState);
        setContentView(R.layout.activity_second_login);
        init();
        setSendSMSBtn();
        setSecondLoginBtn();

    }
    //2차 로그인하기 버튼 클릭
    private void setSecondLoginBtn() {
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputVerifiedCode = inputCheckNum.getText().toString();
                //인증 성공
                if (inputVerifiedCode.equals(verifiedCode)){
                    Toast.makeText(SecondLoginActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                    Intent intent = new Intent(SecondLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                //인증 실패
                else{
                    Toast.makeText(SecondLoginActivity.this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    //랜덤 인증번호 생성, 주의할 점 : 인증번호 발송을 클릭했을 때만 호출할 것(번호 바뀜) -> 액티비티 재 실행시 초기화도 고려
    private String createVerifiCode() {
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
        return String.valueOf(code);
    }
    //sms 보내는 메소드
    private void sendVerificationCode(String phoneNumber, String verificationCode) {
        SmsManager smsManager = SmsManager.getDefault();
        smsManager.sendTextMessage(phoneNumber, null, verificationCode, null, null);
    }

    //인증번호 발송 버튼 클릭
    private void setSendSMSBtn() {
        String phoneNumber = phoneNum.getText().toString();
        //랜덤 인증번호 생성
        verifiedCode = createVerifiCode();
        //인증번호 보내기
         sendVerificationCode(phoneNumber, verifiedCode);

    }

    private void init(){
        phoneNum = findViewById(R.id.phoneNumberArea);
        btnSendSMS = findViewById(R.id.btn_sendSMS);

        inputCheckNum = findViewById(R.id.checkNumberArea);
        btnCheck = findViewById(R.id.secondLoginBtn);
    }


}