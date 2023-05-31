package com.ihh.capstone.login;

import androidx.annotation.NonNull;
import androidx.appcompat.app.AppCompatActivity;
import androidx.core.app.ActivityCompat;
import androidx.core.content.ContextCompat;

import android.Manifest;
import android.content.Intent;
import android.content.pm.PackageManager;
import android.os.Bundle;
import android.telephony.SmsManager;
import android.view.View;
import android.widget.Button;
import android.widget.EditText;
import android.widget.Toast;

import com.ihh.capstone.MainActivity;
import com.ihh.capstone.R;
import com.ihh.capstone.StartActivity;

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

        btnSendSMS.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                //check condition for permission
                if (ContextCompat.checkSelfPermission(SecondLoginActivity.this, Manifest.permission.SEND_SMS)
                        == PackageManager.PERMISSION_GRANTED){
                    //권한이 정상적으로 있을때
                    sendSMS();
                }
                //sms권한이 없을떄
                else{
                    ActivityCompat.requestPermissions(SecondLoginActivity.this, new String[]{Manifest.permission.SEND_SMS},
                            100);
                }
            }
        });
        btnCheck.setOnClickListener(new View.OnClickListener() {
            @Override
            public void onClick(View view) {
                String inputCheck = inputCheckNum.getText().toString();
                //입력값과 인증코드가 같으면 2차 로그인 성공
                if(inputCheck.equals(String.valueOf(verifiedCode))){
                    Toast.makeText(SecondLoginActivity.this, "환영합니다.", Toast.LENGTH_SHORT).show();
                    //메인화면으로 이동
                    Intent intent = new Intent(SecondLoginActivity.this, MainActivity.class);
                    startActivity(intent);
                }
                else{
                    Toast.makeText(SecondLoginActivity.this, "인증번호가 일치하지 않습니다.", Toast.LENGTH_SHORT).show();
                }
            }
        });
    }

    private void sendSMS() {
        String phone = phoneNum.getText().toString();
        verifiedCode = createVerifiCode();

        if(!phone.isEmpty()){
            SmsManager smsManager = SmsManager.getDefault();
            smsManager.sendTextMessage(phone, null, "인증번호 : "+verifiedCode,null,null);
            Toast.makeText(SecondLoginActivity.this, "인증번호가 발송되었습니다.", Toast.LENGTH_SHORT).show();
        }
        //빈 값을 입력했을때
        else{
            Toast.makeText(SecondLoginActivity.this, "번호를 입력해주세요", Toast.LENGTH_SHORT).show();
        }
    }

    //사용자에게 sms 권한 받기
    @Override
    public void onRequestPermissionsResult(int requestCode, @NonNull String[] permissions, @NonNull int[] grantResults) {
        super.onRequestPermissionsResult(requestCode, permissions, grantResults);

        if (requestCode == 100 && grantResults.length > 0 && grantResults[0] == PackageManager.PERMISSION_GRANTED){
            sendSMS();
        }
        else{
            Toast.makeText(SecondLoginActivity.this, "권한이 거부되었습니다.", Toast.LENGTH_SHORT).show();
        }

    }

    //랜덤 인증번호 생성, 주의할 점 : 인증번호 발송을 클릭했을 때만 호출할 것(번호 바뀜) -> 액티비티 재 실행시 초기화도 고려
    private String createVerifiCode() {
        Random random = new Random();
        int code = random.nextInt(9999 - 1000) + 1000;
        return String.valueOf(code);
    }


    //인증번호 발송 버튼 클릭

    private void init(){
        phoneNum = findViewById(R.id.phoneNumberArea);
        btnSendSMS = findViewById(R.id.btn_sendSMS);

        inputCheckNum = findViewById(R.id.checkNumberArea);
        btnCheck = findViewById(R.id.secondLoginBtn);
    }


}